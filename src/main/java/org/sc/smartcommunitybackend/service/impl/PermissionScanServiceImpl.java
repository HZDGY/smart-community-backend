package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.sc.smartcommunitybackend.common.annotation.RequirePermission;
import org.sc.smartcommunitybackend.domain.SysPermission;
import org.sc.smartcommunitybackend.dto.response.PermissionScanResult;
import org.sc.smartcommunitybackend.mapper.SysPermissionMapper;
import org.sc.smartcommunitybackend.service.PermissionScanService;
import org.sc.smartcommunitybackend.util.PermissionNameGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 权限扫描服务实现类
 */
@Service
public class PermissionScanServiceImpl implements PermissionScanService {
    
    private static final Logger logger = LoggerFactory.getLogger(PermissionScanServiceImpl.class);
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @Autowired
    private SysPermissionMapper permissionMapper;
    
    @Override
    @Transactional
    public PermissionScanResult scanAndSavePermissions() {
        logger.info("开始扫描权限注解...");
        
        PermissionScanResult result = new PermissionScanResult();
        
        // 1. 获取所有 Controller Bean
        Map<String, Object> controllers = getAllControllers();
        result.setControllerCount(controllers.size());
        logger.info("找到 {} 个 Controller", controllers.size());
        
        // 2. 扫描所有方法上的 @RequirePermission 注解
        ScanContext context = scanPermissionAnnotations(controllers);
        result.setMethodCount(context.methodCount);
        result.setTotalScanned(context.permissionCodes.size());
        logger.info("扫描到 {} 个方法，共 {} 个权限", context.methodCount, context.permissionCodes.size());
        
        // 3. 保存到数据库
        SaveResult saveResult = savePermissions(context.permissionCodes);
        result.setNewAdded(saveResult.newPermissions.size());
        result.setAlreadyExists(saveResult.existingPermissions.size());
        result.setNewPermissions(saveResult.newPermissions);
        result.setExistingPermissions(saveResult.existingPermissions);
        
        logger.info("权限扫描完成：新增 {} 个，已存在 {} 个", 
                result.getNewAdded(), result.getAlreadyExists());
        
        return result;
    }
    
    /**
     * 获取所有 Controller Bean
     */
    private Map<String, Object> getAllControllers() {
        Map<String, Object> controllers = new HashMap<>();
        
        // 获取所有 @RestController 注解的 Bean
        Map<String, Object> restControllers = applicationContext.getBeansWithAnnotation(RestController.class);
        controllers.putAll(restControllers);
        
        // 获取所有 @Controller 注解的 Bean
        Map<String, Object> normalControllers = applicationContext.getBeansWithAnnotation(Controller.class);
        controllers.putAll(normalControllers);
        
        return controllers;
    }
    
    /**
     * 扫描权限注解
     */
    private ScanContext scanPermissionAnnotations(Map<String, Object> controllers) {
        Set<String> permissionCodes = new HashSet<>();
        int methodCount = 0;
        
        for (Map.Entry<String, Object> entry : controllers.entrySet()) {
            Object controller = entry.getValue();
            Class<?> clazz = controller.getClass();
            
            // 获取原始类（处理 CGLIB 代理）
            Class<?> targetClass = getTargetClass(clazz);
            
            // 获取所有方法
            Method[] methods = targetClass.getDeclaredMethods();
            
            for (Method method : methods) {
                methodCount++;
                
                // 检查是否有 @RequirePermission 注解
                RequirePermission annotation = method.getAnnotation(RequirePermission.class);
                
                if (annotation != null) {
                    // 提取权限编码
                    String[] codes = annotation.value();
                    for (String code : codes) {
                        if (code != null && !code.trim().isEmpty()) {
                            permissionCodes.add(code.trim());
                            logger.debug("发现权限：{} 在方法 {}.{}", 
                                    code, targetClass.getSimpleName(), method.getName());
                        }
                    }
                }
            }
        }
        
        ScanContext context = new ScanContext();
        context.permissionCodes = permissionCodes;
        context.methodCount = methodCount;
        return context;
    }
    
    /**
     * 获取目标类（处理 CGLIB 代理）
     */
    private Class<?> getTargetClass(Class<?> clazz) {
        // 如果是 CGLIB 代理类，获取父类
        if (clazz.getName().contains("$$")) {
            return clazz.getSuperclass();
        }
        return clazz;
    }
    
    /**
     * 保存权限到数据库
     */
    private SaveResult savePermissions(Set<String> permissionCodes) {
        List<String> newPermissions = new ArrayList<>();
        List<String> existingPermissions = new ArrayList<>();
        
        for (String code : permissionCodes) {
            // 检查是否已存在
            LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysPermission::getPermissionCode, code);
            SysPermission existing = permissionMapper.selectOne(wrapper);
            
            if (existing == null) {
                // 不存在，创建新权限
                SysPermission permission = new SysPermission();
                permission.setPermissionCode(code);
                permission.setPermissionName(PermissionNameGenerator.generateName(code));
                permission.setDescription(PermissionNameGenerator.generateDescription(code));
                permission.setResourceType("api");
                permission.setStatus(1);
                permission.setCreateTime(new Date());
                permission.setUpdateTime(new Date());
                
                permissionMapper.insert(permission);
                newPermissions.add(code);
                logger.info("新增权限：{} ({})", code, permission.getPermissionName());
            } else {
                existingPermissions.add(code);
                logger.debug("权限已存在：{}", code);
            }
        }
        
        SaveResult result = new SaveResult();
        result.newPermissions = newPermissions;
        result.existingPermissions = existingPermissions;
        return result;
    }
    
    /**
     * 扫描上下文
     */
    private static class ScanContext {
        Set<String> permissionCodes;
        int methodCount;
    }
    
    /**
     * 保存结果
     */
    private static class SaveResult {
        List<String> newPermissions;
        List<String> existingPermissions;
    }
}
