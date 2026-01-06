package org.sc.smartcommunitybackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sc.smartcommunitybackend.common.Result;
import org.sc.smartcommunitybackend.dto.response.NotificationResponse;
import org.sc.smartcommunitybackend.service.UserNotificationService;
import org.sc.smartcommunitybackend.util.UserContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户通知Controller
 */
@RestController
@RequestMapping("/notification")
@Tag(name = "通知管理", description = "通知相关接口")
public class UserNotificationController extends BaseController {
    
    @Autowired
    private UserNotificationService userNotificationService;
    
    @GetMapping("/list")
    @Operation(summary = "获取通知列表", description = "获取我的通知列表")
    public Result<Page<NotificationResponse>> getNotifications(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") Integer pageSize) {
        Long userId = UserContextUtil.getCurrentUserId();
        Page<NotificationResponse> page = userNotificationService.getNotifications(userId, pageNum, pageSize);
        return success(page);
    }
    
    @PostMapping("/{notificationId}/read")
    @Operation(summary = "标记已读", description = "标记通知为已读")
    public Result<Void> markAsRead(@Parameter(description = "通知ID") @PathVariable Long notificationId) {
        Long userId = UserContextUtil.getCurrentUserId();
        userNotificationService.markAsRead(userId, notificationId);
        return success("已标记为已读", null);
    }
    
    @PostMapping("/read-all")
    @Operation(summary = "全部已读", description = "标记所有通知为已读")
    public Result<Integer> markAllAsRead() {
        Long userId = UserContextUtil.getCurrentUserId();
        int count = userNotificationService.markAllAsRead(userId);
        return success("已标记" + count + "条通知为已读", count);
    }
    
    @GetMapping("/unread-count")
    @Operation(summary = "未读数量", description = "获取未读通知数量")
    public Result<Integer> getUnreadCount() {
        Long userId = UserContextUtil.getCurrentUserId();
        int count = userNotificationService.getUnreadCount(userId);
        return success(count);
    }
    
    @DeleteMapping("/{notificationId}")
    @Operation(summary = "删除通知", description = "删除通知")
    public Result<Void> deleteNotification(@Parameter(description = "通知ID") @PathVariable Long notificationId) {
        Long userId = UserContextUtil.getCurrentUserId();
        userNotificationService.deleteNotification(userId, notificationId);
        return success("删除成功", null);
    }
}
