package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.sc.smartcommunitybackend.domain.*;
import org.sc.smartcommunitybackend.dto.request.CreateProductOrderRequest;
import org.sc.smartcommunitybackend.dto.response.OrderDetailVO;
import org.sc.smartcommunitybackend.dto.response.OrderListVO;
import org.sc.smartcommunitybackend.dto.response.OrderProductVO;
import org.sc.smartcommunitybackend.enums.OrderType;
import org.sc.smartcommunitybackend.enums.PaymentMethod;
import org.sc.smartcommunitybackend.enums.PaymentOrderStatus;
import org.sc.smartcommunitybackend.exception.BusinessException;
import org.sc.smartcommunitybackend.mapper.OrderProductMapper;
import org.sc.smartcommunitybackend.mapper.PaymentOrderMapper;
import org.sc.smartcommunitybackend.service.*;
import org.sc.smartcommunitybackend.util.TransactionNoGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 统一订单服务实现
 */
@Slf4j
@Service
public class UnifiedOrderServiceImpl implements UnifiedOrderService {
    
    @Autowired
    private PaymentOrderMapper paymentOrderMapper;
    
    @Autowired
    private OrderProductMapper orderProductMapper;
    
    @Autowired
    private ShoppingCartService shoppingCartService;
    
    @Autowired
    private ProductService productService;

    @Autowired
    private StoreProductService storeProductService;

    @Autowired
    private StoreService storeService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderDetailVO createProductOrder(Long userId, CreateProductOrderRequest request) {
        log.info("创建商品订单, userId={}, request={}", userId, request);
        
        // 1. 查询购物车商品
        List<ShoppingCart> cartItems = shoppingCartService.listByIds(request.getCartItemIds());
        if (cartItems.isEmpty()) {
            throw new BusinessException("购物车商品不存在");
        }
        
        // 校验购物车商品是否都属于当前用户
        boolean allBelongToUser = cartItems.stream()
                .allMatch(item -> item.getUser_id().equals(userId));
        if (!allBelongToUser) {
            throw new BusinessException("购物车商品数据异常");
        }
        
        // 2. 计算订单总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderProduct> orderProducts = new ArrayList<>();
        
        for (ShoppingCart cartItem : cartItems) {

            Product product = productService.getById(cartItem.getProduct_id());
            StoreProduct storeProduct = storeProductService.getStoreProductById(cartItem.getProduct_id(), cartItem.getStore_id());

            if (storeProduct == null) {
                throw new BusinessException("商品不存在: " + cartItem.getProduct_id());
            }
            
            if (storeProduct.getStatus() != 1) {
                throw new BusinessException("商品已下架: " + product.getProduct_name());
            }
            
            if (storeProduct.getStock() < cartItem.getQuantity()) {
                throw new BusinessException("门店商品库存不足: " + product.getProduct_name());
            }
            
            BigDecimal subtotal = product.getPrice().multiply(new BigDecimal(cartItem.getQuantity()));
            totalAmount = totalAmount.add(subtotal);
            
            // 准备订单商品数据（暂不保存）
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setProductId(product.getProduct_id());
            orderProduct.setProductName(product.getProduct_name());
            orderProduct.setProductImage(product.getCover_img());
            orderProduct.setQuantity(cartItem.getQuantity());
            orderProduct.setPrice(product.getPrice());
            orderProduct.setSubtotal(subtotal);
            orderProduct.setStoreId(request.getStoreId());
            orderProducts.add(orderProduct);
        }
        
        // 3. 查询门店信息
        Store store = storeService.getById(request.getStoreId());
        if (store == null) {
            throw new BusinessException("门店不存在");
        }
        
        // 4. 创建支付订单
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setOrderNo(TransactionNoGenerator.generatePaymentOrderNo());
        paymentOrder.setUserId(userId);
        paymentOrder.setOrderType(OrderType.PRODUCT.getCode());
        paymentOrder.setAmount(totalAmount);
        paymentOrder.setPaymentMethod(PaymentMethod.ALIPAY.name()); // 默认支付宝，后续可选择
        paymentOrder.setStatus(PaymentOrderStatus.PENDING.getCode());
        paymentOrder.setStoreId(request.getStoreId());
        paymentOrder.setDescription(request.getRemark() != null ? request.getRemark() : "商品订单");
        
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + 30 * 60 * 1000); // 30分钟过期
        paymentOrder.setExpireTime(expireTime);
        paymentOrder.setCreateTime(now);
        paymentOrder.setUpdateTime(now);
        
        paymentOrderMapper.insert(paymentOrder);
        
        // 5. 保存订单商品关联
        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.setOrderId(paymentOrder.getOrderId());
            orderProduct.setStoreName(store.getStore_name());
            orderProduct.setCreateTime(now);
            orderProductMapper.insert(orderProduct);
        }
        
        // 6. 清空购物车
        shoppingCartService.removeByIds(request.getCartItemIds());
        
        // 7. 扣减库存
        for (ShoppingCart cartItem : cartItems) {
            Product product = productService.getById(cartItem.getProduct_id());
            product.setStock(product.getStock() - cartItem.getQuantity());
            productService.updateById(product);
        }
        
        log.info("商品订单创建成功, orderNo={}", paymentOrder.getOrderNo());
        
        // 8. 返回订单详情
        return queryOrderDetail(userId, paymentOrder.getOrderId());
    }
    
    @Override
    public Page<OrderListVO> queryUserOrders(Long userId, Integer pageNum, Integer pageSize) {
        Page<PaymentOrder> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PaymentOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentOrder::getUserId, userId)
                .orderByDesc(PaymentOrder::getCreateTime);
        
        Page<PaymentOrder> orderPage = paymentOrderMapper.selectPage(page, wrapper);
        
        return convertToOrderListVO(orderPage);
    }
    
    @Override
    public Page<OrderListVO> queryUserOrdersByStatus(Long userId, Integer status, Integer pageNum, Integer pageSize) {
        Page<PaymentOrder> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PaymentOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentOrder::getUserId, userId)
                .eq(PaymentOrder::getStatus, status)
                .orderByDesc(PaymentOrder::getCreateTime);
        
        Page<PaymentOrder> orderPage = paymentOrderMapper.selectPage(page, wrapper);
        
        return convertToOrderListVO(orderPage);
    }
    
    @Override
    public Page<OrderListVO> queryUserOrdersByType(Long userId, String orderType, Integer pageNum, Integer pageSize) {
        Page<PaymentOrder> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PaymentOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentOrder::getUserId, userId)
                .eq(PaymentOrder::getOrderType, orderType)
                .orderByDesc(PaymentOrder::getCreateTime);
        
        Page<PaymentOrder> orderPage = paymentOrderMapper.selectPage(page, wrapper);
        
        return convertToOrderListVO(orderPage);
    }
    
    @Override
    public OrderDetailVO queryOrderDetail(Long userId, Long orderId) {
        PaymentOrder order = paymentOrderMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        
        OrderDetailVO vo = new OrderDetailVO();
        BeanUtils.copyProperties(order, vo);
        
        // 设置描述信息
        vo.setOrderTypeDesc(getOrderTypeDesc(order.getOrderType()));
        vo.setStatusDesc(getStatusDesc(order.getStatus()));
        vo.setPaymentMethodDesc(getPaymentMethodDesc(order.getPaymentMethod()));
        
        // 如果是商品订单，查询商品列表
        if (OrderType.PRODUCT.getCode().equals(order.getOrderType())) {
            List<OrderProduct> products = orderProductMapper.selectByOrderId(orderId);
            if (!products.isEmpty()) {
                vo.setStoreName(products.get(0).getStoreName());
                List<OrderProductVO> productVOs = products.stream().map(p -> {
                    OrderProductVO pvo = new OrderProductVO();
                    BeanUtils.copyProperties(p, pvo);
                    return pvo;
                }).collect(Collectors.toList());
                vo.setProducts(productVOs);
            }
        }
        
        return vo;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelOrder(Long userId, Long orderId) {
        PaymentOrder order = paymentOrderMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        
        if (order.getStatus().equals(PaymentOrderStatus.SUCCESS.getCode()) ||
            order.getStatus().equals(PaymentOrderStatus.COMPLETED.getCode())) {
            throw new BusinessException("订单已支付，无法取消");
        }
        
        if (order.getStatus().equals(PaymentOrderStatus.CANCELLED.getCode())) {
            throw new BusinessException("订单已取消");
        }
        
        // 如果是商品订单，恢复库存
        if (OrderType.PRODUCT.getCode().equals(order.getOrderType())) {
            List<OrderProduct> products = orderProductMapper.selectByOrderId(orderId);
            for (OrderProduct op : products) {
                Product product = productService.getById(op.getProductId());
                if (product != null) {
                    product.setStock(product.getStock() + op.getQuantity());
                    productService.updateById(product);
                }
            }
        }
        
        order.setStatus(PaymentOrderStatus.CANCELLED.getCode());
        order.setUpdateTime(new Date());
        paymentOrderMapper.updateById(order);
        
        log.info("取消订单成功, orderNo={}", order.getOrderNo());
        return true;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmOrder(Long userId, Long orderId) {
        PaymentOrder order = paymentOrderMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        
        if (!order.getStatus().equals(PaymentOrderStatus.SUCCESS.getCode())) {
            throw new BusinessException("订单状态不正确");
        }
        
        order.setStatus(PaymentOrderStatus.COMPLETED.getCode());
        order.setFinishTime(new Date());
        order.setUpdateTime(new Date());
        paymentOrderMapper.updateById(order);
        
        log.info("确认收货成功, orderNo={}", order.getOrderNo());
        return true;
    }
    
    /**
     * 转换为订单列表VO
     */
    private Page<OrderListVO> convertToOrderListVO(Page<PaymentOrder> orderPage) {
        Page<OrderListVO> voPage = new Page<>(orderPage.getCurrent(), orderPage.getSize(), orderPage.getTotal());
        
        List<OrderListVO> voList = orderPage.getRecords().stream().map(order -> {
            OrderListVO vo = new OrderListVO();
            vo.setOrderId(order.getOrderId());
            vo.setOrderNo(order.getOrderNo());
            vo.setOrderType(order.getOrderType());
            vo.setOrderTypeDesc(getOrderTypeDesc(order.getOrderType()));
            vo.setAmount(order.getAmount());
            vo.setStatus(order.getStatus());
            vo.setStatusDesc(getStatusDesc(order.getStatus()));
            vo.setPaymentMethod(order.getPaymentMethod());
            vo.setPaymentMethodDesc(getPaymentMethodDesc(order.getPaymentMethod()));
            vo.setDescription(order.getDescription());
            vo.setCreateTime(order.getCreateTime());
            vo.setExpireTime(order.getExpireTime());
            
            // 如果是商品订单，查询商品数量和门店
            if (OrderType.PRODUCT.getCode().equals(order.getOrderType())) {
                List<OrderProduct> products = orderProductMapper.selectByOrderId(order.getOrderId());
                if (!products.isEmpty()) {
                    vo.setStoreName(products.get(0).getStoreName());
                    vo.setProductCount(products.stream().mapToInt(OrderProduct::getQuantity).sum());
                }
            }
            
            return vo;
        }).collect(Collectors.toList());
        
        voPage.setRecords(voList);
        return voPage;
    }
    
    /**
     * 获取订单类型描述
     */
    private String getOrderTypeDesc(String orderType) {
        for (OrderType type : OrderType.values()) {
            if (type.getCode().equals(orderType)) {
                return type.getDescription();
            }
        }
        return orderType;
    }
    
    /**
     * 获取订单状态描述
     */
    private String getStatusDesc(Integer status) {
        PaymentOrderStatus orderStatus = PaymentOrderStatus.fromCode(status);
        return orderStatus != null ? orderStatus.getDescription() : "未知状态";
    }
    
    /**
     * 获取支付方式描述
     */
    private String getPaymentMethodDesc(String paymentMethod) {
        try {
            PaymentMethod method = PaymentMethod.valueOf(paymentMethod);
            return method.getDescription();
        } catch (Exception e) {
            return paymentMethod;
        }
    }
}

