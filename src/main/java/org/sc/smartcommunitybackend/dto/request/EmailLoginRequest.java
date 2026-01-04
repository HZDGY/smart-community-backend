package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 邮箱验证码登录请求
 */
@Data
@Schema(description = "邮箱验证码登录请求")
public class EmailLoginRequest {

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱地址", example = "user@example.com", required = true)
    private String email;

    @NotBlank(message = "验证码不能为空")
    @Schema(description = "邮箱验证码（6位数字）", example = "123456", required = true)
    private String verifyCode;
}

