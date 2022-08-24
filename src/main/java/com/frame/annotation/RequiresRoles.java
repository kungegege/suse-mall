package com.frame.annotation;

import com.frame.business.entity.Role;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/4/10
 * Time: 15:30
 * Description: 用户登录
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequiresRoles {

    String[] value() default "user";
}
