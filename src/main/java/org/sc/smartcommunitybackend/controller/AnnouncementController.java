package org.sc.smartcommunitybackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sc.smartcommunitybackend.common.Result;
import org.sc.smartcommunitybackend.dto.request.AnnouncementQueryRequest;
import org.sc.smartcommunitybackend.dto.response.AnnouncementResponse;
import org.sc.smartcommunitybackend.service.CommunityAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 公告控制器
 */
@RestController
@RequestMapping("/api/announcement")
@Tag(name = "公告管理", description = "社区公告查询相关接口")
public class AnnouncementController extends BaseController {

    @Autowired
    private CommunityAnnouncementService announcementService;

    @GetMapping("/list")
    @Operation(summary = "公告列表查询", 
               description = "分页查询社区公告列表\n" +
                           "支持功能:\n" +
                           "1. 关键词搜索(keyword)\n" +
                           "2. 检索范围选择: ALL-全文检索, TITLE-仅标题(searchScope)\n" +
                           "3. 时间范围筛选: ALL-全部, WEEK-最近一周, MONTH-最近一月, YEAR-最近一年(timeRange)\n" +
                           "4. 排序方式: TIME-按时间排序, RELEVANCE-按相关度排序(sortType)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "查询成功"),
            @ApiResponse(responseCode = "400", description = "参数错误"),
            @ApiResponse(responseCode = "401", description = "未授权（token无效或过期）")
    })
    public Result<Page<AnnouncementResponse>> getAnnouncementList(
            @Parameter(description = "查询条件(支持keyword、searchScope、timeRange、sortType、pageNum、pageSize)") 
            @Valid AnnouncementQueryRequest request) {
        Page<AnnouncementResponse> page = announcementService.queryAnnouncementList(request);
        return success(page);
    }

    @GetMapping("/{announceId}")
    @Operation(summary = "公告详情", description = "获取公告详细信息,并增加阅读次数")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "查询成功"),
            @ApiResponse(responseCode = "401", description = "未授权（token无效或过期）"),
            @ApiResponse(responseCode = "600", description = "业务异常（如公告不存在）")
    })
    public Result<AnnouncementResponse> getAnnouncementDetail(
            @Parameter(description = "公告ID", required = true) @PathVariable Long announceId) {
        AnnouncementResponse response = announcementService.getAnnouncementDetail(announceId);
        return success(response);
    }
}

