# 项目结构说明

## 项目概述
这是一个标准的企业级Spring Boot项目结构，适用于智能社区后端系统。

## 技术栈
- Spring Boot 3.5.9
- Java 17
- Spring Data JPA
- MySQL
- Lombok
- Maven

## 项目包结构

```
org.sc.smartcommunitybackend/
├── SmartCommunityBackendApplication.java    # 主启动类
│
├── common/                                   # 通用类
│   ├── Result.java                          # 统一响应结果封装
│   └── ResultCode.java                      # 响应结果码枚举
│
├── config/                                   # 配置类
│   ├── WebConfig.java                       # Web配置（跨域等）
│   └── JpaConfig.java                       # JPA配置
│
├── constant/                                 # 常量类
│   └── CommonConstant.java                  # 通用常量
│
├── controller/                               # 控制器层
│   └── BaseController.java                  # 基础控制器
│
├── dto/                                      # 数据传输对象
│   ├── BaseDTO.java                         # 基础DTO
│   ├── request/                              # 请求DTO
│   │   └── BaseRequest.java                 # 基础请求DTO
│   └── response/                             # 响应DTO
│       └── BaseResponse.java                # 基础响应DTO
│
├── entity/                                   # 实体类
│   └── BaseEntity.java                      # 基础实体类（包含通用字段）
│
├── exception/                                # 异常处理
│   ├── BusinessException.java               # 业务异常类
│   └── GlobalExceptionHandler.java          # 全局异常处理器
│
├── repository/                              # 数据访问层
│   └── BaseRepository.java                  # 基础Repository接口
│
├── service/                                  # 服务层
│   ├── BaseService.java                     # 基础服务接口
│   └── impl/                                 # 服务实现类
│       └── BaseServiceImpl.java             # 基础服务实现类
│
└── util/                                     # 工具类
    └── PageUtil.java                         # 分页工具类
```

## 核心功能说明

### 1. 统一响应结果（Result）
- 所有API接口统一返回`Result<T>`格式
- 包含code、message、data、timestamp字段
- 提供success和error静态方法

### 2. 全局异常处理（GlobalExceptionHandler）
- 统一处理业务异常、参数校验异常、运行时异常等
- 自动将异常转换为统一的响应格式

### 3. 基础实体类（BaseEntity）
- 包含id、createTime、updateTime、deleted字段
- 支持JPA审计（自动填充创建和更新时间）
- 支持逻辑删除

### 4. 基础服务层（BaseService）
- 提供通用的CRUD操作方法
- 支持分页查询
- 可被具体业务服务继承

### 5. 分页工具（PageUtil）
- 简化分页对象创建
- 提供分页结果封装

## 配置文件说明

### application.properties
- 主配置文件，包含默认配置

### application-dev.properties
- 开发环境配置
- 开启SQL日志，便于调试

### application-prod.properties
- 生产环境配置
- 关闭SQL日志，优化性能

## 使用示例

### 1. 创建实体类
```java
@Entity
@Table(name = "user")
public class User extends BaseEntity {
    private String username;
    private String email;
    // getter/setter
}
```

### 2. 创建Repository
```java
@Repository
public interface UserRepository extends BaseRepository<User, Long> {
    // 自定义查询方法
}
```

### 3. 创建Service
```java
@Service
public class UserService extends BaseServiceImpl<User, Long, UserRepository> {
    public UserService(UserRepository repository) {
        super(repository);
    }
    // 自定义业务方法
}
```

### 4. 创建Controller
```java
@RestController
@RequestMapping("/api/users")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    public Result<User> getUser(@PathVariable Long id) {
        return success(userService.findById(id).orElse(null));
    }
}
```

## 开发规范

1. **命名规范**
   - Controller: 以Controller结尾
   - Service: 以Service结尾
   - Repository: 以Repository结尾
   - Entity: 使用实体名称
   - DTO: Request/Response后缀

2. **包结构规范**
   - 严格按照分层架构组织代码
   - 每个业务模块可以创建子包

3. **异常处理**
   - 业务异常使用BusinessException
   - 参数校验使用@Valid注解
   - 全局异常处理器自动处理

4. **响应格式**
   - 所有接口统一返回Result格式
   - 使用BaseController提供的便捷方法

## 下一步开发建议

1. 根据业务需求创建具体的实体类、DTO、Service、Controller
2. 添加安全认证（Spring Security）
3. 添加API文档（Swagger/OpenAPI）
4. 添加缓存支持（Redis）
5. 添加消息队列支持（RabbitMQ/Kafka）
6. 添加定时任务支持（Spring Task）
7. 添加文件上传功能
8. 完善单元测试和集成测试

