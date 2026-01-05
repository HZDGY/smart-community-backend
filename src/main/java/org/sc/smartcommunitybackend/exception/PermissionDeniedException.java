package org.sc.smartcommunitybackend.exception;

/**
 * 权限不足异常
 */
public class PermissionDeniedException extends RuntimeException {
    
    public PermissionDeniedException(String message) {
        super(message);
    }
    
    public PermissionDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}
