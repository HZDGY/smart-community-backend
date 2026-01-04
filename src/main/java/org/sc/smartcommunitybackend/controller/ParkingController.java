package org.sc.smartcommunitybackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sc.smartcommunitybackend.common.Result;
import org.sc.smartcommunitybackend.dto.request.ParkingSpaceRequest;
import org.sc.smartcommunitybackend.dto.response.ParkingSpaceResponse;
import org.sc.smartcommunitybackend.service.ParkingSpaceService;
import org.sc.smartcommunitybackend.util.UserContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 车位管理控制器
 */
@RestController
@RequestMapping("/api/parking")
@Tag(name = "安保管理-车位管理", description = "车位登记和管理相关接口，支持车位登记、查询、更新和删除")
public class ParkingController extends BaseController {

    @Autowired
    private ParkingSpaceService parkingSpaceService;

    @PostMapping("/register")
    @Operation(
        summary = "提交车位登记申请", 
        description = "用户提交车位登记申请，填写车位编号和车牌号。提交后状态为待审核，需要社区管理人员审核通过后才能生效。"
    )
    @ApiResponses({
            @ApiResponse(
                responseCode = "200", 
                description = "提交成功",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                        value = """
                        {
                          "code": 200,
                          "message": "提交车位登记申请成功，请等待审核",
                          "data": {
                            "spaceId": 1,
                            "userId": 1,
                            "userName": "张三",
                            "spaceNo": "A-101",
                            "carNumber": "京A12345",
                            "status": 0,
                            "statusText": "待审核",
                            "createTime": "2026-01-04 10:00:00",
                            "updateTime": "2026-01-04 10:00:00",
                            "auditTime": null,
                            "auditUserId": null,
                            "rejectReason": null
                          }
                        }
                        """
                    )
                )
            ),
            @ApiResponse(responseCode = "400", description = "参数错误"),
            @ApiResponse(responseCode = "401", description = "未授权（token无效或过期）"),
            @ApiResponse(responseCode = "600", description = "业务异常（如车位编号已存在或车牌号已被绑定）")
    })
    public Result<ParkingSpaceResponse> registerParkingSpace(
            @Parameter(description = "车位登记请求", required = true)
            @RequestBody @Valid ParkingSpaceRequest request) {
        Long userId = UserContextUtil.getCurrentUserId();
        ParkingSpaceResponse response = parkingSpaceService.registerParkingSpace(userId, request);
        return success("提交车位登记申请成功，请等待审核", response);
    }

    @GetMapping("/list")
    @Operation(
        summary = "我的车位列表", 
        description = "查询当前用户的所有车位申请记录，包括待审核、已通过和已拒绝的记录。"
    )
    @ApiResponses({
            @ApiResponse(
                responseCode = "200", 
                description = "查询成功",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                        value = """
                        {
                          "code": 200,
                          "message": "成功",
                          "data": [
                            {
                              "spaceId": 1,
                              "userId": 1,
                              "userName": "张三",
                              "spaceNo": "A-101",
                              "carNumber": "京A12345",
                              "status": 1,
                              "statusText": "已通过",
                              "createTime": "2026-01-04 10:00:00",
                              "updateTime": "2026-01-04 10:00:00",
                              "auditTime": "2026-01-04 10:30:00",
                              "auditUserId": 1,
                              "rejectReason": null
                            },
                            {
                              "spaceId": 2,
                              "userId": 1,
                              "userName": "张三",
                              "spaceNo": "A-102",
                              "carNumber": "京B67890",
                              "status": 0,
                              "statusText": "待审核",
                              "createTime": "2026-01-04 11:00:00",
                              "updateTime": "2026-01-04 11:00:00",
                              "auditTime": null,
                              "auditUserId": null,
                              "rejectReason": null
                            }
                          ]
                        }
                        """
                    )
                )
            ),
            @ApiResponse(responseCode = "401", description = "未授权（token无效或过期）")
    })
    public Result<List<ParkingSpaceResponse>> getMyParkingSpaces() {
        Long userId = UserContextUtil.getCurrentUserId();
        List<ParkingSpaceResponse> list = parkingSpaceService.getMyParkingSpaces(userId);
        return success(list);
    }

    @GetMapping("/all")
    @Operation(
        summary = "查询所有车位列表", 
        description = "查询系统中所有车位信息，支持按车位编号和车牌号模糊搜索，支持分页查询。结果按创建时间倒序排列。"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", 
            description = "查询成功",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                    {
                      "code": 200,
                      "message": "成功",
                      "data": {
                        "records": [
                          {
                            "spaceId": 1,
                            "userId": 1,
                            "userName": "张三",
                            "spaceNo": "A-101",
                            "carNumber": "京A12345",
                            "createTime": "2026-01-04 10:00:00",
                            "updateTime": "2026-01-04 10:00:00"
                          },
                          {
                            "spaceId": 2,
                            "userId": 1,
                            "userName": "张三",
                            "spaceNo": "A-102",
                            "carNumber": "京B67890",
                            "createTime": "2026-01-03 15:30:00",
                            "updateTime": "2026-01-03 15:30:00"
                          }
                        ],
                        "total": 20,
                        "size": 10,
                        "current": 1,
                        "pages": 2
                      }
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "未授权，token无效或过期")
    })
    public Result<Page<ParkingSpaceResponse>> getAllParkingSpaces(
            @Parameter(
                description = "车位编号（可选，支持模糊查询）",
                example = "A-101",
                schema = @Schema(type = "string")
            )
            @RequestParam(required = false) String spaceNo,
            @Parameter(
                description = "车牌号（可选，支持模糊查询）",
                example = "京A12345",
                schema = @Schema(type = "string")
            )
            @RequestParam(required = false) String carNumber,
            @Parameter(
                description = "页码，从1开始",
                example = "1",
                required = true
            )
            @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(
                description = "每页记录数",
                example = "10",
                required = true
            )
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<ParkingSpaceResponse> page = parkingSpaceService.getAllParkingSpaces(spaceNo, carNumber, pageNum, pageSize);
        return success(page);
    }

    @GetMapping("/{spaceId}")
    @Operation(summary = "车位详情", description = "获取车位详细信息")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "查询成功"),
            @ApiResponse(responseCode = "401", description = "未授权（token无效或过期）"),
            @ApiResponse(responseCode = "600", description = "业务异常（如车位不存在）")
    })
    public Result<ParkingSpaceResponse> getParkingSpaceDetail(
            @Parameter(description = "车位ID", required = true) @PathVariable Long spaceId) {
        Long userId = UserContextUtil.getCurrentUserId();
        ParkingSpaceResponse response = parkingSpaceService.getParkingSpaceDetail(spaceId, userId);
        return success(response);
    }

    @PutMapping("/{spaceId}")
    @Operation(summary = "更新车位信息", description = "更新车位绑定的车牌号")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "更新成功"),
            @ApiResponse(responseCode = "400", description = "参数错误"),
            @ApiResponse(responseCode = "401", description = "未授权（token无效或过期）"),
            @ApiResponse(responseCode = "600", description = "业务异常（如车位不存在或车牌号已被绑定）")
    })
    public Result<ParkingSpaceResponse> updateParkingSpace(
            @Parameter(description = "车位ID", required = true) @PathVariable Long spaceId,
            @Parameter(description = "车位更新请求", required = true)
            @RequestBody @Valid ParkingSpaceRequest request) {
        Long userId = UserContextUtil.getCurrentUserId();
        ParkingSpaceResponse response = parkingSpaceService.updateParkingSpace(spaceId, userId, request);
        return success("更新成功", response);
    }

    @DeleteMapping("/{spaceId}")
    @Operation(summary = "删除车位", description = "删除车位信息")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "删除成功"),
            @ApiResponse(responseCode = "401", description = "未授权（token无效或过期）"),
            @ApiResponse(responseCode = "600", description = "业务异常（如车位不存在）")
    })
    public Result<Void> deleteParkingSpace(
            @Parameter(description = "车位ID", required = true) @PathVariable Long spaceId) {
        Long userId = UserContextUtil.getCurrentUserId();
        parkingSpaceService.deleteParkingSpace(spaceId, userId);
        return success("删除成功", null);
    }
}

