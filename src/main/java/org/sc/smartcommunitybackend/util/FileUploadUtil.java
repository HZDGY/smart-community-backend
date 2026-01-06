package org.sc.smartcommunitybackend.util;

import org.sc.smartcommunitybackend.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 文件上传工具类
 */
@Component
public class FileUploadUtil {

    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${file.upload.allowed-extensions}")
    private String allowedExtensions;

    @Value("${server.port:8080}")
    private String serverPort;
    
    @Value("${server.servlet.context-path:}")
    private String contextPath;

    /**
     * 上传文件
     *
     * @param file 文件对象
     * @param subDir 子目录（如：avatar, document等）
     * @return 文件访问URL
     */
    public String uploadFile(MultipartFile file, String subDir) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }

        // 验证文件扩展名
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        if (!isAllowedExtension(extension)) {
            throw new BusinessException("不支持的文件格式，仅支持：" + allowedExtensions);
        }

        // 验证文件大小（已在配置文件中限制，这里可以添加额外校验）
        long maxSize = 10 * 1024 * 1024; // 10MB
        if (file.getSize() > maxSize) {
            throw new BusinessException("文件大小不能超过10MB");
        }

        try {
            // 构建保存路径：uploads/subDir/yyyyMMdd/
            String datePath = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String fullPath = uploadPath + subDir + File.separator + datePath;
            
            // 确保目录存在
            File directory = new File(fullPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // 生成唯一文件名
            String newFilename = UUID.randomUUID().toString().replace("-", "") + "." + extension;
            String filePath = fullPath + File.separator + newFilename;

            // 保存文件
            Path path = Paths.get(filePath);
            Files.write(path, file.getBytes());

            // 返回访问URL
            // 格式：http://localhost:8080/api/uploads/avatar/yyyyMMdd/filename.jpg
            String fileUrl = "http://localhost:" + serverPort + contextPath + "/uploads/" + 
                            subDir + "/" + datePath + "/" + newFilename;
            
            return fileUrl;

        } catch (IOException e) {
            throw new BusinessException("文件上传失败：" + e.getMessage());
        }
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * 检查文件扩展名是否允许
     */
    private boolean isAllowedExtension(String extension) {
        if (allowedExtensions == null || allowedExtensions.isEmpty()) {
            return true;
        }
        List<String> allowedList = Arrays.asList(allowedExtensions.split(","));
        return allowedList.contains(extension.toLowerCase());
    }

    /**
     * 删除文件
     *
     * @param fileUrl 文件URL
     * @return 是否删除成功
     */
    public boolean deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return false;
        }

        try {
            // 从URL中提取文件路径
            // http://localhost:8080/uploads/avatar/yyyyMMdd/filename.jpg
            // -> uploads/avatar/yyyyMMdd/filename.jpg
            String pathPart = fileUrl.substring(fileUrl.indexOf("/uploads/") + 1);
            File file = new File(pathPart);
            
            if (file.exists()) {
                return file.delete();
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}

