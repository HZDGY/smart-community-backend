package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 车位登记/更新请求
 */
@Data
@Schema(description = "车位登记/更新请求")
public class ParkingSpaceRequest {

    @Schema(description = "车位编号", example = "A-101", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "车位编号不能为空")
    private String spaceNo;

    @Schema(description = "绑定车牌号", example = "京A12345", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "车牌号不能为空")
    @Pattern(regexp = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-HJ-NP-Z0-9]{4,5}[A-HJ-NP-Z0-9挂学警港澳]$", 
             message = "车牌号格式不正确")
    private String carNumber;
}

