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
import org.sc.smartcommunitybackend.dto.request.RepairReportRequest;
import org.sc.smartcommunitybackend.dto.response.RepairReportResponse;
import org.sc.smartcommunitybackend.service.RepairReportService;
import org.sc.smartcommunitybackend.util.UserContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 报事维修控制器
 * 
 * @author 智慧社区
 * @description 提供报事维修相关功能，包括提交维修申请、查询维修列表和详情
 */
@RestController
@RequestMapping("/api/repair")
@Tag(name = "物业管理-报事维修", description = "用户可选择事项类型，描述问题，提交维修申请至社区管理人员。支持查询维修进度和处理结果。")
public class RepairReportController extends BaseController {

    @Autowired
    private RepairReportService repairReportService;

    @PostMapping("/submit")
    @Operation(
        summary = "提交报事维修", 
        description = "用户提交报事维修申请。需要选择事项类型（如水电维修、电梯故障等）并详细描述问题。提交后状态为待处理，社区管理人员会及时处理。"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", 
            description = "提交成功",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Result.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "code": 200,
                      "message": "提交成功",
                      "data": {
                        "reportId": 1,
                        "userId": 1,
                        "reportType": "水电维修",
                        "description": "厨房水龙头漏水，需要维修",
                        "status": 0,
                        "statusText": "待处理",
                        "createTime": "2026-01-04 10:30:00",
                        "handleTime": null,
                        "handleUserId": null,
                        "handleResult": null
                      }
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "请求参数错误"),
        @ApiResponse(responseCode = "401", description = "未授权，token无效或过期")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "报事维修请求参数",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = RepairReportRequest.class),
            examples = {
                @ExampleObject(
                    name = "水电维修",
                    value = """
                    {
                      "reportType": "水电维修",
                      "description": "厨房水龙头漏水，需要尽快维修"
                    }
                    """
                ),
                @ExampleObject(
                    name = "电梯故障",
                    value = """
                    {
                      "reportType": "电梯故障",
                      "description": "3号楼电梯按钮失灵，无法正常使用"
                    }
                    """
                ),
                @ExampleObject(
                    name = "门窗维修",
                    value = """
                    {
                      "reportType": "门窗维修",
                      "description": "卧室窗户关不严，漏风严重"
                    }
                    """
                )
            }
        )
    )
    public Result<RepairReportResponse> submitRepairReport(@Valid @RequestBody RepairReportRequest request) {
        Long userId = UserContextUtil.getCurrentUserId();
        RepairReportResponse response = repairReportService.submitRepairReport(userId, request);
        return success("提交成功", response);
    }

    @GetMapping("/my-list")
    @Operation(
        summary = "查询我的报事维修列表", 
        description = "查询当前用户提交的所有报事维修记录。支持按处理状态筛选（待处理、处理中、已完成、已驳回），支持分页查询。结果按创建时间倒序排列，最新的记录在前面。"
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
                            "reportId": 1,
                            "userId": 1,
                            "reportType": "水电维修",
                            "description": "厨房水龙头漏水，需要维修",
                            "status": 0,
                            "statusText": "待处理",
                            "createTime": "2026-01-04 09:00:00",
                            "handleTime": null,
                            "handleUserId": null,
                            "handleResult": null
                          },
                          {
                            "reportId": 2,
                            "userId": 1,
                            "reportType": "门窗维修",
                            "description": "卧室窗户关不严，漏风严重",
                            "status": 2,
                            "statusText": "已完成",
                            "createTime": "2026-01-02 10:15:00",
                            "handleTime": "2026-01-02 16:30:00",
                            "handleUserId": 1,
                            "handleResult": "已安排维修人员上门处理，问题已解决"
                          }
                        ],
                        "total": 10,
                        "size": 10,
                        "current": 1,
                        "pages": 1
                      }
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "未授权，token无效或过期")
    })
    public Result<Page<RepairReportResponse>> getMyRepairReportList(
            @Parameter(
                description = "处理状态筛选（可选）",
                example = "0",
                schema = @Schema(
                    type = "integer",
                    allowableValues = {"0", "1", "2", "3"},
                    description = "0-待处理，1-处理中，2-已完成，3-已驳回。不传则查询所有状态"
                )
            )
            @RequestParam(required = false) Integer status,
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
        Long userId = UserContextUtil.getCurrentUserId();
        Page<RepairReportResponse> page = repairReportService.getUserRepairReportList(userId, status, pageNum, pageSize);
        return success(page);
    }

    @GetMapping("/{reportId}")
    @Operation(
        summary = "查询报事维修详情", 
        description = "查询指定报事维修的详细信息，包括处理状态、处理时间、处理人员、处理结果等完整信息。只能查询自己提交的维修记录。"
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
                        "reportId": 1,
                        "userId": 1,
                        "reportType": "水电维修",
                        "description": "厨房水龙头漏水，需要维修",
                        "status": 2,
                        "statusText": "已完成",
                        "createTime": "2026-01-02 10:15:00",
                        "handleTime": "2026-01-02 16:30:00",
                        "handleUserId": 1,
                        "handleResult": "已安排维修人员上门处理，问题已解决"
                      }
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "未授权，token无效或过期"),
        @ApiResponse(responseCode = "404", description = "报事维修记录不存在或无权访问")
    })
    public Result<RepairReportResponse> getRepairReportDetail(
            @Parameter(
                description = "报事维修ID",
                example = "1",
                required = true
            )
            @PathVariable Long reportId) {
        Long userId = UserContextUtil.getCurrentUserId();
        RepairReportResponse response = repairReportService.getRepairReportDetail(userId, reportId);
        return success(response);
    }
}

