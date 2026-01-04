package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 事项投诉请求DTO
 */
@Data
@Schema(description = "事项投诉请求参数", example = """
    {
      "complaintType": "噪音扰民",
      "description": "楼上住户深夜装修，噪音严重影响休息"
    }
    """)
public class ComplaintRequest {
    
    @Schema(
        description = "投诉类型", 
        example = "噪音扰民",
        requiredMode = Schema.RequiredMode.REQUIRED,
        allowableValues = {"噪音扰民", "违规停车", "垃圾处理", "物业服务", "安全问题", "设施损坏", "其他"}
    )
    @NotBlank(message = "投诉类型不能为空")
    @Size(max = 50, message = "投诉类型长度不能超过50个字符")
    private String complaintType;
    
    @Schema(
        description = "投诉描述，请详细描述投诉内容和诉求", 
        example = "楼上住户深夜装修，噪音严重影响休息",
        requiredMode = Schema.RequiredMode.REQUIRED,
        minLength = 5,
        maxLength = 500
    )
    @NotBlank(message = "投诉描述不能为空")
    @Size(min = 5, max = 500, message = "投诉描述长度应在5-500个字符之间")
    private String description;
}

