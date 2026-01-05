package org.sc.smartcommunitybackend.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 权限名称生成工具类
 * 根据权限编码自动生成中文名称和描述
 */
public class PermissionNameGenerator {
    
    /**
     * 资源类型映射表（英文 -> 中文）
     */
    private static final Map<String, String> RESOURCE_MAP = new HashMap<>();
    
    /**
     * 操作类型映射表（英文 -> 中文）
     */
    private static final Map<String, String> ACTION_MAP = new HashMap<>();
    
    static {
        // 初始化资源映射
        RESOURCE_MAP.put("user", "用户");
        RESOURCE_MAP.put("role", "角色");
        RESOURCE_MAP.put("permission", "权限");
        RESOURCE_MAP.put("complaint", "投诉");
        RESOURCE_MAP.put("repair", "报修");
        RESOURCE_MAP.put("parking", "停车");
        RESOURCE_MAP.put("announcement", "公告");
        RESOURCE_MAP.put("visitor", "访客");
        RESOURCE_MAP.put("mall", "商品");
        RESOURCE_MAP.put("order", "订单");
        RESOURCE_MAP.put("report", "报表");
        RESOURCE_MAP.put("data", "数据");
        RESOURCE_MAP.put("system", "系统");
        
        // 初始化操作映射
        ACTION_MAP.put("view", "查看");
        ACTION_MAP.put("create", "创建");
        ACTION_MAP.put("update", "更新");
        ACTION_MAP.put("delete", "删除");
        ACTION_MAP.put("audit", "审核");
        ACTION_MAP.put("handle", "处理");
        ACTION_MAP.put("approve", "审批");
        ACTION_MAP.put("assign-role", "分配角色");
        ACTION_MAP.put("assign-permission", "分配权限");
        ACTION_MAP.put("view-permission", "查看权限");
        ACTION_MAP.put("manage", "管理");
        ACTION_MAP.put("export", "导出");
        ACTION_MAP.put("import", "导入");
        ACTION_MAP.put("backup", "备份");
        ACTION_MAP.put("restore", "恢复");
    }
    
    /**
     * 根据权限编码生成权限名称
     * 例如：user:view -> 查看用户
     * 
     * @param permissionCode 权限编码（格式：资源:操作）
     * @return 权限名称
     */
    public static String generateName(String permissionCode) {
        if (permissionCode == null || !permissionCode.contains(":")) {
            return permissionCode;
        }
        
        String[] parts = permissionCode.split(":");
        if (parts.length != 2) {
            return permissionCode;
        }
        
        String resource = parts[0].trim();
        String action = parts[1].trim();
        
        String resourceCn = RESOURCE_MAP.getOrDefault(resource, resource);
        String actionCn = ACTION_MAP.getOrDefault(action, action);
        
        return actionCn + resourceCn;
    }
    
    /**
     * 根据权限编码生成权限描述
     * 例如：user:view -> 查看用户权限
     * 
     * @param permissionCode 权限编码（格式：资源:操作）
     * @return 权限描述
     */
    public static String generateDescription(String permissionCode) {
        String name = generateName(permissionCode);
        return name + "权限";
    }
    
    /**
     * 添加资源映射
     * 
     * @param resourceKey 资源英文名
     * @param resourceName 资源中文名
     */
    public static void addResourceMapping(String resourceKey, String resourceName) {
        RESOURCE_MAP.put(resourceKey, resourceName);
    }
    
    /**
     * 添加操作映射
     * 
     * @param actionKey 操作英文名
     * @param actionName 操作中文名
     */
    public static void addActionMapping(String actionKey, String actionName) {
        ACTION_MAP.put(actionKey, actionName);
    }
}
