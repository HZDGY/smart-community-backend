package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.OrderItem;
import generator.service.OrderItemService;
import generator.mapper.OrderItemMapper;
import org.springframework.stereotype.Service;

/**
* @author 吴展德
* @description 针对表【order_item(订单项表)】的数据库操作Service实现
* @createDate 2025-12-30 10:46:05
*/
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem>
    implements OrderItemService{

}




