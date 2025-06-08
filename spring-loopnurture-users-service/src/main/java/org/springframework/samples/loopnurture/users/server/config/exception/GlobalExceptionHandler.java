package org.springframework.samples.loopnurture.users.server.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.samples.loopnurture.users.domain.exception.LoginFailedException;
import org.springframework.samples.loopnurture.users.domain.exception.ResourceNotFoundException;
import org.springframework.samples.loopnurture.users.domain.exception.UnauthorizedException;
import org.springframework.samples.loopnurture.users.domain.exception.UserUniqExistsException;
import org.springframework.samples.loopnurture.users.server.controller.dto.ApiResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理用户唯一标识已存在异常
     */
    @ExceptionHandler(UserUniqExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse<?> handleUserUniqExistsException(UserUniqExistsException ex) {
        log.warn("用户唯一标识已存在: {}", ex.getUserUniq());
        return ApiResponse.error(40901, ex.getMessage());
    }

    /**
     * 处理登录失败异常
     */
    @ExceptionHandler(LoginFailedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<?> handleLoginFailedException(LoginFailedException ex) {
        log.warn("登录失败: {}", ex.getMessage());
        return ApiResponse.error(40101, ex.getMessage());
    }

    /**
     * 处理未授权异常
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<?> handleUnauthorizedException(UnauthorizedException ex) {
        log.warn("未授权访问: {}", ex.getMessage());
        return ApiResponse.error(40102, ex.getMessage());
    }

    /**
     * 处理资源未找到异常
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.warn("资源未找到: {}", ex.getMessage());
        return ApiResponse.error(40401, ex.getMessage());
    }

    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("参数验证失败: {}", ex.getMessage());
        return ApiResponse.error(40001, ex.getMessage());
    }

    /**
     * 处理其他未预期的异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handleException(Exception ex) {
        log.error("未预期的异常", ex);
        return ApiResponse.error(500, "服务器内部错误");
    }
} 