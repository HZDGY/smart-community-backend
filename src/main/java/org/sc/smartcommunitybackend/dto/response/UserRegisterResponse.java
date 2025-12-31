package org.sc.smartcommunitybackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 用户注册响应DTO
 */
@Data
@Schema(description = "用户注册响应")
public class UserRegisterResponse extends BaseResponse {

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "用户名", example = "张三")
    private String userName;

    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    @Schema(description = "性别（0-未知 1-男 2-女）", example = "1")
    private Integer gender;

    @Schema(description = "年龄", example = "25")
    private Integer age;

    @Schema(description = "邮箱", example = "zhangsan@example.com")
    private String email;

    @Schema(description = "用户类型（1-普通用户 2-商户管理员 3-社区管理员）", example = "1")
    private Integer userType;

    @Schema(description = "状态（0-冻结 1-正常）", example = "1")
    private Integer status;

    @Schema(description = "注册时间", example = "2024-01-01 12:00:00")
    private Date createTime;
}

