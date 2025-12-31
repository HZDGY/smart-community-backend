package org.sc.smartcommunitybackend.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 用户上下文工具类
 * 用于在请求处理过程中获取当前登录用户信息
 */
public class UserContextUtil {

    /**
     * 获取当前请求的HttpServletRequest
     */
    private static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        return attributes.getRequest();
    }

    /**
     * 获取当前登录用户ID
     *
     * @return 用户ID，如果未登录返回null
     */
    public static Long getCurrentUserId() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        Object userId = request.getAttribute("userId");
        if (userId instanceof Long) {
            return (Long) userId;
        }
        return null;
    }

    /**
     * 获取当前登录用户手机号
     *
     * @return 手机号，如果未登录返回null
     */
    public static String getCurrentUserPhone() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        Object phone = request.getAttribute("phone");
        if (phone instanceof String) {
            return (String) phone;
        }
        return null;
    }

    /**
     * 获取当前请求的token
     *
     * @return token字符串，如果未登录返回null
     */
    public static String getCurrentToken() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        Object token = request.getAttribute("token");
        if (token instanceof String) {
            return (String) token;
        }
        return null;
    }

    /**
     * 检查当前是否已登录
     *
     * @return true-已登录，false-未登录
     */
    public static boolean isAuthenticated() {
        return getCurrentUserId() != null;
    }
}

