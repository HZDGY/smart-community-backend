package org.sc.smartcommunitybackend.exception;

import lombok.Getter;
import org.sc.smartcommunitybackend.common.ResultCode;

/**
 * 业务异常类
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    @Getter
    private Integer code;

    /**
     * 错误消息
     */
    private String message;

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
        this.message = message;
        this.code = ResultCode.BUSINESS_ERROR.getCode();
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.code = ResultCode.BUSINESS_ERROR.getCode();
    }

    @Override
    public String getMessage() {
        return message;
    }
}

