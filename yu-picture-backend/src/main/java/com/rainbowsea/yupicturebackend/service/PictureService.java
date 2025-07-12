package com.rainbowsea.yupicturebackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rainbowsea.yupicturebackend.api.aliyunai.model.CreateOutPaintingTaskResponse;
import com.rainbowsea.yupicturebackend.api.imagesearch.model.ImageSearchResult;
import com.rainbowsea.yupicturebackend.model.dto.picture.CreatePictureOutPaintingTaskRequest;
import com.rainbowsea.yupicturebackend.model.dto.picture.PictureEditByBatchRequest;
import com.rainbowsea.yupicturebackend.model.dto.picture.PictureEditRequest;
import com.rainbowsea.yupicturebackend.model.dto.picture.PictureQueryRequest;
import com.rainbowsea.yupicturebackend.model.dto.picture.PictureReviewRequest;
import com.rainbowsea.yupicturebackend.model.dto.picture.PictureUploadByBatchRequest;
import com.rainbowsea.yupicturebackend.model.dto.picture.PictureUploadRequest;
import com.rainbowsea.yupicturebackend.model.dto.picture.SearchPictureByPictureRequest;
import com.rainbowsea.yupicturebackend.model.entity.Picture;
import com.rainbowsea.yupicturebackend.model.entity.User;
import com.rainbowsea.yupicturebackend.model.vo.PictureVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
* @author huo
* @description 针对表【picture(图片)】的数据库操作Service
* @createDate 2025-07-05 20:57:22
*/
public interface PictureService extends IService<Picture> {

    /**
     * 校验图片
     *
     * @param picture
     */
    void validPicture(Picture picture);


    /**
     * 获取图片包装类（分页）
     *
     * @param picturePage
     * @param request
     * @return
     */
    Page<PictureVO> getPictureVOPage(Page<Picture> picturePage, HttpServletRequest request);


    /**
     * 获取图片包装类（单条）
     *
     * @param picture
     * @param request
     * @return
     */
    PictureVO getPictureVO(Picture picture, HttpServletRequest request);



    /**
     * 上传图片
     *
     * @param inputSource 文件输入源（可以是文件/ URL）
     * @param pictureUploadRequest
     * @param loginUser
     * @return
     */
    PictureVO uploadPicture(Object inputSource,
                            PictureUploadRequest pictureUploadRequest,
                            User loginUser);



    /**
     * 获取查询对象
     *
     * @param pictureQueryRequest
     * @return
     */
    QueryWrapper<Picture> getQueryWrapper(PictureQueryRequest pictureQueryRequest);


    /**
     * 图片审核
     *
     * @param pictureReviewRequest
     * @param loginUser
     */
    void doPictureReview(PictureReviewRequest pictureReviewRequest, User loginUser);


    /**
     * 填充审核参数
     * @param picture
     * @param loginUser
     */
    public void fillReviewParams(Picture picture, User loginUser);


    /**
     * 批量抓取和创建图片
     *
     * @param pictureUploadByBatchRequest
     * @param loginUser
     * @return 成功创建的图片数
     */
    Integer uploadPictureByBatch(
            PictureUploadByBatchRequest pictureUploadByBatchRequest,
            User loginUser
    );


    /**
     * 删除图片，包含删除对象存储当中的图片
     * @param oldPicture
     */
    public void clearPictureFile(Picture oldPicture);


    /**
     * 校验空间图片的权限
     * @param loginUser
     * @param picture
     */
    void checkPictureAuth(User loginUser, Picture picture);


    /**
     * 删除图片
     * @param pictureId
     * @param loginUser
     */
    void deletePicture(long pictureId, User loginUser);


    /**
     * 编辑图片
     * @param pictureEditRequest
     * @param loginUser
     */
    void editPicture(PictureEditRequest pictureEditRequest, User loginUser);

    //@Override
    //public List<ImageSearchResult> getSimilarPicture(SearchPictureByPictureRequest request) throws IOException {
    //    // 1.校验参数
    //    Long pictureId = request.getPictureId();
    //    ThrowUtils.throwIf(pictureId == null || pictureId <= 0 ,ErrorCode.NOT_FOUND_ERROR);
    //
    //    // 2.查询数据库
    //    Picture picture = this.getById(pictureId);
    //    ThrowUtils.throwIf(picture == null, ErrorCode.NOT_FOUND_ERROR);
    //
    //    // 3.提取图片在COS中的key
    //    // https://su-picture-1358660290.cos.ap-guangzhou.myqcloud.com/private/1924014007170187266/2025-05-18_4sg0eoeucr.webp
    //    String url = picture.getUrl();
    //    String pictureKey = url.split("su-picture-1358660290.cos.ap-guangzhou.myqcloud.com/")[1];
    //
    //    // 4.下载文件 文件地址 = 当前项目路径 + 图片key（图片key带时间戳本来是唯一）
    //    String suffix = pictureKey.replace('/', '\\');
    //    String localPath = System.getProperty("user.dir") + "\\images\\" + suffix;
    //    cosManager.downloadPicture(pictureKey, localPath);
    //
    //    // 5.返回结果
    //    try {
    //       return ImageSearchApiFacade.searchImage(localPath);
    //        //return ImageSearchFacade.getSimilarImage(localPath);
    //    } finally {
    //        // 删除本地文件及文件夹
    //        File dir = new File(System.getProperty("user.dir") + "\\images\\");
    //
    //        if (dir.isDirectory()) {
    //            // 清空目录
    //            FileUtil.clean(dir);
    //            log.info("Directory cleaned successfully.");
    //        } else {
    //            log.info("The provided path is not a directory.");
    //        }
    //    }
    //}


    /**
     * 颜色查询服务
     * 按颜色查询图片的方法
     *
     * @param spaceId
     * @param picColor
     * @param loginUser
     * @return
     */

    List<PictureVO> searchPictureByColor(Long spaceId, String picColor, User loginUser);


    /**
     * 批量编辑图片
     *
     * @param pictureEditByBatchRequest
     * @param loginUser
     */
    void editPictureByBatch(PictureEditByBatchRequest pictureEditByBatchRequest, User loginUser);


    /**
     * to do 以图搜图 webp 图片搜索问题方案1,
     * @param request
     * @return
     * @throws IOException
     */
    //List<ImageSearchResult> getSimilarPicture(SearchPictureByPictureRequest request) throws IOException;


    /**
     * 创建 阿里百炼扩图任务
     * @param createPictureOutPaintingTaskRequest
     * @param loginUser
     * @return
     */
    public CreateOutPaintingTaskResponse createPictureOutPaintingTask(CreatePictureOutPaintingTaskRequest createPictureOutPaintingTaskRequest,
                                                                      User loginUser);
}
