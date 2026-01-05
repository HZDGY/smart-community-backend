package org.sc.smartcommunitybackend.service;

import org.sc.smartcommunitybackend.dto.response.PermissionScanResult;

/**
 * 权限扫描服务接口
 */
public interface PermissionScanService {
    
    /**
     * 扫描所有控制器中的权限注解并保存到数据库
     * 
     * @return 扫描结果
     */
    PermissionScanResult scanAndSavePermissions();
}
