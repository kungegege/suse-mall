package com.frame.exception.handler;

import cn.hutool.core.exceptions.ValidateException;
import com.frame.exception.code.BaseResponseCode;
import com.frame.exception.BusinessException;
import com.frame.utils.DataResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @ClassName: RestExceptionHandler
 * TODO:类文件简单描述
 * @Author: 小霍
 * @UpdateUser: 小霍
 * @Version: 0.0.1
 */
@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    public DataResult handleException(Exception e) {
        log.error("handleException....{}", e);
        return DataResult.getResult(BaseResponseCode.SYSTEM_ERROR);
    }

    @ExceptionHandler(BusinessException.class)
    public DataResult handlerBusinessException(BusinessException e) {
        log.error("BusinessException ...{}", e);
        return DataResult.getResult(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public DataResult handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        log.error("handlerMethodArgumentNotValidException  AllErrors:{} MethodArgumentNotValidException:{}", e.getBindingResult().getAllErrors(), e);
        String msg = null;
        for (ObjectError error : allErrors) {
            msg = error.getDefaultMessage();
            break;
        }
        return DataResult.getResult(BaseResponseCode.VALIDATOR_ERROR.getCode(), msg);
    }

    @ExceptionHandler(ValidateException.class)
    public DataResult handlerValidateException(ValidateException e) {
        String message = e.getMessage();

        if (message.contains("exp")) {
            // token 过期
            return DataResult.getResult(BaseResponseCode.OUT_OF_EXP_TOKEN);
        }

        return DataResult.getResult(BaseResponseCode.ACCOUNT_PASSWORD_ERROR);
    }
}
