package com.rainbowsea.yupicturebackend.manager.upload;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.ciModel.persistence.CIObject;
import com.qcloud.cos.model.ciModel.persistence.ImageInfo;
import com.qcloud.cos.model.ciModel.persistence.ProcessResults;
import com.rainbowsea.yupicturebackend.config.CosClientConfig;
import com.rainbowsea.yupicturebackend.exception.BusinessException;
import com.rainbowsea.yupicturebackend.exception.ErrorCode;
import com.rainbowsea.yupicturebackend.manager.CosManager;
import com.rainbowsea.yupicturebackend.model.dto.file.UploadPictureResult;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * 图片上传模板：
 * 模板方法模式
 */

@Slf4j
public abstract class PictureUploadTemplate {

    @Resource
    protected CosManager cosManager;

    @Resource
    protected CosClientConfig cosClientConfig;

    /**
     * 模板方法，定义上传流程 上传图片(图片压缩格式改为 webp ，以及缩略图处理)
     *
     * @param inputSource      文件
     * @param uploadPathPrefix 上传路径前缀
     * @return
     */
    public final UploadPictureResult uploadPicture(Object inputSource, String uploadPathPrefix) {
        // 1. 校验图片
        validPicture(inputSource);

        // 2. 图片上传地址
        String uuid = RandomUtil.randomString(16);
        String originFilename = getOriginFilename(inputSource);
        String uploadFilename = String.format("%s_%s.%s", DateUtil.formatDate(new Date()), uuid,
                FileUtil.getSuffix(originFilename));
        String uploadPath = String.format("/%s/%s", uploadPathPrefix, uploadFilename);

        File file = null;
        try {
            // 3. 创建临时文件
            file = File.createTempFile(uploadPath, null);
            // 处理文件来源（本地或 URL）
            processFile(inputSource, file);

            // 4. 上传图片到对象存储
            PutObjectResult putObjectResult = cosManager.putPictureObject(uploadPath, file);
            ImageInfo imageInfo = putObjectResult.getCiUploadResult().getOriginalInfo().getImageInfo();


            // 5. 获取到图片处理结果
            ProcessResults processResults = putObjectResult.getCiUploadResult().getProcessResults();
            List<CIObject> objectList = processResults.getObjectList();
            if (CollUtil.isNotEmpty(objectList)) {
                CIObject compressedCiObject = objectList.get(0);  // 第一个规则(图片压缩格式转换 webp)后处理后图片的结果
                // 缩略图默认等于压缩图
                CIObject thumbnailCiObject = compressedCiObject;
                // 当有生成缩略图,才获取 缩略图，处理的图片结果
                if (objectList.size() > 1) {
                    thumbnailCiObject = objectList.get(1);  // 第二个规则(缩略图处理)后处理后图片的结果
                }

                // 封装压缩图和 缩略图处理返回结果
                return buildResult(originFilename, compressedCiObject, thumbnailCiObject, imageInfo);
            }

            // 5. 封装返回结果
            return buildResult(originFilename, file, uploadPath, imageInfo);
        } catch (Exception e) {
            log.error("图片上传到对象存储失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        } finally {
            // 6. 清理临时文件
            deleteTempFile(file);
        }
    }

    /**
     * 校验输入源（本地文件或 URL）
     */
    protected abstract void validPicture(Object inputSource);

    /**
     * 获取输入源的原始文件名
     */
    protected abstract String getOriginFilename(Object inputSource);

    /**
     * 处理输入源并生成本地临时文件
     */
    protected abstract void processFile(Object inputSource, File file) throws Exception;

    /**
     * 封装返回结果
     *
     * @param originFilename
     * @param file
     * @param uploadPath
     * @param imageInfo        对象存储返回的图片信息
     * @return
     */
    private UploadPictureResult buildResult(String originFilename, File file, String uploadPath, ImageInfo imageInfo) {
        UploadPictureResult uploadPictureResult = new UploadPictureResult();
        int picWidth = imageInfo.getWidth();
        int picHeight = imageInfo.getHeight();
        double picScale = NumberUtil.round(picWidth * 1.0 / picHeight, 2).doubleValue();
        uploadPictureResult.setPicName(FileUtil.mainName(originFilename));
        uploadPictureResult.setPicWidth(picWidth);
        uploadPictureResult.setPicHeight(picHeight);
        uploadPictureResult.setPicScale(picScale);
        uploadPictureResult.setPicFormat(imageInfo.getFormat());
        uploadPictureResult.setPicSize(FileUtil.size(file));
        uploadPictureResult.setPicColor(imageInfo.getAve());
        uploadPictureResult.setUrl(cosClientConfig.getHost() + "/" + uploadPath);
        return uploadPictureResult;
    }


    /**
     * 封装图片规则处理(压缩)后的，返回结果
     *
     * @param originFilename     没压缩的原图
     * @param compressedCiObject 压缩后的对象
     * @return
     */
    private UploadPictureResult buildResult(String originFilename, CIObject compressedCiObject) {
        UploadPictureResult uploadPictureResult = new UploadPictureResult();
        int picWidth = compressedCiObject.getWidth();
        int picHeight = compressedCiObject.getHeight();
        double picScale = NumberUtil.round(picWidth * 1.0 / picHeight, 2).doubleValue();
        uploadPictureResult.setPicName(FileUtil.mainName(originFilename));
        uploadPictureResult.setPicWidth(picWidth);
        uploadPictureResult.setPicHeight(picHeight);
        uploadPictureResult.setPicScale(picScale);
        uploadPictureResult.setPicFormat(compressedCiObject.getFormat());
        uploadPictureResult.setPicSize(compressedCiObject.getSize().longValue());
        // 设置图片为压缩后的地址
        uploadPictureResult.setUrl(cosClientConfig.getHost() + "/" + compressedCiObject.getKey());

        return uploadPictureResult;
    }


    /**
     * 封装图片规则处理(压缩) 和 缩略图处理 后的，返回结果
     *
     * @param originFilename     原始文件名
     * @param compressedCiObject 压缩后的对象
     * @param thumbnailCiObject  缩略图对象
     * @param imageInfo 图片信息
     * @return
     */
    private UploadPictureResult buildResult(String originFilename, CIObject compressedCiObject,
                                            CIObject thumbnailCiObject, ImageInfo imageInfo) {

        // 计算宽高
        int picWidth = compressedCiObject.getWidth();
        int picHeight = compressedCiObject.getHeight();
        double picScale = NumberUtil.round(picWidth * 1.0 / picHeight, 2).doubleValue();

        // 封装其返回结果
        UploadPictureResult uploadPictureResult = new UploadPictureResult();

        // 设置压缩后的原图地址
        uploadPictureResult.setPicName(FileUtil.mainName(originFilename));
        uploadPictureResult.setPicWidth(picWidth);
        uploadPictureResult.setPicHeight(picHeight);
        uploadPictureResult.setPicScale(picScale);
        uploadPictureResult.setPicFormat(compressedCiObject.getFormat());
        uploadPictureResult.setPicSize(compressedCiObject.getSize().longValue());
        uploadPictureResult.setPicColor(imageInfo.getAve());

        // 设置图片为压缩后的地址
        uploadPictureResult.setUrl(cosClientConfig.getHost() + "/" + compressedCiObject.getKey());
        // 设置缩略图
        uploadPictureResult.setThumbnailUrl(cosClientConfig.getHost() + "/" + thumbnailCiObject.getKey());

        return uploadPictureResult;
    }


    /**
     * 删除临时文件
     */
    public void deleteTempFile(File file) {
        if (file == null) {
            return;
        }
        boolean deleteResult = file.delete();
        if (!deleteResult) {
            log.error("file delete error, filepath = {}", file.getAbsolutePath());
        }
    }
}
