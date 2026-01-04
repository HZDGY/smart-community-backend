package org.sc.smartcommunitybackend.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Date;

/**
 * 访客登记请求
 */
@Data
@Schema(description = "访客登记请求")
public class VisitorRegisterRequest {

    @Schema(description = "访客姓名", example = "张三", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "访客姓名不能为空")
    private String visitorName;

    @Schema(description = "访客电话", example = "13800138000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "访客电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String visitorPhone;

    @Schema(description = "来访目的", example = "探访亲友", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "来访目的不能为空")
    private String visitPurpose;

    @Schema(description = "放行时间", example = "2026-01-05 10:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "放行时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date allowTime;

    @Schema(description = "有效日期", example = "2026-01-05 23:59:59", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "有效日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validDate;
}

