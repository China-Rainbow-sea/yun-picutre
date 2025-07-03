package com.rainbowsea.yupicturebackend.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 自定义权限校验注解
 */
@Target(ElementType.METHOD) // 表示该注解可以放在方法上
@Retention(RetentionPolicy.RUNTIME) // 标识该注解可以被读取到
public @interface AuthCheck {

    /**
     * 必须具有某个角色
     * 默认为 “”
     */
    String mustRole() default "";
}
