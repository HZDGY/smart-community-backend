package org.sc.smartcommunitybackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 报事维修响应DTO
 */
@Data
@Schema(description = "报事维修响应数据", example = """
    {
      "reportId": 1,
      "userId": 1,
      "reportType": "水电维修",
      "description": "厨房水龙头漏水，需要维修",
      "status": 2,
      "statusText": "已完成",
      "createTime": "2026-01-02 10:15:00",
      "handleTime": "2026-01-02 16:30:00",
      "handleUserId": 1,
      "handleResult": "已安排维修人员上门处理，问题已解决"
    }
    """)
public class RepairReportResponse {
    
    @Schema(description = "报事ID，唯一标识", example = "1")
    private Long reportId;
    
    @Schema(description = "提交用户ID", example = "1")
    private Long userId;
    
    @Schema(description = "事项类型", example = "水电维修")
    private String reportType;
    
    @Schema(description = "事项描述", example = "厨房水龙头漏水，需要维修")
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
    
    @Schema(description = "提交时间", example = "2026-01-02 10:15:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    
    @Schema(description = "处理时间，未处理时为null", example = "2026-01-02 16:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date handleTime;
    
    @Schema(description = "处理人员ID，未处理时为null", example = "1")
    private Long handleUserId;
    
    @Schema(description = "处理结果说明，未处理时为null", example = "已安排维修人员上门处理，问题已解决")
    private String handleResult;
}

