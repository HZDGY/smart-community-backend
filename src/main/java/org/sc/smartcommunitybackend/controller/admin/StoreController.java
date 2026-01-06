package org.sc.smartcommunitybackend.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.sc.smartcommunitybackend.common.Result;
import org.sc.smartcommunitybackend.dto.request.AdminStoreListRequest;
import org.sc.smartcommunitybackend.dto.request.StoreRequest;
import org.sc.smartcommunitybackend.dto.response.PageResult;
import org.sc.smartcommunitybackend.dto.response.StoreVO;
import org.sc.smartcommunitybackend.service.StoreService;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端-门店管理接口
 */
@Slf4j
@RestController
@RequestMapping("/admin/stores")
@Tag(name = "管理端-门店管理接口")
public class StoreController {
    @Resource
    private StoreService storeService;
    
    @PostMapping("/list")
    @Operation(summary = "门店列表")
    public Result<PageResult<StoreVO>> storeList(@RequestBody AdminStoreListRequest adminStoreListRequest) {
        PageResult<StoreVO> storeVOPageResult = storeService.adminStoreList(adminStoreListRequest);
        return Result.success(storeVOPageResult);
    }
    
    @PostMapping
    @Operation(summary = "新增门店")
    public Result<Boolean> addStore(@RequestBody StoreRequest storeRequest) {
        boolean success = storeService.addStore(storeRequest);
        return Result.success(success);
    }
    
    @PutMapping
    @Operation(summary = "修改门店")
    public Result<Boolean> updateStore(@RequestBody StoreRequest storeRequest) {
        boolean success = storeService.updateStore(storeRequest);
        return  Result.success(success);
    }
    
    @DeleteMapping("/{storeId}")
    @Operation(summary = "删除门店")
    public Result<Boolean> deleteStore(@PathVariable Long storeId) {
        boolean success = storeService.deleteStore(storeId);
        return  Result.success(success);}
}