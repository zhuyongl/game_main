package com.tencent.wxcloudrun.exception;

import com.tencent.wxcloudrun.common.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ApiResponse handleBusinessException(BusinessException e, HttpServletRequest request) {
        logger.error("业务异常，请求地址: {}, 异常信息: {}", request.getRequestURI(), e.getMessage());
        return ApiResponse.error(e.getMessage());
    }

    /**
     * 处理参数验证异常（@Validated）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        logger.error("参数验证异常，请求地址: {}, 异常信息: {}", request.getRequestURI(), e.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ApiResponse.error("参数验证失败: " + errors.toString());
    }

    /**
     * 处理参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    public ApiResponse handleBindException(BindException e, HttpServletRequest request) {
        logger.error("参数绑定异常，请求地址: {}, 异常信息: {}", request.getRequestURI(), e.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ApiResponse.error("参数绑定失败: " + errors.toString());
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public ApiResponse handleException(Exception e, HttpServletRequest request) {
        logger.error("系统异常，请求地址: {}", request.getRequestURI(), e);
        return ApiResponse.error("系统内部错误");
    }
}