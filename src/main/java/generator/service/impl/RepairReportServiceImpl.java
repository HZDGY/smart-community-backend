package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.RepairReport;
import generator.service.RepairReportService;
import generator.mapper.RepairReportMapper;
import org.springframework.stereotype.Service;

/**
* @author 吴展德
* @description 针对表【repair_report(报事维修表)】的数据库操作Service实现
* @createDate 2025-12-30 10:46:05
*/
@Service
public class RepairReportServiceImpl extends ServiceImpl<RepairReportMapper, RepairReport>
    implements RepairReportService{

}




