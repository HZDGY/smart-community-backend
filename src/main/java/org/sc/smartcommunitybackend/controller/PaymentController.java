package org.sc.smartcommunitybackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sc.smartcommunitybackend.common.Result;
import org.sc.smartcommunitybackend.domain.PaymentOrder;
import org.sc.smartcommunitybackend.dto.request.CreatePaymentOrderRequest;
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
    
    @PostMapping("/create-order")
    @Operation(summary = "创建支付订单", description = "创建支付订单（通用接口），创建后需要调用发起支付接口")
    public Result<PaymentOrder> createOrder(
            @Parameter(description = "创建订单请求", required = true)
            @RequestBody @Valid CreatePaymentOrderRequest request) {
        Long userId = UserContextUtil.getCurrentUserId();
        
        // 创建支付订单
        PaymentOrder order = paymentService.createPaymentOrder(
                userId,
                request.getOrderType(),
                request.getAmount(),
                request.getPaymentMethod(),
                request.getRelatedId(),
                request.getDescription()
        );
        
        return success("订单创建成功，请调用发起支付接口", order);
    }
    
    @PostMapping("/create-and-pay")
    @Operation(summary = "创建订单并发起支付", description = "一步完成创建订单和发起支付（便捷接口）")
    public Result<PaymentResponse> createAndPay(
            @Parameter(description = "创建订单请求", required = true)
            @RequestBody @Valid CreatePaymentOrderRequest request) {
        Long userId = UserContextUtil.getCurrentUserId();
        
        // 创建支付订单
        PaymentOrder order = paymentService.createPaymentOrder(
                userId,
                request.getOrderType(),
                request.getAmount(),
                request.getPaymentMethod(),
                request.getRelatedId(),
                request.getDescription()
        );
        
        // 发起支付
        PaymentResponse response = paymentService.initiatePayment(order.getOrderNo());
        
        return success("订单创建成功，请完成支付", response);
    }
    
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
    
    @Autowired
    private org.sc.smartcommunitybackend.service.WalletService walletService;
    
    @PostMapping("/mock/callback/{orderNo}")
    @Operation(summary = "模拟支付回调", description = "模拟第三方支付回调（仅用于开发测试）")
    public Result<Void> mockPaymentCallback(
            @Parameter(description = "订单号") @PathVariable String orderNo,
            @Parameter(description = "是否成功") @RequestParam(defaultValue = "true") Boolean success) {
        
        // 记录回调前的余额
        Long userId = UserContextUtil.getCurrentUserId();
        org.sc.smartcommunitybackend.domain.UserWallet walletBefore = walletService.getWalletInfo(userId);
        org.slf4j.LoggerFactory.getLogger(getClass()).info("=== Controller: 回调前钱包余额: {} ===", walletBefore.getBalance());
        
        paymentService.mockPaymentCallback(orderNo, success);
        
        // 记录回调后的余额
        org.sc.smartcommunitybackend.domain.UserWallet walletAfter = walletService.getWalletInfo(userId);
        org.slf4j.LoggerFactory.getLogger(getClass()).info("=== Controller: 回调后钱包余额: {} ===", walletAfter.getBalance());
        
        return success("模拟回调成功", null);
    }
}
