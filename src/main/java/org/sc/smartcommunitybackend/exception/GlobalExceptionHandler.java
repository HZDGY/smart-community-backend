package org.sc.smartcommunitybackend.exception;

import org.sc.smartcommunitybackend.common.Result;
import org.sc.smartcommunitybackend.common.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleBusinessException(BusinessException e) {
        logger.warn("业务异常: {}", e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理权限不足异常
     */
    @ExceptionHandler(PermissionDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result<Void> handlePermissionDeniedException(PermissionDeniedException e) {
        logger.warn("权限不足: {}", e.getMessage());
        return Result.error(403, e.getMessage());
    }


    /**
     * 处理参数校验异常（@RequestBody）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        logger.warn("参数校验异常: {}", message);
        return Result.error(ResultCode.PARAM_ERROR.getCode(), message);
    }

    /**
     * 处理参数校验异常（@ModelAttribute）
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleBindException(BindException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        logger.warn("参数校验异常: {}", message);
        return Result.error(ResultCode.PARAM_ERROR.getCode(), message);
    }

    /**
     * 处理参数校验异常（@RequestParam、@PathVariable）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String message = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        logger.warn("参数校验异常: {}", message);
        return Result.error(ResultCode.PARAM_ERROR.getCode(), message);
    }

    /**
     * 处理参数类型转换异常（@RequestParam、@PathVariable）
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String paramName = e.getName();
        String requiredType = e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "未知类型";
        String providedValue = e.getValue() != null ? e.getValue().toString() : "null";
        String message = String.format("参数 '%s' 的值 '%s' 无法转换为所需的类型 %s", 
                                      paramName, providedValue, requiredType);
        logger.warn("参数类型转换异常: {}", message);
        return Result.error(ResultCode.PARAM_ERROR.getCode(), message);
    }

    /**
     * 处理静态资源找不到异常（不打印错误日志，避免干扰）
     */
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNoResourceFoundException(NoResourceFoundException e) {
        // 静态资源找不到，不做处理，让Spring处理404
        logger.debug("静态资源未找到: {}", e.getResourcePath());
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleRuntimeException(RuntimeException e) {
        logger.error("运行时异常", e);
        return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e) {
        logger.error("系统异常", e);
        return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
    }
}

