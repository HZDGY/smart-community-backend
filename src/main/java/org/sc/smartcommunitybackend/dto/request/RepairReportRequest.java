package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 报事维修请求DTO
 */
@Data
@Schema(description = "报事维修请求参数", example = """
    {
      "reportType": "水电维修",
      "description": "厨房水龙头漏水，需要尽快维修"
    }
    """)
public class RepairReportRequest {
    
    @Schema(
        description = "事项类型", 
        example = "水电维修",
        requiredMode = Schema.RequiredMode.REQUIRED,
        allowableValues = {"水电维修", "电梯故障", "门窗维修", "网络故障", "供暖问题", "环境卫生", "其他"}
    )
    @NotBlank(message = "事项类型不能为空")
    @Size(max = 50, message = "事项类型长度不能超过50个字符")
    private String reportType;
    
    @Schema(
        description = "事项描述，请详细描述问题的具体情况", 
        example = "厨房水龙头漏水，需要维修",
        requiredMode = Schema.RequiredMode.REQUIRED,
        minLength = 5,
        maxLength = 500
    )
    @NotBlank(message = "事项描述不能为空")
    @Size(min = 5, max = 500, message = "事项描述长度应在5-500个字符之间")
    private String description;
}

