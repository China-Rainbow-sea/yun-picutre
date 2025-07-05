package com.rainbowsea.yupicturebackend.controller;


import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qcloud.cos.utils.IOUtils;
import com.rainbowsea.yupicturebackend.annotation.AuthCheck;
import com.rainbowsea.yupicturebackend.common.BaseResponse;
import com.rainbowsea.yupicturebackend.common.ResultUtils;
import com.rainbowsea.yupicturebackend.constant.UserConstant;
import com.rainbowsea.yupicturebackend.exception.BusinessException;
import com.rainbowsea.yupicturebackend.exception.ErrorCode;
import com.rainbowsea.yupicturebackend.manager.CosManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private CosManager cosManager;


    /**
     * 测试文件下载
     * @param filepath 文件路径
     * @param response 响应对象
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)  // 目前只有管理员才可以下载文件
    @GetMapping("/test/download")
    public void testDownloadFile(String filepath, HttpServletResponse response) throws IOException {

        COSObject cosObject = cosManager.getObject(filepath);
        COSObjectInputStream cosObjectInput = cosObject.getObjectContent();

        try {
            // 处理下载到的流
            byte[] bytes = IOUtils.toByteArray(cosObjectInput);
            // 通过最原始的 response ，设置响应，设置响应头
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + filepath);
            // 写入响应
            response.getOutputStream().write(bytes);
            // 刷新写入
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("file download error, filepath = " + filepath,e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"下载失败");
        } finally {
            // 释放流
            if(cosObjectInput != null ) {
                cosObjectInput.close();
            }
        }


    }


    /**
     * 测试文件上传
     * @param multipartFile 文件对象
     * @return 返回该图片的存放路径
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)  // 目前只有管理员才可以上传文件
    @PostMapping("/test/upload")  // 传输文件，大文件，用 post方式
    public BaseResponse<String> testUploadFile(@RequestPart("file") MultipartFile multipartFile) {
        // 文件目录
        String filename = multipartFile.getOriginalFilename(); // 获取到文件名
        String filepath = String.format("/test/%s", filename); // 文件存储路径

        File file = null;

        try {
            file = File.createTempFile(filepath,null);
            multipartFile.transferTo(file);
            cosManager.putObject(filepath,file);

            // 返回可访问的地址
            return ResultUtils.success(filepath);
        } catch (Exception e) {
            log.error("file upload error, filepath = " + filepath,e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"上传失败");
        } finally {
            if(file != null) {
                // 删除临时文件，因为这里我们上传文件是，将前端的文件存储到本地服务后,再存储到对象存储当中的
                // 中间有一个临时文件,需要删除,不然就一直存在了
                boolean delete = file.delete();
                if(!delete) {
                    log.error("file delete error , filepath = {}",filepath);
                }
            }
        }
    }
}
