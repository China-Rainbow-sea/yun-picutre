package com.rainbowsea.yupicturebackend.common;

import com.rainbowsea.yupicturebackend.exception.ErrorCode;
import lombok.Data;

import java.io.Serializable;


/**
 * 通用响应类
 *
 */
@Data
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = -5613320965555244407L;

    private int code;

    private T data;

    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}

