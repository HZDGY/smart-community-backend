# Swagger/OpenAPI 使用指南

## 概述

项目已集成 SpringDoc OpenAPI 3.x（适用于 Spring Boot 3.x），提供了完整的 API 文档功能。

## 访问地址

启动项目后，可以通过以下地址访问 Swagger UI：

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs (JSON)**: http://localhost:8080/api-docs
- **API Docs (YAML)**: http://localhost:8080/api-docs.yaml

## 配置说明

### 配置文件位置
- `src/main/resources/application.properties` - 主配置文件
- `src/main/java/org/sc/smartcommunitybackend/config/SwaggerConfig.java` - Swagger配置类

### 主要配置项

```properties
# API文档路径
springdoc.api-docs.path=/api-docs

# Swagger UI路径
springdoc.swagger-ui.path=/swagger-ui.html

# 是否启用Swagger UI
springdoc.swagger-ui.enabled=true

# 扫描的包路径
springdoc.packages-to-scan=org.sc.smartcommunitybackend.controller

# 匹配的路径
springdoc.paths-to-match=/**
```

## 常用注解

### Controller 层注解

#### @Tag
用于标记 Controller 类，定义 API 分组。

```java
@Tag(name = "用户管理", description = "用户相关的API接口")
@RestController
@RequestMapping("/api/users")
public class UserController {
    // ...
}
```

#### @Operation
用于标记方法，定义 API 操作信息。

```java
@Operation(
    summary = "获取用户信息",
    description = "根据用户ID获取用户详细信息",
    tags = {"用户管理"}
)
@GetMapping("/{id}")
public Result<User> getUser(@PathVariable Long id) {
    // ...
}
```

#### @Parameter
用于标记参数，定义参数信息。

```java
@Operation(summary = "查询用户列表")
@GetMapping("/list")
public Result<List<User>> list(
    @Parameter(description = "页码", example = "0")
    @RequestParam(defaultValue = "0") Integer page,
    
    @Parameter(description = "每页大小", example = "10")
    @RequestParam(defaultValue = "10") Integer size
) {
    // ...
}
```

#### @ApiResponse / @ApiResponses
用于定义响应信息。

```java
@Operation(summary = "创建用户")
@PostMapping
@ApiResponses({
    @ApiResponse(responseCode = "200", description = "创建成功"),
    @ApiResponse(responseCode = "400", description = "参数错误"),
    @ApiResponse(responseCode = "500", description = "服务器错误")
})
public Result<User> create(@RequestBody @Valid UserRequest request) {
    // ...
}
```

### DTO 层注解

#### @Schema
用于标记 DTO 类或字段，定义模型信息。

```java
@Schema(description = "用户请求DTO")
public class UserRequest {
    
    @Schema(description = "用户名", example = "zhangsan", required = true)
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    @Schema(description = "邮箱", example = "zhangsan@example.com")
    @Email(message = "邮箱格式不正确")
    private String email;
    
    @Schema(description = "年龄", example = "25", minimum = "1", maximum = "150")
    @Min(value = 1, message = "年龄必须大于0")
    @Max(value = 150, message = "年龄不能超过150")
    private Integer age;
}
```

#### @Hidden
用于隐藏不需要在文档中显示的类或方法。

```java
@Hidden  // 隐藏此接口
@GetMapping("/internal")
public Result<String> internal() {
    // ...
}
```

## 使用示例

### 完整的 Controller 示例

```java
package org.sc.smartcommunitybackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sc.smartcommunitybackend.common.Result;
import org.sc.smartcommunitybackend.dto.request.UserRequest;
import org.sc.smartcommunitybackend.dto.response.UserResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "用户管理", description = "用户相关的API接口")
public class UserController extends BaseController {

    @GetMapping("/{id}")
    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户详细信息")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "获取成功"),
        @ApiResponse(responseCode = "404", description = "用户不存在")
    })
    public Result<UserResponse> getUser(
            @Parameter(description = "用户ID", required = true, example = "1")
            @PathVariable Long id) {
        // 业务逻辑
        return success(userResponse);
    }

    @GetMapping
    @Operation(summary = "获取用户列表", description = "分页获取用户列表")
    public Result<List<UserResponse>> list(
            @Parameter(description = "页码", example = "0")
            @RequestParam(defaultValue = "0") Integer page,
            
            @Parameter(description = "每页大小", example = "10")
            @RequestParam(defaultValue = "10") Integer size) {
        // 业务逻辑
        return success(userList);
    }

    @PostMapping
    @Operation(summary = "创建用户", description = "创建新用户")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "创建成功"),
        @ApiResponse(responseCode = "400", description = "参数错误")
    })
    public Result<UserResponse> create(
            @Parameter(description = "用户信息", required = true)
            @RequestBody @Valid UserRequest request) {
        // 业务逻辑
        return success(userResponse);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新用户", description = "更新用户信息")
    public Result<UserResponse> update(
            @Parameter(description = "用户ID", required = true)
            @PathVariable Long id,
            
            @Parameter(description = "用户信息", required = true)
            @RequestBody @Valid UserRequest request) {
        // 业务逻辑
        return success(userResponse);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "根据用户ID删除用户")
    public Result<Void> delete(
            @Parameter(description = "用户ID", required = true)
            @PathVariable Long id) {
        // 业务逻辑
        return success();
    }
}
```

### DTO 示例

```java
package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.sc.smartcommunitybackend.dto.BaseDTO;

@Data
@Schema(description = "用户创建请求")
public class UserCreateRequest extends BaseRequest {
    
    @Schema(description = "用户名", example = "zhangsan", required = true, maxLength = 50)
    @NotBlank(message = "用户名不能为空")
    @Size(max = 50, message = "用户名长度不能超过50")
    private String username;
    
    @Schema(description = "邮箱", example = "zhangsan@example.com", required = true)
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
    
    @Schema(description = "密码", example = "123456", required = true, minLength = 6)
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, message = "密码长度不能少于6位")
    private String password;
}
```

```java
package org.sc.smartcommunitybackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.sc.smartcommunitybackend.dto.BaseDTO;

import java.time.LocalDateTime;

@Data
@Schema(description = "用户响应信息")
public class UserResponse extends BaseResponse {
    
    @Schema(description = "用户ID", example = "1")
    private Long id;
    
    @Schema(description = "用户名", example = "zhangsan")
    private String username;
    
    @Schema(description = "邮箱", example = "zhangsan@example.com")
    private String email;
    
    @Schema(description = "创建时间", example = "2024-01-01 12:00:00")
    private LocalDateTime createTime;
}
```

## 最佳实践

1. **为所有 Controller 添加 @Tag 注解**
   - 使用清晰的分组名称
   - 添加描述信息

2. **为所有接口方法添加 @Operation 注解**
   - 提供简洁的 summary
   - 添加详细的 description

3. **为参数添加 @Parameter 注解**
   - 提供参数描述
   - 设置示例值（example）
   - 标记必填参数（required = true）

4. **为 DTO 类添加 @Schema 注解**
   - 为类添加描述
   - 为字段添加描述和示例值
   - 设置字段约束（required, min, max 等）

5. **使用 @ApiResponses 定义响应**
   - 明确各种响应状态码的含义
   - 帮助前端开发者理解接口行为

6. **隐藏内部接口**
   - 使用 @Hidden 隐藏不需要对外暴露的接口

## 注意事项

1. Spring Boot 3.x 使用 SpringDoc OpenAPI，不是 Swagger2
2. 注解包路径为 `io.swagger.v3.oas.annotations.*`
3. 确保 DTO 类有合适的 getter/setter（使用 Lombok 的 @Data）
4. 参数校验注解（@Valid, @NotNull 等）会自动在文档中显示约束信息

## 测试接口

项目已包含一个测试 Controller (`TestController`)，可以通过以下接口测试 Swagger 功能：

- GET `/api/test/hello` - Hello接口
- GET `/api/test/echo/{message}` - 回显接口
- POST `/api/test/info` - 信息接口

访问 http://localhost:8080/swagger-ui.html 查看完整的 API 文档。

