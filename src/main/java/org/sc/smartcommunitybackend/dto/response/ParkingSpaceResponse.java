package org.sc.smartcommunitybackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 车位信息响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "车位信息响应", example = """
    {
      "spaceId": 1,
      "userId": 1,
      "userName": "张三",
      "spaceNo": "A-101",
      "carNumber": "京A12345",
      "status": 1,
      "statusText": "已通过",
      "createTime": "2026-01-04 10:00:00",
      "updateTime": "2026-01-04 11:00:00",
      "auditTime": "2026-01-04 10:30:00",
      "auditUserId": 1,
      "rejectReason": null
    }
    """)
public class ParkingSpaceResponse {

    @Schema(description = "车位ID", example = "1")
    private Long spaceId;

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "用户姓名", example = "张三")
    private String userName;

    @Schema(description = "车位编号", example = "A-101")
    private String spaceNo;

    @Schema(description = "绑定车牌号", example = "京A12345")
    private String carNumber;

    @Schema(
        description = "审核状态", 
        example = "1",
        allowableValues = {"0", "1", "2"},
        implementation = Integer.class
    )
    private Integer status;

    @Schema(description = "状态描述文本", example = "已通过")
    private String statusText;

    @Schema(description = "创建时间", example = "2026-01-04 10:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Schema(description = "更新时间", example = "2026-01-04 11:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @Schema(description = "审核时间，未审核时为null", example = "2026-01-04 10:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date auditTime;

    @Schema(description = "审核人ID，未审核时为null", example = "1")
    private Long auditUserId;

    @Schema(description = "拒绝原因，未拒绝时为null")
    private String rejectReason;
}

