package org.sc.smartcommunitybackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 访客登记响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "访客登记响应")
public class VisitorRegisterResponse {

    @Schema(description = "登记ID", example = "1")
    private Long registerId;

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "用户姓名", example = "李四")
    private String userName;

    @Schema(description = "访客姓名", example = "张三")
    private String visitorName;

    @Schema(description = "访客电话", example = "13800138000")
    private String visitorPhone;

    @Schema(description = "来访目的", example = "探访亲友")
    private String visitPurpose;

    @Schema(description = "放行时间", example = "2026-01-05 10:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date allowTime;

    @Schema(description = "有效日期", example = "2026-01-05 23:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validDate;

    @Schema(description = "审核状态: 0-待审核 1-已通过 2-已拒绝", example = "0")
    private Integer status;

    @Schema(description = "审核状态描述", example = "待审核")
    private String statusDesc;

    @Schema(description = "创建时间", example = "2026-01-04 10:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Schema(description = "审核时间", example = "2026-01-04 11:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date auditTime;

    @Schema(description = "审核人姓名", example = "王五")
    private String auditUserName;

    @Schema(description = "拒绝原因", example = "信息不完整")
    private String rejectReason;
}

