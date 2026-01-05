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
import org.sc.smartcommunitybackend.dto.request.ComplaintRequest;
import org.sc.smartcommunitybackend.dto.response.ComplaintResponse;
import org.sc.smartcommunitybackend.service.ComplaintService;
import org.sc.smartcommunitybackend.util.UserContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 事项投诉控制器
 * 
 * @author 智慧社区
 * @description 提供事项投诉相关功能，包括提交投诉、查询投诉列表和详情
 */
@RestController
@RequestMapping("/complaint")
@Tag(name = "物业管理-事项投诉", description = "用户可编辑投诉事项类型和事项描述，提交至社区管理人员。支持查询投诉进度和处理结果。")
public class ComplaintController extends BaseController {

    @Autowired
    private ComplaintService complaintService;

    @PostMapping("/submit")
    @Operation(
        summary = "提交事项投诉", 
        description = "用户提交事项投诉。需要选择投诉类型（如噪音扰民、违规停车等）并详细描述投诉内容。提交后状态为待处理，社区管理人员会及时处理。"
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
                        "complaintId": 1,
                        "userId": 1,
                        "complaintType": "噪音扰民",
                        "description": "楼上住户深夜装修，噪音严重影响休息",
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
        description = "事项投诉请求参数",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ComplaintRequest.class),
            examples = {
                @ExampleObject(
                    name = "噪音扰民",
                    value = """
                    {
                      "complaintType": "噪音扰民",
                      "description": "楼上住户深夜装修，噪音严重影响休息"
                    }
                    """
                ),
                @ExampleObject(
                    name = "违规停车",
                    value = """
                    {
                      "complaintType": "违规停车",
                      "description": "有车辆长期占用消防通道，存在安全隐患"
                    }
                    """
                ),
                @ExampleObject(
                    name = "垃圾处理",
                    value = """
                    {
                      "complaintType": "垃圾处理",
                      "description": "垃圾分类不规范，垃圾桶周围环境脏乱"
                    }
                    """
                ),
                @ExampleObject(
                    name = "物业服务",
                    value = """
                    {
                      "complaintType": "物业服务",
                      "description": "物业费收取不合理，服务质量差"
                    }
                    """
                )
            }
        )
    )
    public Result<ComplaintResponse> submitComplaint(@Valid @RequestBody ComplaintRequest request) {
        Long userId = UserContextUtil.getCurrentUserId();
        ComplaintResponse response = complaintService.submitComplaint(userId, request);
        return success("提交成功", response);
    }

    @GetMapping("/my-list")
    @Operation(
        summary = "查询我的投诉列表", 
        description = "查询当前用户提交的所有投诉记录。支持按处理状态筛选（待处理、处理中、已完成、已驳回），支持分页查询。结果按创建时间倒序排列，最新的记录在前面。"
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
                            "complaintId": 1,
                            "userId": 1,
                            "complaintType": "噪音扰民",
                            "description": "楼上住户深夜装修，噪音严重影响休息",
                            "status": 0,
                            "statusText": "待处理",
                            "createTime": "2026-01-04 09:30:00",
                            "handleTime": null,
                            "handleUserId": null,
                            "handleResult": null
                          },
                          {
                            "complaintId": 2,
                            "userId": 1,
                            "complaintType": "垃圾处理",
                            "description": "垃圾分类不规范，垃圾桶周围环境脏乱",
                            "status": 2,
                            "statusText": "已完成",
                            "createTime": "2026-01-02 11:00:00",
                            "handleTime": "2026-01-02 14:00:00",
                            "handleUserId": 1,
                            "handleResult": "已加强垃圾分类宣传，并增加清洁频次"
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
    public Result<Page<ComplaintResponse>> getMyComplaintList(
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
        Page<ComplaintResponse> page = complaintService.getUserComplaintList(userId, status, pageNum, pageSize);
        return success(page);
    }

    @GetMapping("/{complaintId}")
    @Operation(
        summary = "查询投诉详情", 
        description = "查询指定投诉的详细信息，包括处理状态、处理时间、处理人员、处理结果等完整信息。只能查询自己提交的投诉记录。"
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
                        "complaintId": 1,
                        "userId": 1,
                        "complaintType": "噪音扰民",
                        "description": "楼上住户深夜装修，噪音严重影响休息",
                        "status": 2,
                        "statusText": "已完成",
                        "createTime": "2026-01-02 11:00:00",
                        "handleTime": "2026-01-02 14:00:00",
                        "handleUserId": 1,
                        "handleResult": "已加强噪音管理，并与楼上住户沟通，问题已解决"
                      }
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "未授权，token无效或过期"),
        @ApiResponse(responseCode = "404", description = "投诉记录不存在或无权访问")
    })
    public Result<ComplaintResponse> getComplaintDetail(
            @Parameter(
                description = "投诉ID",
                example = "1",
                required = true
            )
            @PathVariable Long complaintId) {
        Long userId = UserContextUtil.getCurrentUserId();
        ComplaintResponse response = complaintService.getComplaintDetail(userId, complaintId);
        return success(response);
    }
}

