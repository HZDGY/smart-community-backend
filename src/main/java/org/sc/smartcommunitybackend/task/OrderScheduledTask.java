package org.sc.smartcommunitybackend.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.sc.smartcommunitybackend.domain.PaymentOrder;
import org.sc.smartcommunitybackend.mapper.PaymentOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 订单定时任务
 * 负责处理订单的自动过期、删除等定时操作
 */
@Slf4j
@Component
public class OrderScheduledTask {

    @Autowired
    private PaymentOrderMapper paymentOrderMapper;

    /**
     * 订单过期时间（分钟），默认30分钟
     */
    @Value("${order.expire.minutes:30}")
    private Integer orderExpireMinutes;

    /**
     * 定时删除过期的待支付订单
     * 每5分钟执行一次
     * 
     * 注意：此方法会直接删除过期订单，无法恢复！
     * 如果需要保留订单历史记录，请使用 cancelExpiredOrders() 方法
     * 如需启用此功能，请取消下面 @Scheduled 注解的注释
     */
    // @Scheduled(cron = "0 */5 * * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void deleteExpiredOrders() {
        log.info("开始执行过期订单删除任务...");
        
        try {
            // 查询所有过期的待支付订单
            LambdaQueryWrapper<PaymentOrder> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(PaymentOrder::getStatus, 0) // 状态为待支付
                    .isNotNull(PaymentOrder::getExpireTime) // 有过期时间
                    .lt(PaymentOrder::getExpireTime, new Date()); // 过期时间小于当前时间
            
            List<PaymentOrder> expiredOrders = paymentOrderMapper.selectList(queryWrapper);
            
            if (expiredOrders.isEmpty()) {
                log.info("没有需要删除的过期订单");
                return;
            }
            
            log.info("发现 {} 个过期订单，准备删除", expiredOrders.size());
            
            // 删除过期订单
            int deletedCount = 0;
            for (PaymentOrder order : expiredOrders) {
                try {
                    int result = paymentOrderMapper.deleteById(order.getOrderId());
                    if (result > 0) {
                        deletedCount++;
                        log.info("删除过期订单成功 - 订单号: {}, 订单ID: {}, 创建时间: {}, 过期时间: {}", 
                                order.getOrderNo(), order.getOrderId(), order.getCreateTime(), order.getExpireTime());
                    }
                } catch (Exception e) {
                    log.error("删除过期订单失败 - 订单号: {}, 错误信息: {}", order.getOrderNo(), e.getMessage());
                }
            }
            
            log.info("过期订单删除任务完成，成功删除 {} 个订单", deletedCount);
            
        } catch (Exception e) {
            log.error("执行过期订单删除任务时发生错误", e);
            throw e;
        }
    }

    /**
     * 定时取消过期的待支付订单（推荐使用）
     * 每3分钟执行一次
     * 
     * 此方法会将过期订单状态更新为"已取消"，保留订单历史记录
     * 优点：便于数据分析和用户行为追踪
     * 如不需要此功能，可注释掉下面的 @Scheduled 注解
     */
    @Scheduled(cron = "0 */3 * * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void cancelExpiredOrders() {
        log.info("开始执行过期订单取消任务...");
        
        try {
            // 查询所有过期的待支付订单
            LambdaQueryWrapper<PaymentOrder> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(PaymentOrder::getStatus, 0) // 状态为待支付
                    .isNotNull(PaymentOrder::getExpireTime) // 有过期时间
                    .lt(PaymentOrder::getExpireTime, new Date()); // 过期时间小于当前时间
            
            List<PaymentOrder> expiredOrders = paymentOrderMapper.selectList(queryWrapper);
            
            if (expiredOrders.isEmpty()) {
                log.info("没有需要取消的过期订单");
                return;
            }
            
            log.info("发现 {} 个过期订单，准备取消", expiredOrders.size());
            
            // 更新订单状态为已取消（状态5）
            int canceledCount = 0;
            for (PaymentOrder order : expiredOrders) {
                try {
                    order.setStatus(5); // 5-已取消
                    order.setUpdateTime(new Date());
                    int result = paymentOrderMapper.updateById(order);
                    if (result > 0) {
                        canceledCount++;
                        log.info("取消过期订单成功 - 订单号: {}, 订单ID: {}, 创建时间: {}, 过期时间: {}", 
                                order.getOrderNo(), order.getOrderId(), order.getCreateTime(), order.getExpireTime());
                    }
                } catch (Exception e) {
                    log.error("取消过期订单失败 - 订单号: {}, 错误信息: {}", order.getOrderNo(), e.getMessage());
                }
            }
            
            log.info("过期订单取消任务完成，成功取消 {} 个订单", canceledCount);
            
        } catch (Exception e) {
            log.error("执行过期订单取消任务时发生错误", e);
            throw e;
        }
    }
}
