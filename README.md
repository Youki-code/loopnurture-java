# LoopNurture 多模块微服务项目

本仓库基于 Spring Boot / Spring Cloud 微服务体系，按照 **领域驱动设计（DDD）+ 分层架构** 实现，当前包含两个业务模块：

| 模块 | 说明 | 端口 | 主要技术 |
|------|------|------|----------|
| spring-loopnurture-users-service | 用户与组织管理、认证授权 | 8081 | Spring Boot, JPA, JWT, Swagger | 
| spring-loopnurture-mail-service  | 邮件模板、发送规则与调度 | 8082 | Spring Boot, SendGrid, Quartz |

> 其余基础设施（配置中心、注册中心、网关等）可根据需要接入 Spring Cloud 组件，这里不做演示。

---

## 快速启动

### 前置环境
1. JDK 17+
2. Maven 3.8+
3. 本地数据库（默认使用 H2，如需持久化可改为 MySQL）
4. 可选：Docker（启动依赖服务）

### 单模块启动
```bash
# 在项目根目录（默认以 test 环境启动）
./mvnw -pl spring-loopnurture-users-service -am spring-boot:run -Dspring-boot.run.profiles=test
```
浏览器访问 `http://localhost:8081/swagger-ui.html` 即可查看接口。

### 全部模块启动
```bash
./mvnw clean install -DskipTests
# 分别运行（均为 test 环境）
./mvnw -pl spring-loopnurture-users-service spring-boot:run -Dspring-boot.run.profiles=test
./mvnw -pl spring-loopnurture-mail-service spring-boot:run -Dspring-boot.run.profiles=test
```

### 本地完整环境启动顺序
如果你需要在本地同时体验完整链路（配置中心 / 注册中心 / 网关 / 业务服务），**推荐按照如下顺序启动**：

| 步骤 | 模块 | 端口 | 备注 |
|------|------|------|------|
| 1 | spring-loopnurture-config-server | 8888 | 提供集中化配置；其他服务依赖 |
| 2 | spring-loopnurture-discovery-server | 8761 | 服务注册发现；依赖 config-server |
| 3 | spring-loopnurture-users-service | 8081 | 业务服务 |
| 3 | spring-loopnurture-mail-service | 8082 | 业务服务 |
| 4 | spring-loopnurture-api-gateway | 8080 | 对外网关；依赖 discovery-server |
| 5 | (可选) spring-loopnurture-admin-server | 9090 | 监控面板 |

示例启动脚本（dev 环境）：

```bash
# 依次打开新的终端窗口 / tmux pane（test 环境）
PROFILE=test
./mvnw -pl spring-loopnurture-config-server spring-boot:run -Dspring-boot.run.profiles=${PROFILE} &
sleep 15
./mvnw -pl spring-loopnurture-discovery-server spring-boot:run -Dspring-boot.run.profiles=${PROFILE} &
sleep 10
./mvnw -pl spring-loopnurture-users-service spring-boot:run -Dspring-boot.run.profiles=${PROFILE} &
./mvnw -pl spring-loopnurture-mail-service spring-boot:run -Dspring-boot.run.profiles=${PROFILE} &
./mvnw -pl spring-loopnurture-api-gateway spring-boot:run -Dspring-boot.run.profiles=${PROFILE} &
```

亦可一次性使用 Docker Compose：

```bash
# 仅启动必要依赖（config / discovery / gateway / users / mail）
docker compose up -d config-server discovery-server api-gateway users-service mail-service
```

### 运行环境（Profiles）

项目统一支持三套运行环境：

| Profile | 说明 | 激活方式 |
|---------|------|----------|
| dev（默认） | 本地开发，H2 内存库 | 无需额外参数 |
| test | 测试 / QA，使用测试数据库 | `-Dspring-boot.run.profiles=test` 或 `SPRING_PROFILES_ACTIVE=test` |
| prod | 生产运行，关闭 debug & swagger 等工具 | `-Dspring-boot.run.profiles=prod` 或 `SPRING_PROFILES_ACTIVE=prod` |

例如以 **prod** 模式启动 users-service：

```bash
./mvnw -pl spring-loopnurture-users-service spring-boot:run -Dspring-boot.run.profiles=prod
```

---

## 主要目录结构
```
|- spring-loopnurture-users-service
|  |- src/main/java
|     |- .../controller      # 接口层（Controller + DTO）
|     |- .../service         # 应用层（业务服务）
|     |- .../domain          # 领域层（模型、仓储接口）
|     |- .../infra           # 基础设施层（RepositoryImpl、PO、Mapper、Converter）
|  |- src/test/java          # 单元测试
|
|- spring-loopnurture-mail-service
   |- 结构同上，侧重邮件发送逻辑
```

---

## 规则约定（摘录）
* 禁止在代码中直接书写 **完整限定类名（FQN）**，必须通过 import 引用
* Service 类命名以 `*Service` 结尾，不定义接口，直接实现业务
* 数据访问调用链：`Service -> *Repository -> Jpa*Mapper -> Database`
* PO 禁止使用枚举字段，统一以基本类型存储；枚举转换在 Converter 处理
* 详见 `.cursor/rules/default-rules.mdc` 与 `.cursor/rules/mail-rule.mdc`

---

## 常见命令
| 目的 | 命令 |
|------|------|
| 编译全部模块 | `./mvnw clean install -DskipTests` |
| 只测试 users-service | `./mvnw -pl spring-loopnurture-users-service test` |
| 构建 Docker 镜像 | `./mvnw -P buildDocker` |

---

## 贡献
欢迎 PR / Issue 提交改进建议，遵循 Apache 2.0 许可证。 

# 在模块根或项目根执行
./mvnw clean package -DskipTests
# 然后重新启动 users-service
./mvnw -pl spring-loopnurture-users-service spring-boot:run -Dspring-boot.run.profiles=test 