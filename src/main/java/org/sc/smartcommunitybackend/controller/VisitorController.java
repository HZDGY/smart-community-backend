package org.sc.smartcommunitybackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sc.smartcommunitybackend.common.Result;
import org.sc.smartcommunitybackend.dto.request.VisitorRegisterRequest;
import org.sc.smartcommunitybackend.dto.response.VisitorRegisterResponse;
import org.sc.smartcommunitybackend.service.VisitorRegisterService;
import org.sc.smartcommunitybackend.util.UserContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 访客登记控制器
 */
@RestController
@RequestMapping("/visitor")
@Tag(name = "访客登记管理", description = "访客登记相关接口")
public class VisitorController extends BaseController {

    @Autowired
    private VisitorRegisterService visitorRegisterService;

    @PostMapping("/register")
    @Operation(summary = "创建访客登记", description = "用户登记来访目的、放行时间及有效日期，供社区工作人员审核")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "登记成功"),
            @ApiResponse(responseCode = "400", description = "参数错误"),
            @ApiResponse(responseCode = "401", description = "未授权（token无效或过期）"),
            @ApiResponse(responseCode = "600", description = "业务异常")
    })
    public Result<VisitorRegisterResponse> createRegister(
            @Parameter(description = "访客登记请求", required = true)
            @RequestBody @Valid VisitorRegisterRequest request) {
        Long userId = UserContextUtil.getCurrentUserId();
        VisitorRegisterResponse response = visitorRegisterService.createRegister(userId, request);
        return success("登记成功", response);
    }

    @GetMapping("/list")
    @Operation(summary = "我的访客登记列表", description = "查询当前用户的访客登记列表")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "查询成功"),
            @ApiResponse(responseCode = "401", description = "未授权（token无效或过期）")
    })
    public Result<Page<VisitorRegisterResponse>> getMyRegisterList(
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量", example = "10") @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = UserContextUtil.getCurrentUserId();
        Page<VisitorRegisterResponse> page = visitorRegisterService.getMyRegisterList(userId, pageNum, pageSize);
        return success(page);
    }

    @GetMapping("/{registerId}")
    @Operation(summary = "访客登记详情", description = "获取访客登记详细信息")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "查询成功"),
            @ApiResponse(responseCode = "401", description = "未授权（token无效或过期）"),
            @ApiResponse(responseCode = "600", description = "业务异常（如登记不存在）")
    })
    public Result<VisitorRegisterResponse> getRegisterDetail(
            @Parameter(description = "登记ID", required = true) @PathVariable Long registerId) {
        Long userId = UserContextUtil.getCurrentUserId();
        VisitorRegisterResponse response = visitorRegisterService.getRegisterDetail(registerId, userId);
        return success(response);
    }

    @DeleteMapping("/{registerId}")
    @Operation(summary = "取消访客登记", description = "取消待审核的访客登记")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "取消成功"),
            @ApiResponse(responseCode = "401", description = "未授权（token无效或过期）"),
            @ApiResponse(responseCode = "600", description = "业务异常（如登记不存在或已审核）")
    })
    public Result<Void> cancelRegister(
            @Parameter(description = "登记ID", required = true) @PathVariable Long registerId) {
        Long userId = UserContextUtil.getCurrentUserId();
        visitorRegisterService.cancelRegister(registerId, userId);
        return success("取消成功", null);
    }
}

