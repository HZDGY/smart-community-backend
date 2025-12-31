package org.sc.smartcommunitybackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * 文件上传配置
 */
@Configuration
public class FileUploadConfig implements WebMvcConfigurer {

    @Value("${file.upload.path}")
    private String uploadPath;

    /**
     * 配置静态资源访问
     * 将文件上传目录映射为可访问的URL路径
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 确保上传目录存在
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 将 /uploads/** 映射到文件系统的上传目录
        String absolutePath = uploadDir.getAbsolutePath();
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + absolutePath + File.separator);
    }
}

