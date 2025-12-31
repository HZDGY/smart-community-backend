package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 忘记密码请求DTO
 */
@Data
@Schema(description = "忘记密码请求")
public class ForgotPasswordRequest {

    /**
     * 手机号
     */
    @Schema(description = "手机号", example = "13800138000")
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 验证码（实际项目中需要短信验证码，这里简化处理，可以使用默认验证码或其他验证方式）
     */
    @Schema(description = "验证码", example = "123456")
    @NotBlank(message = "验证码不能为空")
    private String verifyCode;

    /**
     * 新密码
     */
    @Schema(description = "新密码", example = "123456")
    @NotBlank(message = "新密码不能为空")
    @Pattern(regexp = "^.{6,20}$", message = "密码长度必须在6-20位之间")
    private String newPassword;

    /**
     * 确认新密码
     */
    @Schema(description = "确认新密码", example = "123456")
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;
}

