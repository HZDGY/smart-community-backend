package org.sc.smartcommunitybackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sc.smartcommunitybackend.common.Result;
import org.sc.smartcommunitybackend.common.annotation.RequirePermission;
import org.sc.smartcommunitybackend.domain.PropertyFeeBill;
import org.sc.smartcommunitybackend.domain.PropertyFeePayment;
import org.sc.smartcommunitybackend.dto.request.GenerateBillRequest;
import org.sc.smartcommunitybackend.dto.request.PayPropertyFeeRequest;
import org.sc.smartcommunitybackend.service.PropertyFeeService;
import org.sc.smartcommunitybackend.util.UserContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 物业费管理控制器
 */
@RestController
@RequestMapping("/property-fee")
@Tag(name = "物业费管理", description = "物业费账单和缴费相关接口")
public class PropertyFeeController extends BaseController {
    
    @Autowired
    private PropertyFeeService propertyFeeService;
    
    @GetMapping("/bills")
    @Operation(summary = "查询我的物业费账单", description = "查询当前用户的物业费账单列表")
//    @RequirePermission("property-fee:view")
    public Result<List<PropertyFeeBill>> getMyBills(
            @Parameter(description = "账单状态（0-未缴 1-已缴 2-部分缴纳）")
            @RequestParam(required = false) Integer status,
            @Parameter(description = "页码", example = "1")
            @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量", example = "10")
            @RequestParam(defaultValue = "10") Integer size) {
        Long userId = UserContextUtil.getCurrentUserId();
        List<PropertyFeeBill> bills = propertyFeeService.getUserBills(userId, status, page, size);
        
        return success(bills);
    }
    
    @GetMapping("/bills/{billId}")
    @Operation(summary = "获取账单详情", description = "查询指定账单的详细信息")
//    @RequirePermission("property-fee:view")
    public Result<PropertyFeeBill> getBillDetail(
            @Parameter(description = "账单ID", required = true)
            @PathVariable Long billId) {
        PropertyFeeBill bill = propertyFeeService.getBillDetail(billId);
        return success(bill);
    }
    
    @PostMapping("/pay")
    @Operation(summary = "缴纳物业费", description = "使用钱包或其他方式缴纳物业费")
//    @RequirePermission("property-fee:pay")
    public Result<PropertyFeePayment> payBill(
            @Parameter(description = "缴费请求", required = true)
            @RequestBody @Valid PayPropertyFeeRequest request) {
        Long userId = UserContextUtil.getCurrentUserId();
        PropertyFeePayment payment = propertyFeeService.payBill(
                userId, 
                request.getBillId(), 
                request.getAmount(), 
                request.getPaymentMethod()
        );
        return success("缴费成功", payment);
    }
    
    @GetMapping("/payments")
    @Operation(summary = "查询缴费记录", description = "查询当前用户的缴费记录")
//    @RequirePermission("property-fee:view")
    public Result<List<PropertyFeePayment>> getPaymentRecords(
            @Parameter(description = "页码", example = "1")
            @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量", example = "10")
            @RequestParam(defaultValue = "10") Integer size) {
        Long userId = UserContextUtil.getCurrentUserId();
        List<PropertyFeePayment> payments = propertyFeeService.getPaymentRecords(userId, page, size);
        
        return success(payments);
    }
    
//    @PostMapping("/bills/generate")
//    @Operation(summary = "生成物业费账单", description = "管理员生成物业费账单（需要管理权限）")
//    @RequirePermission("property-fee:manage")
//    public Result<PropertyFeeBill> generateBill(
//            @Parameter(description = "生成账单请求", required = true)
//            @RequestBody @Valid GenerateBillRequest request) {
//        PropertyFeeBill bill = propertyFeeService.generateBill(
//                request.getUserId(),
//                request.getBillingPeriod(),
//                request.getPropertyFee(),
//                request.getWaterFee(),
//                request.getElectricityFee(),
//                request.getGasFee(),
//                request.getParkingFee(),
//                request.getOtherFee(),
//                request.getDueDate()
//        );
//        return success("生成账单成功", bill);
//    }
}
