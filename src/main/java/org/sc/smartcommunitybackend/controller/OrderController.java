package org.sc.smartcommunitybackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.sc.smartcommunitybackend.common.Result;
import org.sc.smartcommunitybackend.dto.request.CreateProductOrderRequest;
import org.sc.smartcommunitybackend.dto.response.OrderDetailVO;
import org.sc.smartcommunitybackend.dto.response.OrderListVO;
import org.sc.smartcommunitybackend.service.UnifiedOrderService;
import org.sc.smartcommunitybackend.util.UserContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 统一订单控制器
 */
@Slf4j
@RestController
@RequestMapping("/orders")
@Tag(name = "订单管理接口", description = "统一订单管理，包含商品订单、充值订单、物业费订单等")
public class OrderController extends BaseController {
    
    @Autowired
    private UnifiedOrderService unifiedOrderService;
    
    @PostMapping("/product/create")
    @Operation(summary = "创建商品订单", description = "从购物车创建商品订单")
    public Result<OrderDetailVO> createProductOrder(
            @Parameter(description = "创建商品订单请求", required = true)
            @RequestBody @Valid CreateProductOrderRequest request) {
        Long userId = UserContextUtil.getCurrentUserId();
        OrderDetailVO order = unifiedOrderService.createProductOrder(userId, request);
        return success("订单创建成功", order);
    }
    
    @GetMapping("/list")
    @Operation(summary = "查询我的所有订单", description = "分页查询当前用户的所有订单")
    public Result<Page<OrderListVO>> queryMyOrders(
            @Parameter(description = "页码", example = "1")
            @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小", example = "10")
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = UserContextUtil.getCurrentUserId();
        Page<OrderListVO> orders = unifiedOrderService.queryUserOrders(userId, pageNum, pageSize);
        return success(orders);
    }
    
    @GetMapping("/list/by-status")
    @Operation(summary = "按状态查询订单", 
               description = "按状态筛选订单：0-待支付，1-支付中，2-支付成功/待取货，3-已完成，4-支付失败，5-已取消，6-已退款")
    public Result<Page<OrderListVO>> queryOrdersByStatus(
            @Parameter(description = "订单状态", required = true, example = "0")
            @RequestParam Integer status,
            @Parameter(description = "页码", example = "1")
            @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小", example = "10")
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = UserContextUtil.getCurrentUserId();
        Page<OrderListVO> orders = unifiedOrderService.queryUserOrdersByStatus(userId, status, pageNum, pageSize);
        return success(orders);
    }
    
    @GetMapping("/list/by-type")
    @Operation(summary = "按类型查询订单", 
               description = "按类型筛选订单：PRODUCT-商品订单，RECHARGE-充值，PROPERTY_FEE-物业费，PARKING_FEE-停车费等")
    public Result<Page<OrderListVO>> queryOrdersByType(
            @Parameter(description = "订单类型", required = true, example = "PRODUCT")
            @RequestParam String orderType,
            @Parameter(description = "页码", example = "1")
            @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小", example = "10")
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = UserContextUtil.getCurrentUserId();
        Page<OrderListVO> orders = unifiedOrderService.queryUserOrdersByType(userId, orderType, pageNum, pageSize);
        return success(orders);
    }
    
    @GetMapping("/{orderId}")
    @Operation(summary = "查询订单详情", description = "根据订单ID查询订单详细信息")
    public Result<OrderDetailVO> queryOrderDetail(
            @Parameter(description = "订单ID", required = true)
            @PathVariable Long orderId) {
        Long userId = UserContextUtil.getCurrentUserId();
        OrderDetailVO order = unifiedOrderService.queryOrderDetail(userId, orderId);
        return success(order);
    }
    
    @PostMapping("/{orderId}/cancel")
    @Operation(summary = "取消订单", description = "取消待支付的订单")
    public Result<Void> cancelOrder(
            @Parameter(description = "订单ID", required = true)
            @PathVariable Long orderId) {
        Long userId = UserContextUtil.getCurrentUserId();
        unifiedOrderService.cancelOrder(userId, orderId);
        return success();
    }
    
    @PostMapping("/{orderId}/confirm")
    @Operation(summary = "确认收货/完成订单", description = "确认收货，订单状态变为已完成")
    public Result<Void> confirmOrder(
            @Parameter(description = "订单ID", required = true)
            @PathVariable Long orderId) {
        Long userId = UserContextUtil.getCurrentUserId();
        unifiedOrderService.confirmOrder(userId, orderId);
        return success();
    }
}

