package org.sc.smartcommunitybackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sc.smartcommunitybackend.common.Result;
import org.sc.smartcommunitybackend.dto.request.SendMessageRequest;
import org.sc.smartcommunitybackend.dto.response.MessageResponse;
import org.sc.smartcommunitybackend.service.PrivateMessageService;
import org.sc.smartcommunitybackend.util.UserContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 私信Controller
 */
@RestController
@RequestMapping("/message")
@Tag(name = "私信管理", description = "私信相关接口")
public class PrivateMessageController extends BaseController {
    
    @Autowired
    private PrivateMessageService privateMessageService;
    
    @PostMapping("/send")
    @Operation(summary = "发送私信", description = "向其他用户发送私信")
    public Result<Long> sendMessage(@RequestBody @Valid SendMessageRequest request) {
        Long userId = UserContextUtil.getCurrentUserId();
        Long messageId = privateMessageService.sendMessage(userId, request);
        return success("发送成功", messageId);
    }
    
    @GetMapping("/chat/{otherUserId}")
    @Operation(summary = "获取聊天记录", description = "获取与某人的聊天记录")
    public Result<Page<MessageResponse>> getChatHistory(
            @Parameter(description = "对方用户ID") @PathVariable Long otherUserId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") Integer pageSize) {
        Long userId = UserContextUtil.getCurrentUserId();
        Page<MessageResponse> page = privateMessageService.getChatHistory(userId, otherUserId, pageNum, pageSize);
        return success(page);
    }
    
    @GetMapping("/conversations")
    @Operation(summary = "获取会话列表", description = "获取最近联系人列表")
    public Result<Page<MessageResponse>> getConversations(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") Integer pageSize) {
        Long userId = UserContextUtil.getCurrentUserId();
        Page<MessageResponse> page = privateMessageService.getConversations(userId, pageNum, pageSize);
        return success(page);
    }
    
    @PostMapping("/{messageId}/read")
    @Operation(summary = "标记已读", description = "标记消息为已读")
    public Result<Void> markAsRead(@Parameter(description = "消息ID") @PathVariable Long messageId) {
        Long userId = UserContextUtil.getCurrentUserId();
        privateMessageService.markAsRead(userId, messageId);
        return success("已标记为已读", null);
    }
    
    @PostMapping("/read-all/{otherUserId}")
    @Operation(summary = "全部已读", description = "标记与某人的所有消息为已读")
    public Result<Integer> markAllAsRead(@Parameter(description = "对方用户ID") @PathVariable Long otherUserId) {
        Long userId = UserContextUtil.getCurrentUserId();
        int count = privateMessageService.markAllAsRead(userId, otherUserId);
        return success("已标记" + count + "条消息为已读", count);
    }
    
    @GetMapping("/unread-count")
    @Operation(summary = "未读数量", description = "获取未读消息数量")
    public Result<Integer> getUnreadCount() {
        Long userId = UserContextUtil.getCurrentUserId();
        int count = privateMessageService.getUnreadCount(userId);
        return success(count);
    }
    
    @DeleteMapping("/{messageId}")
    @Operation(summary = "删除消息", description = "删除消息")
    public Result<Void> deleteMessage(@Parameter(description = "消息ID") @PathVariable Long messageId) {
        Long userId = UserContextUtil.getCurrentUserId();
        privateMessageService.deleteMessage(userId, messageId);
        return success("删除成功", null);
    }
}
