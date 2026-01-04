package org.sc.smartcommunitybackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 事项投诉响应DTO
 */
@Data
@Schema(description = "事项投诉响应数据", example = """
    {
      "complaintId": 1,
      "userId": 1,
      "complaintType": "噪音扰民",
      "description": "楼上住户深夜装修，噪音严重影响休息",
      "status": 2,
      "statusText": "已完成",
      "createTime": "2026-01-02 11:00:00",
      "handleTime": "2026-01-02 14:00:00",
      "handleUserId": 1,
      "handleResult": "已加强噪音管理，并与楼上住户沟通，问题已解决"
    }
    """)
public class ComplaintResponse {
    
    @Schema(description = "投诉ID，唯一标识", example = "1")
    private Long complaintId;
    
    @Schema(description = "提交用户ID", example = "1")
    private Long userId;
    
    @Schema(description = "投诉类型", example = "噪音扰民")
    private String complaintType;
    
    @Schema(description = "投诉描述", example = "楼上住户深夜装修，噪音严重影响休息")
    private String description;
    
    @Schema(
        description = "处理状态", 
        example = "2",
        allowableValues = {"0", "1", "2", "3"},
        implementation = Integer.class
    )
    private Integer status;
    
    @Schema(description = "状态描述文本", example = "已完成")
    private String statusText;
    
    @Schema(description = "提交时间", example = "2026-01-02 11:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    
    @Schema(description = "处理时间，未处理时为null", example = "2026-01-02 14:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date handleTime;
    
    @Schema(description = "处理人员ID，未处理时为null", example = "1")
    private Long handleUserId;
    
    @Schema(description = "处理结果说明，未处理时为null", example = "已加强噪音管理，并与楼上住户沟通，问题已解决")
    private String handleResult;
}

