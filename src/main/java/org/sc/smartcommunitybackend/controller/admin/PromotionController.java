package org.sc.smartcommunitybackend.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.sc.smartcommunitybackend.common.Result;
import org.sc.smartcommunitybackend.common.annotation.RequirePermission;
import org.sc.smartcommunitybackend.dto.request.PromotionPageRequest;
import org.sc.smartcommunitybackend.dto.request.PromotionRequest;
import org.sc.smartcommunitybackend.dto.response.AdminProductVO;
import org.sc.smartcommunitybackend.dto.response.PageResult;
import org.sc.smartcommunitybackend.dto.response.PromotionVO;
import org.sc.smartcommunitybackend.service.PromotionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/promotions")
@Tag(name = "管理端-营销管理接口")
public class PromotionController {
    @Resource
    private PromotionService promotionService;

    @PostMapping("/list")
    @Operation(summary = "查询营销列表")
    public Result<PageResult<PromotionVO>> list(@RequestBody PromotionPageRequest promotionPageRequest) {
        PageResult<PromotionVO> promotionVOPageResult = promotionService.queryList(promotionPageRequest);
        return Result.success(promotionVOPageResult);
    }

    @PostMapping()
    @Operation(summary = "添加营销")
    private Result<Long> add(@RequestBody PromotionRequest promotionRequest) {
        Long add = promotionService.add(promotionRequest);
        return Result.success(add);
    }

    @PutMapping("/{promotionId}")
    @Operation(summary = "修改营销")
    private Result<Boolean> update( @PathVariable Long promotionId,@RequestBody PromotionRequest promotionRequest) {
        return Result.success(promotionService.updatePromotion(promotionId,promotionRequest));
    }
    @DeleteMapping("/{promotionId}")
    @Operation(summary = "删除营销")
    private Result<Boolean> delete(@PathVariable Long promotionId) {
        return Result.success(promotionService.delete(promotionId));
    }
}
