

# Harmony AI Code Service

## 项目简介

Harmony AI Code Service 是一个基于 Spring Boot 框架构建的企业级后端服务项目，采用标准的分层架构设计，实现了用户管理等核心功能。项目集成了 MyBatis 持久层框架，提供 RESTful API 接口，支持跨域配置，并具备完善的异常处理机制。

## 技术栈

- **核心框架**: Spring Boot 2.x
- **持久层**: MyBatis + MyBatis-Plus
- **数据库**: MySQL 5.7+ / 8.0+
- **构建工具**: Maven
- **API 文档**: Swagger (可选集成)
- **日志**: Lombok @Slf4j

## 项目结构

```
harmony-ai-code/
├── src/main/java/com/harmony/harmonyaicodeservice/
│   ├── HarmonyAiCodeServiceApplication.java    # 启动类
│   ├── common/                                  # 公共组件
│   │   ├── BaseResponse.java                   # 统一响应封装
│   │   ├── PageRequest.java                    # 分页请求参数
│   │   ├── DeleteRequest.java                  # 删除请求参数
│   │   └── ResultUtils.java                    # 响应工具类
│   ├── config/                                 # 配置类
│   │   └── CorsConfig.java                     # 跨域配置
│   ├── controller/                             # 控制器层
│   │   ├── HeatherController.java              # 测试接口
│   │   └── UserController.java                 # 用户管理接口
│   ├── exception/                              # 异常处理
│   │   ├── BusinessException.java              # 业务异常
│   │   ├── ErrorCode.java                      # 错误码枚举
│   │   ├── GlobalExceptionHandler.java         # 全局异常处理器
│   │   └── ThrowUtils.java                     # 异常抛出工具
│   ├── generator/                              # 代码生成器
│   │   └── MyBatisCodeGenerator.java           # MyBatis代码生成器
│   ├── mapper/                                 # 数据访问层
│   │   └── UserMapper.java                     # 用户Mapper接口
│   ├── model/                                  # 数据模型
│   │   └── entity/
│   │       └── User.java                       # 用户实体类
│   └── service/                                # 业务逻辑层
│       ├── UserService.java                    # 用户服务接口
│       └── impl/
│           └── UserServiceImpl.java            # 用户服务实现
├── src/main/resources/
│   ├── application.yml                         # 应用配置
│   └── mapper/                                 # MyBatis XML映射
│       └── UserMapper.xml
├── sql/
│   └── create_table.sql                        # 数据库初始化脚本
├── pom.xml                                     # Maven配置
└── README.md                                   # 项目文档
```

## 核心功能

### 用户管理模块
- **用户注册**: 创建新用户账户
- **用户查询**: 获取用户详细信息
- **用户更新**: 修改用户资料
- **用户删除**: 删除指定用户
- **用户列表**: 获取所有用户列表
- **分页查询**: 支持分页和排序的用户列表

### 系统功能
- **健康检查**: Heather 接口用于服务健康监测
- **统一响应**: 标准化 API 响应格式
- **异常处理**: 全局统一的异常捕获与处理
- **跨域支持**: 配置化的 CORS 跨域资源共享

## 快速开始

### 环境要求
- JDK 1.8 或更高版本
- Maven 3.6+
- MySQL 5.7+

### 数据库配置

1. 创建数据库:
```sql
CREATE DATABASE harmony_ai_code DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 执行初始化脚本:
```bash
mysql -u username -p harmony_ai_code < sql/create_table.sql
```

### 项目构建

```bash
# 克隆项目
git clone https://gitee.com/Harmony_TL/harmony-ai-code.git

# 进入项目目录
cd harmony-ai-code

# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run
```

### 配置说明

在 `src/main/resources/application.yml` 中配置数据库连接:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/harmony_ai_code?useUnicode=true&characterEncoding=utf8mb4
    username: your_username
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

## API 接口文档

### 基础信息
- **Base URL**: `http://localhost:8080`
- **Content-Type**: `application/json`

### 接口列表

#### 1. 健康检查
```http
GET /heather
```
**响应示例**:
```json
{
  "code": 0,
  "data": "success",
  "message": "ok"
}
```

#### 2. 用户管理

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/user/save` | 创建/保存用户 |
| DELETE | `/user/remove/{id}` | 删除用户 |
| PUT | `/user/update` | 更新用户信息 |
| GET | `/user/list` | 获取用户列表 |
| GET | `/user/getInfo/{id}` | 获取用户详情 |
| GET | `/user/page` | 分页查询用户 |

#### 统一响应格式

**成功响应**:
```json
{
  "code": 0,
  "data": { ... },
  "message": "ok"
}
```

**错误响应**:
```json
{
  "code": 40001,
  "data": null,
  "message": "请求参数错误"
}
```

## 核心类说明

### BaseResponse<T>
统一响应包装类，包含:
- `code`: 状态码 (0 表示成功)
- `data`: 响应数据
- `message`: 提示信息

### ErrorCode
错误码枚举，定义系统级错误码:
- `SUCCESS(0, "ok")`
- `PARAMS_ERROR(40000, "请求参数错误")`
- `NOT_LOGIN_ERROR(40100, "未登录")`
- `NO_AUTH_ERROR(40300, "无权限")`
- `NOT_FOUND_ERROR(40400, "请求数据不存在")`
- `SYSTEM_ERROR(50000, "系统错误")`

### ThrowUtils
异常抛出工具类:
- `throwIf(condition, errorCode)`: 条件为真时抛出业务异常
- `throwIf(condition, errorCode, message)`: 带自定义消息的异常抛出

## 代码生成器

项目内置 MyBatis 代码生成器，通过 `MyBatisCodeGenerator.java` 可快速生成:
- Entity 实体类
- Mapper 接口
- Service 接口及实现
- Controller 控制器

## 许可证

本项目采用 MIT License 开源协议。