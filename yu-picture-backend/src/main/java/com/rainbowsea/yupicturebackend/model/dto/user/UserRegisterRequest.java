package com.rainbowsea.yupicturebackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;


/**
 * 用户注册请求类
 */
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = -700516754391219416L;


    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 确认密码
     */
    private String checkPassword;
}
