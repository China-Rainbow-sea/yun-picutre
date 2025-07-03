package com.rainbowsea.yupicturebackend.model.dto.user;

import com.rainbowsea.yupicturebackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户查询请求
 */

@EqualsAndHashCode(callSuper = true) // 表示支持 继承的 extends 父类也支持 lombok 生成 get set 方法
@Data
public class UserQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = -1511257645614570093L;


    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;
}
