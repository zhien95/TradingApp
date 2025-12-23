# 微服务交易系统

一个基于Spring Cloud的高性能微服务交易系统，采用分布式架构设计，支持高并发交易场景。

## 项目结构

```
trading-system/
├── common/                    # 公共模块
├── gateway/                   # API网关
├── order-service/             # 订单管理服务
├── matching-engine/           # 匹配引擎服务
├── account-service/           # 账户管理服务
├── market-data-service/       # 市场数据服务
├── risk-service/              # 风控服务
├── notification-service/      # 通知服务
├── scripts/                   # 系统管理脚本
├── docker-compose.yml         # 开发环境容器编排
├── docker-compose-prod.yml    # 生产环境容器编排
├── start-services.bat         # 一键启动微服务脚本
├── build-docker-images.bat    # 构建Docker镜像脚本
├── deploy-docker.bat          # Docker部署脚本
├── pom.xml                    # 根项目配置
└── README.md                  # 项目说明文档
```

## 技术栈

### 后端技术
- **Spring Boot 3.x** - 微服务框架
- **Spring Cloud 2023** - 微服务生态
- **Spring Cloud Gateway** - API网关
- **Spring Data JPA** - 数据访问
- **Spring Kafka** - 消息队列
- **MySQL 8.0** - 关系型数据库
- **Redis 7.x** - 缓存系统
- **Nacos/Eureka** - 服务注册发现

### 前端技术
- **React 18.x** - 组件化UI框架
- **TypeScript 4.x** - 类型安全
- **Ant Design 5.x** - UI组件库
- **ECharts 5.x** - 数据可视化
- **Socket.IO** - 实时通信

## 系统管理脚本

项目提供了多个管理脚本以简化开发和部署流程：

### 开发环境脚本
- `scripts/start-dev.bat` - 启动开发环境（所有服务在单独窗口中运行）
- `scripts/stop-services.bat` - 停止所有服务
- `scripts/health-check.bat` - 检查服务运行状态

### 构建和部署脚本
- `scripts/build-images.bat` - 构建所有服务的Docker镜像
- `scripts/deploy-prod.bat` - 部署到生产环境
- `scripts/manage-services.bat` - 统一管理控制台

## 模块说明

### 1. Gateway (API网关)
- 统一入口管理
- 请求路由和负载均衡
- 认证授权
- 限流熔断

### 2. Order Service (订单服务)
- 订单创建、修改、取消
- 订单状态管理
- 订单持久化
- 订单验证

### 3. Matching Engine (匹配引擎)
- 订单撮合算法
- 实时匹配处理
- 交易生成
- 高性能内存订单簿

### 4. Account Service (账户服务)
- 资金管理
- 持仓管理
- 账户信息查询
- 资产计算

### 5. Market Data Service (市场数据)
- 实时行情推送
- 历史数据服务
- WebSocket连接管理
- K线数据生成

### 6. Risk Service (风控服务)
- 风险控制检查
- 交易限制验证
- 异常交易监控
- 用户限额管理

### 7. Notification Service (通知服务)
- 交易结果通知
- 风控告警
- 系统消息推送
- 邮件/SMS通知

### 8. Common (公共模块)
- 公共实体类
- 枚举定义
- 工具类
- DTO对象

## 系统架构

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   Gateway       │    │  Discovery      │
│   React App     │───▶│   Service       │───▶│  Service        │
│                 │    │                 │    │  (Eureka/Nacos) │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                              │
            ┌─────────────────┼─────────────────┐
            │                 │                 │
    ┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐
    │   Order         │ │  Matching       │ │  Account        │
    │   Service       │ │  Engine         │ │  Service        │
    └─────────────────┘ └─────────────────┘ └─────────────────┘
            │                       │                 │
            └───────────────────────┼─────────────────┘
                                    │
    ┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐
    │  Market Data    │ │   Risk          │ │ Notification    │
    │  Service        │ │  Service        │ │  Service        │
    └─────────────────┘ └─────────────────┘ └─────────────────┘
            │                       │                 │
            ▼                       ▼                 ▼
    ┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐
    │   MySQL         │ │    Redis        │ │    Kafka        │
    │   Database      │ │   Cache         │ │   Message       │
    └─────────────────┘ └─────────────────┘ └─────────────────┘
```

## 快速开始

### 环境要求
- Java 21+
- Maven 3.8+
- Docker & Docker Compose
- MySQL 8.0
- Redis 7.x
- Kafka 3.x

### 使用管理控制台（推荐）

1. 克隆项目
```bash
git clone <repository-url>
cd trading-system
```

2. 运行管理控制台
```bash
cd scripts
manage-services.bat
```

3. 选择选项启动开发环境或部署到生产环境

### 手动启动步骤

1. 克隆项目
```bash
git clone <repository-url>
cd trading-system
```

2. 启动基础设施
```bash
docker-compose up -d
```

3. 编译项目
```bash
mvn clean install
```

4. 启动各服务（按顺序）
```bash
# 使用脚本启动开发环境
scripts/start-dev.bat
```

### API端点

- **网关服务**: `http://localhost:8080`
- **订单服务**: `http://localhost:8081`
- **账户服务**: `http://localhost:8083`
- **匹配引擎**: `http://localhost:8082`
- **市场数据**: `http://localhost:8084`
- **风控服务**: `http://localhost:8085`
- **通知服务**: `http://localhost:8086`

## 核心功能

### 订单管理
- 支持限价单、市价单
- 订单状态实时更新
- 部分成交处理

### 撮合引擎
- 价格时间优先算法
- 高性能内存撮合
- 实时交易生成

### 账户管理
- 资金冻结/解冻
- 持仓实时更新
- 资产统计

### 风险控制
- 单笔限额控制
- 日累计限额
- 异常交易监控

## 配置说明

### 服务配置
各服务的配置文件位于 `src/main/resources/application.yml`

### 数据库配置
- 订单服务数据库: `trading_order`
- 账户服务数据库: `trading_account`

### 消息队列
- Kafka Topic: `order-events`, `trade-events`, `market-data`

## 部署方案

### 开发环境部署
```bash
# 使用管理控制台
scripts/manage-services.bat
# 选择选项 1 启动开发环境
```

### 生产环境部署
```bash
# 使用管理控制台
scripts/manage-services.bat
# 选择选项 2 部署到生产环境
```

或直接运行部署脚本：
```bash
scripts/deploy-prod.bat
```

### 手动Docker部署
```bash
# 构建Docker镜像
scripts/build-images.bat

# 使用Docker Compose启动
docker-compose -f docker-compose-prod.yml up -d
```

## 监控与运维

### 健康检查
```bash
scripts/health-check.bat
```

### 查看日志
```bash
# 使用管理控制台
scripts/manage-services.bat
# 选择选项 6 查看日志
```

### 停止服务
```bash
scripts/stop-services.bat
```

## 安全性

- JWT Token认证
- API访问控制
- 敏感数据加密
- SQL注入防护

## 性能优化

- Redis缓存热点数据
- 数据库连接池优化
- 异步处理机制
- 消息队列解耦

## 开发规范

### 代码规范
- 使用Google Java Style Guide
- 统一的命名规范
- 详细的代码注释

### 提交规范
- Git提交信息遵循Conventional Commits
- 分支管理遵循Git Flow

## 贡献指南

1. Fork项目
2. 创建功能分支
3. 提交更改
4. 发起Pull Request

## 许可证

MIT License