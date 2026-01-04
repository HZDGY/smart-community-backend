package org.sc.smartcommunitybackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 公告响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "公告响应")
public class AnnouncementResponse {

    @Schema(description = "公告ID", example = "1")
    private Long announceId;

    @Schema(description = "公告标题", example = "关于小区停水的通知")
    private String title;

    @Schema(description = "公告内容", example = "因水管维修，明天上午8:00-12:00停水...")
    private String content;

    @Schema(description = "发布人ID", example = "100")
    private Long publishUserId;

    @Schema(description = "发布人姓名", example = "张三")
    private String publishUserName;

    @Schema(description = "发布时间", example = "2026-01-04 10:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date publishTime;

    @Schema(description = "阅读次数", example = "128")
    private Integer readCount;
}

