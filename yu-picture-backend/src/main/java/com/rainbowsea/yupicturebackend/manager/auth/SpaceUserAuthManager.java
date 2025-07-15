package com.rainbowsea.yupicturebackend.manager.auth;


import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.rainbowsea.yupicturebackend.manager.auth.model.SpaceUserAuthConfig;
import com.rainbowsea.yupicturebackend.manager.auth.model.SpaceUserRole;
import com.rainbowsea.yupicturebackend.model.entity.Space;
import com.rainbowsea.yupicturebackend.model.entity.SpaceUser;
import com.rainbowsea.yupicturebackend.model.entity.User;
import com.rainbowsea.yupicturebackend.model.enums.SpaceRoleEnum;
import com.rainbowsea.yupicturebackend.model.enums.SpaceTypeEnum;
import com.rainbowsea.yupicturebackend.service.SpaceUserService;
import com.rainbowsea.yupicturebackend.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/** 空间成员权限管理
 *
 */
@Component
public class SpaceUserAuthManager {

    public static final SpaceUserAuthConfig SPACE_USER_AUTH_CONFIG;

    @Resource
    private UserService userService;


    @Resource
    private SpaceUserService spaceUserService;



    // 通过静态代码块对 SPACE_USER_AUTH_CONFIG 进行赋值
    static {
        // 使用 hutool 读取 resources/biz/下的 spaceUserAuthConfig.json 文件
        String json = ResourceUtil.readUtf8Str("biz/spaceUserAuthConfig.json");
        // 再使用 hutool 工具类，将 JSON 文件的内容，赋值到 SpaceUserAuthConfig类对象中
        // 因为我们的SpaceUserAuthConfig 对象类，就是根据 这个 spaceUserAuthConfig.json 的属性值，创建的字段类
        // 所以可以直接使用 hutool 让我们一一映射赋值上
        SPACE_USER_AUTH_CONFIG = JSONUtil.toBean(json, SpaceUserAuthConfig.class);
    }


    /**
     *  根据角色获取权限列表
     * @param spaceUserRole
     * @return
     */
    public List<String> getPermissionsByRole(String spaceUserRole) {
        if(StrUtil.isBlank(spaceUserRole)) {
            return new ArrayList<>();
        }
        // 找到匹配的角色
        SpaceUserRole role = SPACE_USER_AUTH_CONFIG.getRoles().stream()
                .filter(r -> spaceUserRole.equals(r.getKey()))
                .findFirst()
                .orElse(null);  // 如果不存在返回 null
        if (role == null) {
            return new ArrayList<>();
        }
        return role.getPermissions();
    }



    /**
     * 获取权限列表
     *
     * @param space
     * @param loginUser
     * @return
     */
    public List<String> getPermissionList(Space space, User loginUser) {
        if (loginUser == null) {
            return new ArrayList<>();
        }
        // 管理员权限
        List<String> ADMIN_PERMISSIONS = getPermissionsByRole(SpaceRoleEnum.ADMIN.getValue());
        // 公共图库
        if (space == null) {
            if (userService.isAdmin(loginUser)) {
                return ADMIN_PERMISSIONS;
            }
            return new ArrayList<>();
        }
        SpaceTypeEnum spaceTypeEnum = SpaceTypeEnum.getEnumByValue(space.getSpaceType());
        if (spaceTypeEnum == null) {
            return new ArrayList<>();
        }
        // 根据空间获取对应的权限
        switch (spaceTypeEnum) {
            case PRIVATE:
                // 私有空间，仅本人或管理员有所有权限
                if (space.getUserId().equals(loginUser.getId()) || userService.isAdmin(loginUser)) {
                    return ADMIN_PERMISSIONS;
                } else {
                    return new ArrayList<>();
                }
            case TEAM:
                // 团队空间，查询 SpaceUser 并获取角色和权限
                SpaceUser spaceUser = spaceUserService.lambdaQuery()
                        .eq(SpaceUser::getSpaceId, space.getId())
                        .eq(SpaceUser::getUserId, loginUser.getId())
                        .one();
                if (spaceUser == null) {
                    return new ArrayList<>();
                } else {
                    return getPermissionsByRole(spaceUser.getSpaceRole());
                }
        }
        return new ArrayList<>();
    }

}
