package org.sc.smartcommunitybackend.config;

import org.sc.smartcommunitybackend.interceptor.JwtAuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtAuthInterceptor jwtAuthInterceptor;
    @Value("${app.interceptor.enabled:true}")
    private boolean interceptorEnabled;
    /**
     * 配置拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (interceptorEnabled) {
        registry.addInterceptor(jwtAuthInterceptor)
                // 拦截所有请求
                .addPathPatterns("/**")
                // 排除不需要认证的路径
                .excludePathPatterns(
                        // 用户注册、登录和忘记密码
                        "/api/user/register",
                        "/api/user/login",
                        "/api/user/forgot-password",
                        
                        // Knife4j文档相关
                        "/doc.html",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/favicon.ico",
                        
                        // 静态资源
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/fonts/**",
                        
                        // 上传的文件访问（公开）
                        "/uploads/**",
                        
                        // 健康检查等
                        "/actuator/**",
                        "/error"
                );
    }}

    /**
     * 配置静态资源映射
     * 确保Knife4j的静态资源能够正确访问
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Knife4j 文档静态资源
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        
        // Swagger UI 资源
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/swagger-ui/");
    }

    /**
     * 配置视图控制器
     * 将根路径重定向到API文档
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 可选：将根路径重定向到Knife4j文档
        // registry.addRedirectViewController("/", "/doc.html");
    }
}

