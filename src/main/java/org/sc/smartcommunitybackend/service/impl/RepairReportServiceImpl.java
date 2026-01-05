package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sc.smartcommunitybackend.constant.RepairReportConstant;
import org.sc.smartcommunitybackend.domain.RepairReport;
import org.sc.smartcommunitybackend.dto.request.RepairReportRequest;
import org.sc.smartcommunitybackend.dto.response.RepairReportResponse;
import org.sc.smartcommunitybackend.service.RepairReportService;
import org.sc.smartcommunitybackend.mapper.RepairReportMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author 吴展德
* @description 针对表【repair_report(报事维修表)】的数据库操作Service实现
* @createDate 2025-12-30 10:46:05
*/
@Service
public class RepairReportServiceImpl extends ServiceImpl<RepairReportMapper, RepairReport>
    implements RepairReportService{

    @Override
    public RepairReportResponse submitRepairReport(Long userId, RepairReportRequest request) {
        // 创建报事维修记录
        RepairReport repairReport = new RepairReport();
        repairReport.setUserId(userId);
        repairReport.setReportType(request.getReportType());
        repairReport.setDescription(request.getDescription());
        repairReport.setLocation(request.getLocation());
        repairReport.setStatus(RepairReportConstant.STATUS_PENDING);
        repairReport.setCreateTime(new Date());
        
        // 保存到数据库
        this.save(repairReport);
        
        // 构造响应
        RepairReportResponse response = new RepairReportResponse();
        BeanUtils.copyProperties(repairReport, response);
        response.setStatusText(getStatusText(repairReport.getStatus()));
        
        return response;
    }

    @Override
    public Page<RepairReportResponse> getUserRepairReportList(Long userId, Integer status, Integer pageNum, Integer pageSize) {
        // 构建查询条件
        LambdaQueryWrapper<RepairReport> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RepairReport::getUserId, userId);
        
        // 如果指定了状态，添加状态过滤
        if (status != null) {
            queryWrapper.eq(RepairReport::getStatus, status);
        }
        
        // 按创建时间倒序排列
        queryWrapper.orderByDesc(RepairReport::getCreateTime);
        
        // 分页查询
        Page<RepairReport> page = new Page<>(pageNum, pageSize);
        Page<RepairReport> resultPage = this.page(page, queryWrapper);
        
        // 转换为响应对象
        Page<RepairReportResponse> responsePage = new Page<>();
        BeanUtils.copyProperties(resultPage, responsePage, "records");
        
        responsePage.setRecords(
            resultPage.getRecords().stream()
                .map(this::convertToResponse)
                .toList()
        );
        
        return responsePage;
    }

    @Override
    public RepairReportResponse getRepairReportDetail(Long userId, Long reportId) {
        // 查询报事维修记录
        LambdaQueryWrapper<RepairReport> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RepairReport::getReportId, reportId)
                   .eq(RepairReport::getUserId, userId);
        
        RepairReport repairReport = this.getOne(queryWrapper);
        
        if (repairReport == null) {
            throw new RuntimeException("报事维修记录不存在");
        }
        
        return convertToResponse(repairReport);
    }
    
    /**
     * 转换为响应对象
     */
    private RepairReportResponse convertToResponse(RepairReport repairReport) {
        RepairReportResponse response = new RepairReportResponse();
        BeanUtils.copyProperties(repairReport, response);
        response.setStatusText(getStatusText(repairReport.getStatus()));
        return response;
    }
    
    /**
     * 获取状态描述
     */
    private String getStatusText(Integer status) {
        if (status == null) {
            return "未知";
        }
        switch (status) {
            case RepairReportConstant.STATUS_PENDING:
                return "待处理";
            case RepairReportConstant.STATUS_PROCESSING:
                return "处理中";
            case RepairReportConstant.STATUS_COMPLETED:
                return "已完成";
            case RepairReportConstant.STATUS_REJECTED:
                return "已驳回";
            default:
                return "未知";
        }
    }
}




