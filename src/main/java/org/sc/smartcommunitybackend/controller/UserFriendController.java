package org.sc.smartcommunitybackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sc.smartcommunitybackend.common.Result;
import org.sc.smartcommunitybackend.dto.request.AddFriendRequest;
import org.sc.smartcommunitybackend.dto.response.FriendResponse;
import org.sc.smartcommunitybackend.service.UserFriendService;
import org.sc.smartcommunitybackend.util.UserContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户好友Controller
 */
@RestController
@RequestMapping("/friend")
@Tag(name = "好友管理", description = "好友相关接口")
public class UserFriendController extends BaseController {
    
    @Autowired
    private UserFriendService userFriendService;
    
    @PostMapping("/request")
    @Operation(summary = "发送好友申请", description = "向其他用户发送好友申请")
    public Result<Void> sendFriendRequest(@RequestBody @Valid AddFriendRequest request) {
        Long userId = UserContextUtil.getCurrentUserId();
        userFriendService.sendFriendRequest(userId, request);
        return success("好友申请已发送", null);
    }
    
    @PostMapping("/{friendId}/accept")
    @Operation(summary = "接受好友申请", description = "接受好友申请")
    public Result<Void> acceptFriendRequest(@Parameter(description = "好友关系ID") @PathVariable Long friendId) {
        Long userId = UserContextUtil.getCurrentUserId();
        userFriendService.acceptFriendRequest(userId, friendId);
        return success("已同意好友申请", null);
    }
    
    @PostMapping("/{friendId}/reject")
    @Operation(summary = "拒绝好友申请", description = "拒绝好友申请")
    public Result<Void> rejectFriendRequest(@Parameter(description = "好友关系ID") @PathVariable Long friendId) {
        Long userId = UserContextUtil.getCurrentUserId();
        userFriendService.rejectFriendRequest(userId, friendId);
        return success("已拒绝好友申请", null);
    }
    
    @DeleteMapping("/{friendUserId}")
    @Operation(summary = "删除好友", description = "删除好友关系")
    public Result<Void> deleteFriend(@Parameter(description = "好友用户ID") @PathVariable Long friendUserId) {
        Long userId = UserContextUtil.getCurrentUserId();
        userFriendService.deleteFriend(userId, friendUserId);
        return success("已删除好友", null);
    }
    
    @GetMapping("/list")
    @Operation(summary = "获取好友列表", description = "获取我的好友列表")
    public Result<Page<FriendResponse>> getFriendList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") Integer pageSize) {
        Long userId = UserContextUtil.getCurrentUserId();
        Page<FriendResponse> page = userFriendService.getFriendList(userId, pageNum, pageSize);
        return success(page);
    }
    
    @GetMapping("/requests")
    @Operation(summary = "获取好友申请列表", description = "获取收到的好友申请列表")
    public Result<Page<FriendResponse>> getFriendRequests(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") Integer pageSize) {
        Long userId = UserContextUtil.getCurrentUserId();
        Page<FriendResponse> page = userFriendService.getFriendRequests(userId, pageNum, pageSize);
        return success(page);
    }
}
