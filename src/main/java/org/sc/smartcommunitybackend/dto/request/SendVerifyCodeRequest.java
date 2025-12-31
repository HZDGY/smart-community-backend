package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 发送验证码请求DTO
 */
@Data
@Schema(description = "发送验证码请求")
public class SendVerifyCodeRequest {

    /**
     * 邮箱地址
     */
    @Schema(description = "邮箱地址", example = "user@example.com")
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
}

