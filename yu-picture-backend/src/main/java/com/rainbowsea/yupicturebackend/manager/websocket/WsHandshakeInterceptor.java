package com.rainbowsea.yupicturebackend.manager.websocket;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.rainbowsea.yupicturebackend.manager.auth.SpaceUserAuthManager;
import com.rainbowsea.yupicturebackend.manager.auth.model.SpaceUserPermissionConstant;
import com.rainbowsea.yupicturebackend.model.entity.Picture;
import com.rainbowsea.yupicturebackend.model.entity.Space;
import com.rainbowsea.yupicturebackend.model.entity.User;
import com.rainbowsea.yupicturebackend.model.enums.SpaceTypeEnum;
import com.rainbowsea.yupicturebackend.service.PictureService;
import com.rainbowsea.yupicturebackend.service.SpaceService;
import com.rainbowsea.yupicturebackend.service.UserService;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 * WebSocket 拦截器，建立连接前要先校验
 */
@Component
@Slf4j
public class WsHandshakeInterceptor implements HandshakeInterceptor {

    @Resource
    private UserService userService;

    @Resource
    private PictureService pictureService;

    @Resource
    private SpaceService spaceService;

    @Resource
    private SpaceUserAuthManager spaceUserAuthManager;


    /**
     * WebSocket 每次建立连接握手前执行 该方法 beforeHandshake()方法 ，返回 true 表示放行，返回 false 表示拦截
     * 这里我们编写一些，建立连接，握手之前的连接之前，进行校验，因为只有团队空间，才有该协作功能
     * 没有团队空间，就不需要建立连接了
     * @param request
     * @param response
     * @param wsHandler
     * @param attributes 给 WebSocketSession 会话设置属性
     * @return
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest httpServletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            // 从请求中获取参数
            String pictureId = httpServletRequest.getParameter("pictureId");
            if (StrUtil.isBlank(pictureId)) {
                log.error("缺少图片参数，拒绝握手");
                return false;
            }
            // 获取当前登录用户
            User loginUser = userService.getLoginUser(httpServletRequest);
            if (ObjUtil.isEmpty(loginUser)) {
                log.error("用户未登录，拒绝握手");
                return false;
            }
            // 校验用户是否有编辑当前图片的权限
            Picture picture = pictureService.getById(pictureId);
            if (ObjUtil.isEmpty(picture)) {
                log.error("图片不存在，拒绝握手");
                return false;
            }
            Long spaceId = picture.getSpaceId();
            Space space = null;
            if (spaceId != null) {
                space = spaceService.getById(spaceId);
                if (ObjUtil.isEmpty(space)) {
                    log.error("图片所在空间不存在，拒绝握手");
                    return false;
                }
                if (space.getSpaceType() != SpaceTypeEnum.TEAM.getValue()) {
                    log.error("图片所在空间不是团队空间，拒绝握手");
                    return false;
                }
            }
            List<String> permissionList = spaceUserAuthManager.getPermissionList(space, loginUser);
            if (!permissionList.contains(SpaceUserPermissionConstant.PICTURE_EDIT)) {
                log.error("用户没有编辑图片的权限，拒绝握手");
                return false;
            }
            // 设置用户登录信息等属性到 WebSocket 会话中
            attributes.put("user", loginUser);
            attributes.put("userId", loginUser.getId());
            attributes.put("pictureId", Long.valueOf(pictureId)); // 记得转换为 Long 类型
        }
        return true;
    }


    /**
     * WebSocket 每次建立连接后，握手后执行 该方法 afterHandshake()方法
     * @param request
     * @param response
     * @param wsHandler
     * @param exception
     */
    @Override
    public void afterHandshake(@NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response, @NotNull WebSocketHandler wsHandler, Exception exception) {
    }
}
