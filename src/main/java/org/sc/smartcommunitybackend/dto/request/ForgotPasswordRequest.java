package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
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
     * 邮箱地址
     */
    @Schema(description = "邮箱地址", example = "user@example.com")
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 邮箱验证码
     */
    @Schema(description = "邮箱验证码", example = "123456")
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

