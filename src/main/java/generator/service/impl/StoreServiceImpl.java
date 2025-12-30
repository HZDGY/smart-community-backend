package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.Store;
import generator.service.StoreService;
import generator.mapper.StoreMapper;
import org.springframework.stereotype.Service;

/**
* @author 吴展德
* @description 针对表【store(门店表)】的数据库操作Service实现
* @createDate 2025-12-30 10:46:05
*/
@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store>
    implements StoreService{

}




