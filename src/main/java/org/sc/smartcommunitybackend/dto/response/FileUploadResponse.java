package org.sc.smartcommunitybackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件上传响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "文件上传响应")
public class FileUploadResponse {

    /**
     * 文件访问URL
     */
    @Schema(description = "文件访问URL")
    private String url;

    /**
     * 原始文件名
     */
    @Schema(description = "原始文件名")
    private String originalFilename;

    /**
     * 文件大小（字节）
     */
    @Schema(description = "文件大小（字节）")
    private Long size;

    /**
     * 文件类型
     */
    @Schema(description = "文件类型")
    private String contentType;
}

