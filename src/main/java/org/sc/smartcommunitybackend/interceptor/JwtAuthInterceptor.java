package org.sc.smartcommunitybackend.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.sc.smartcommunitybackend.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT认证拦截器
 * 拦截需要认证的请求，验证JWT token的有效性
 */
@Component
public class JwtAuthInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthInterceptor.class);

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 在请求处理之前进行调用
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 从请求头中获取token
        String token = getTokenFromRequest(request);
        
        if (token == null || token.isEmpty()) {
            logger.warn("请求未携带token: {}", request.getRequestURI());
            sendUnauthorizedResponse(response, "未登录，请先登录");
            return false;
        }

        try {
            // 2. 验证token是否过期
            if (jwtUtil.isTokenExpired(token)) {
                logger.warn("token已过期: {}", token);
                sendUnauthorizedResponse(response, "登录已过期，请重新登录");
                return false;
            }

            // 3. 从token中获取用户信息
            Long userId = jwtUtil.getUserIdFromToken(token);
            String phone = jwtUtil.getPhoneFromToken(token);

            // 4. 将用户信息存入request中，供后续使用
            request.setAttribute("userId", userId);
            request.setAttribute("phone", phone);
            request.setAttribute("token", token);

            logger.debug("用户认证成功: userId={}, phone={}", userId, phone);
            return true;

        } catch (Exception e) {
            logger.error("token验证失败: {}", e.getMessage());
            sendUnauthorizedResponse(response, "token验证失败，请重新登录");
            return false;
        }
    }

    /**
     * 从请求中提取token
     * 支持两种方式：
     * 1. Authorization header: Bearer {token}
     * 2. token参数: ?token={token}
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        // 1. 优先从Authorization header中获取
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // 移除 "Bearer " 前缀
        }

        // 2. 从请求参数中获取
        String token = request.getParameter("token");
        if (token != null && !token.isEmpty()) {
            return token;
        }

        return null;
    }

    /**
     * 发送401未授权响应
     */
    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        
        String jsonResponse = String.format(
            "{\"code\":401,\"message\":\"%s\",\"data\":null}",
            message
        );
        
        response.getWriter().write(jsonResponse);
    }
}

