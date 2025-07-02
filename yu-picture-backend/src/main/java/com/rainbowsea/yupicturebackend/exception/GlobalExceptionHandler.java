package com.rainbowsea.yupicturebackend.exception;


import com.rainbowsea.yupicturebackend.common.BaseResponse;
import com.rainbowsea.yupicturebackend.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(BusinessException.class) // 捕获该异常处理 BusinessException
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException" ,e);
        // 异常没有数据，不传数据
        return ResultUtils.error(e.getCode(),e.getMessage());
    }



    @ExceptionHandler(RuntimeException.class) // 捕获该异常处理 RuntimeException
    public BaseResponse<?> businessExceptionHandler(RuntimeException e) {
        log.error("RuntimeException" ,e);
        // 异常没有数据，不传数据
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,"系统错误");
    }


}
