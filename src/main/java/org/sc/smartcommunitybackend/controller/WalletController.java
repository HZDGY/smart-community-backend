package org.sc.smartcommunitybackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sc.smartcommunitybackend.common.Result;
import org.sc.smartcommunitybackend.common.annotation.RequirePermission;
import org.sc.smartcommunitybackend.domain.PaymentOrder;
import org.sc.smartcommunitybackend.domain.UserWallet;
import org.sc.smartcommunitybackend.domain.WalletTransaction;
import org.sc.smartcommunitybackend.dto.request.RechargeRequest;
import org.sc.smartcommunitybackend.dto.request.TransferRequest;
import org.sc.smartcommunitybackend.dto.response.PaymentResponse;
import org.sc.smartcommunitybackend.enums.OrderType;
import org.sc.smartcommunitybackend.service.PaymentService;
import org.sc.smartcommunitybackend.service.WalletService;
import org.sc.smartcommunitybackend.util.UserContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 钱包管理控制器
 */
@RestController
@RequestMapping("/wallet")
@Tag(name = "钱包管理", description = "用户钱包相关接口")
public class WalletController extends BaseController {
    
    @Autowired
    private WalletService walletService;
    
    @Autowired
    private PaymentService paymentService;
    
    @GetMapping("/info")
    @Operation(summary = "获取钱包信息", description = "查询当前用户的钱包信息")
//    @RequirePermission("wallet:view")
    public Result<UserWallet> getWalletInfo() {
        Long userId = UserContextUtil.getCurrentUserId();
        UserWallet wallet = walletService.getWalletInfo(userId);
        return success(wallet);
    }
    
    @PostMapping("/recharge")
    @Operation(summary = "充值", description = "创建充值订单并发起支付（便捷接口，等同于 /payment/create-and-pay）")
//    @RequirePermission("wallet:recharge")
    public Result<PaymentResponse> recharge(
            @Parameter(description = "充值请求", required = true)
            @RequestBody @Valid RechargeRequest request) {
        Long userId = UserContextUtil.getCurrentUserId();
        
        // 创建支付订单
        PaymentOrder order = paymentService.createPaymentOrder(
                userId,
                OrderType.RECHARGE.getCode(),
                request.getAmount(),
                request.getPaymentMethod(),
                null,
                "钱包充值"
        );
        
        // 发起支付
        PaymentResponse response = paymentService.initiatePayment(order.getOrderNo());
        
        return success("充值订单已创建，请完成支付", response);
    }
    
    @PostMapping("/transfer")
    @Operation(summary = "转账", description = "向其他用户转账")
//    @RequirePermission("wallet:transfer")
    public Result<WalletTransaction> transfer(
            @Parameter(description = "转账请求", required = true)
            @RequestBody @Valid TransferRequest request) {
        Long userId = UserContextUtil.getCurrentUserId();
        WalletTransaction transaction = walletService.transfer(
                userId, 
                request.getToUserId(), 
                request.getAmount(), 
                request.getDescription()
        );
        return success("转账成功", transaction);
    }
    
    @GetMapping("/transactions")
    @Operation(summary = "查询交易记录", description = "查询当前用户的交易记录")
//    @RequirePermission("wallet:view")
    public Result<List<WalletTransaction>> getTransactions(
            @Parameter(description = "交易类型", example = "ALL")
            @RequestParam(defaultValue = "ALL") String type,
            @Parameter(description = "页码", example = "1")
            @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量", example = "10")
            @RequestParam(defaultValue = "10") Integer size) {
        Long userId = UserContextUtil.getCurrentUserId();
        List<WalletTransaction> transactions = walletService.getTransactions(userId, type, page, size);
        
        return success(transactions);
    }
}
