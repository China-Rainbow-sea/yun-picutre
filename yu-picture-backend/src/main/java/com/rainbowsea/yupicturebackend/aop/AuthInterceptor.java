package com.rainbowsea.yupicturebackend.aop;


import com.rainbowsea.yupicturebackend.annotation.AuthCheck;
import com.rainbowsea.yupicturebackend.exception.BusinessException;
import com.rainbowsea.yupicturebackend.exception.ErrorCode;
import com.rainbowsea.yupicturebackend.model.entity.User;
import com.rainbowsea.yupicturebackend.model.enums.UserRoleEnum;
import com.rainbowsea.yupicturebackend.service.UserService;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * AuthCheck注解: 用户权限校验拦截-AOP切面实现
 */
@Aspect  // 启动AOP切面
@Component  // 交给 Spring IOC 容器管理
public class AuthInterceptor {

    @Resource
    private UserService userService;


    /**
     * 执行拦截
     *
     * @param joinPoint 切入点
     * @param authCheck 权限校验注解
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        // 读取注解当中的属性信息
        String mustRole = authCheck.mustRole();

        // 获取当前上下文
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        // 通过上下文，获取到 request 对象
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        // 获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        // 获取当前注解上的角色权限值
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
        // 以下的代码：必须有权限，才会通过
        if(mustRoleEnum == null) {
            return joinPoint.proceed();
        }
        // 如果注解上没有标注权限，则表示该方法不需要权限，放行
        // 获取当前用户的角色权限值
        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(loginUser.getUserRole());
        if (userRoleEnum == null) {  // 用户权限为 null 表示用户没有登录
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 如果注解上标注了权限，但该当前用户却没有该注解标注的权限，则拦截，不放行
        // 注解上标注了 admin 管理员权限   ，但是该用户不是 admin 管理员 ——>拦截
        // 要求必须有管理员权限，但用户没有管理员权限，拒绝
        if(UserRoleEnum.ADMIN.equals(mustRoleEnum) && !UserRoleEnum.ADMIN.equals(userRoleEnum)) {
            throw  new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 通过权限校验，放行
        return joinPoint.proceed();
    }
}
