package com.frame.annotation.handle;

import com.frame.annotation.RequiresRoles;
import com.frame.business.base.BaseController;
import com.frame.business.entity.UserRole;
import com.frame.business.service.JwtService;
import com.frame.exception.BusinessException;
import com.frame.exception.code.BaseResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/4/10
 * Time: 22:42
 * Description: 权限校验 处理类
 */
@Aspect
@Slf4j
@Component
public class AuthHandle {

    @Autowired
    private BaseController baseController;

    @Autowired
    private JwtService jwtService;

    @Pointcut(" execution(public * com.frame.business.controller.*.*(..))")
    public void pointCut() { }

    @Before(value = "pointCut() && @annotation(requiresRoles)")
    public void before(JoinPoint jp , RequiresRoles requiresRoles) {
        String className = jp.getThis().toString();
        String methodName = jp.getSignature().getName();
        log.info("权限校验：类名{},函数名{}", className, methodName);

        String token = baseController.getToken();

        if (StringUtils.isEmpty(token)) {
            throw new BusinessException(BaseResponseCode.NOT_LOGIN);
        }

        try {
            if (!jwtService.verify(token)) {
                throw new BusinessException(BaseResponseCode.OUT_OF_EXP_TOKEN);
            }
        } catch (Exception e) {
            throw new BusinessException(BaseResponseCode.OUT_OF_EXP_TOKEN);
        }

        // 权限问题
        String[] targetRoles = requiresRoles.value();

        // 当前用户的角色信息
        List<String> currentRoles = baseController.getCurrentRoles();

        if (currentRoles.isEmpty()){
            throw new BusinessException(BaseResponseCode.NOT_PERMISSION);
        }

        if (targetRoles.length == 1) {
            if (targetRoles[0].equals(UserRole.NORMAL_USER.getRoleName())) {
                // 普通用户
                return;
            }
            if (currentRoles.contains(UserRole.MANAGE_ADMIN.getRoleName())) {
                // 系统管理员
                return;
            }
        }

        for (String targetRole : targetRoles) {
            if (!currentRoles.contains(targetRole)) {
                throw new BusinessException(BaseResponseCode.NOT_PERMISSION);
            }
        }
    }

}
