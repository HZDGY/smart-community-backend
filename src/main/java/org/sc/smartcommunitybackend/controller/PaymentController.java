package org.sc.smartcommunitybackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sc.smartcommunitybackend.common.Result;
import org.sc.smartcommunitybackend.domain.PaymentOrder;
import org.sc.smartcommunitybackend.dto.response.PaymentResponse;
import org.sc.smartcommunitybackend.service.PaymentService;
import org.sc.smartcommunitybackend.util.UserContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 支付Controller
 */
@RestController
@RequestMapping("/payment")
@Tag(name = "支付管理", description = "支付相关接口（模拟支付）")
public class PaymentController extends BaseController {
    
    @Autowired
    private PaymentService paymentService;
    
    @PostMapping("/initiate/{orderNo}")
    @Operation(summary = "发起支付", description = "发起支付（返回支付URL）")
    public Result<PaymentResponse> initiatePayment(
            @Parameter(description = "订单号") @PathVariable String orderNo) {
        PaymentResponse response = paymentService.initiatePayment(orderNo);
        return success(response);
    }
    
    @GetMapping("/query/{orderNo}")
    @Operation(summary = "查询订单状态", description = "查询支付订单状态")
    public Result<PaymentOrder> queryOrderStatus(
            @Parameter(description = "订单号") @PathVariable String orderNo) {
        PaymentOrder order = paymentService.queryOrderStatus(orderNo);
        return success(order);
    }
    
    @PostMapping("/cancel/{orderNo}")
    @Operation(summary = "取消订单", description = "取消支付订单")
    public Result<Void> cancelOrder(
            @Parameter(description = "订单号") @PathVariable String orderNo) {
        Long userId = UserContextUtil.getCurrentUserId();
        paymentService.cancelOrder(orderNo, userId);
        return success("订单已取消", null);
    }
    
    @PostMapping("/mock/callback/{orderNo}")
    @Operation(summary = "模拟支付回调", description = "模拟第三方支付回调（仅用于开发测试）")
    public Result<Void> mockPaymentCallback(
            @Parameter(description = "订单号") @PathVariable String orderNo,
            @Parameter(description = "是否成功") @RequestParam(defaultValue = "true") Boolean success) {
        paymentService.mockPaymentCallback(orderNo, success);
        return success("模拟回调成功", null);
    }
}
