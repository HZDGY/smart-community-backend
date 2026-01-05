package org.sc.smartcommunitybackend.dto.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限扫描结果 DTO
 */
@Data
public class PermissionScanResult {
    
    /**
     * 扫描到的权限总数
     */
    private Integer totalScanned;
    
    /**
     * 新增的权限数量
     */
    private Integer newAdded;
    
    /**
     * 已存在的权限数量
     */
    private Integer alreadyExists;
    
    /**
     * 新增的权限列表
     */
    private List<String> newPermissions = new ArrayList<>();
    
    /**
     * 已存在的权限列表
     */
    private List<String> existingPermissions = new ArrayList<>();
    
    /**
     * 扫描的控制器数量
     */
    private Integer controllerCount;
    
    /**
     * 扫描的方法数量
     */
    private Integer methodCount;
}
