package org.sc.smartcommunitybackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.sc.smartcommunitybackend.domain.PaymentOrder;
import org.sc.smartcommunitybackend.dto.request.CreateProductOrderRequest;
import org.sc.smartcommunitybackend.dto.response.OrderDetailVO;
import org.sc.smartcommunitybackend.dto.response.OrderListVO;

/**
 * 统一订单服务接口
 */
public interface UnifiedOrderService {
    
    /**
     * 创建商品订单
     * @param userId 用户ID
     * @param request 订单请求
     * @return 订单详情
     */
    OrderDetailVO createProductOrder(Long userId, CreateProductOrderRequest request);
    
    /**
     * 查询用户所有订单（分页）
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 订单列表
     */
    Page<OrderListVO> queryUserOrders(Long userId, Integer pageNum, Integer pageSize);
    
    /**
     * 按状态查询用户订单（分页）
     * @param userId 用户ID
     * @param status 订单状态
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 订单列表
     */
    Page<OrderListVO> queryUserOrdersByStatus(Long userId, Integer status, Integer pageNum, Integer pageSize);
    
    /**
     * 按类型查询用户订单（分页）
     * @param userId 用户ID
     * @param orderType 订单类型
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 订单列表
     */
    Page<OrderListVO> queryUserOrdersByType(Long userId, String orderType, Integer pageNum, Integer pageSize);
    
    /**
     * 查询订单详情
     * @param userId 用户ID
     * @param orderId 订单ID
     * @return 订单详情
     */
    OrderDetailVO queryOrderDetail(Long userId, Long orderId);
    
    /**
     * 取消订单
     * @param userId 用户ID
     * @param orderId 订单ID
     * @return 是否成功
     */
    boolean cancelOrder(Long userId, Long orderId);
    
    /**
     * 确认收货/完成订单
     * @param userId 用户ID
     * @param orderId 订单ID
     * @return 是否成功
     */
    boolean confirmOrder(Long userId, Long orderId);
}

