package org.sc.smartcommunitybackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.sc.smartcommunitybackend.domain.RepairReport;
import com.baomidou.mybatisplus.extension.service.IService;
import org.sc.smartcommunitybackend.dto.request.RepairReportRequest;
import org.sc.smartcommunitybackend.dto.response.RepairReportResponse;

import java.util.List;

/**
* @author 吴展德
* @description 针对表【repair_report(报事维修表)】的数据库操作Service
* @createDate 2025-12-30 10:46:05
*/
public interface RepairReportService extends IService<RepairReport> {

    /**
     * 提交报事维修
     * @param userId 用户ID
     * @param request 报事维修请求
     * @return 报事维修响应
     */
    RepairReportResponse submitRepairReport(Long userId, RepairReportRequest request);

    /**
     * 查询用户的报事维修列表
     * @param userId 用户ID
     * @param status 处理状态（可选）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    Page<RepairReportResponse> getUserRepairReportList(Long userId, Integer status, Integer pageNum, Integer pageSize);

    /**
     * 查询报事维修详情
     * @param userId 用户ID
     * @param reportId 报事ID
     * @return 报事维修响应
     */
    RepairReportResponse getRepairReportDetail(Long userId, Long reportId);
}
