package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 更新个人资料请求DTO
 */
@Data
@Schema(description = "更新个人资料请求")
public class UpdateProfileRequest {

    /**
     * 用户名
     */
    @Schema(description = "用户名", example = "张三")
    private String userName;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱", example = "zhangsan@example.com")
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 性别 0-未知 1-男 2-女
     */
    @Schema(description = "性别(0-未知,1-男,2-女)", example = "1")
    @Min(value = 0, message = "性别值必须在0-2之间")
    @Max(value = 2, message = "性别值必须在0-2之间")
    private Integer gender;

    /**
     * 年龄
     */
    @Schema(description = "年龄", example = "25")
    @Min(value = 1, message = "年龄必须大于0")
    @Max(value = 150, message = "年龄不能超过150")
    private Integer age;
}

