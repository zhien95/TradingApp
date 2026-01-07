# Common 模块

此模块包含交易系统中所有微服务共享的通用组件，包括：

## 功能特性

1. **Avro 消息定义**：标准化的消息格式定义
2. **DTO 类**：数据传输对象
3. **枚举类型**：业务类型定义
4. **MapStruct 映射器**：Avro 与 DTO 之间的转换
5. **工具类**：序列生成、消息验证等

## 消息类型

- **Transaction**: 交易信息
- **PriceUpdate**: 价格更新
- **OrderUpdate**: 订单更新
- **RiskAlert**: 风险警报

## 使用方式

在其他微服务中添加依赖：

```xml
<dependency>
    <groupId>com.trading</groupId>
    <artifactId>common</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Avro Schema 规范

所有 Avro Schema 文件都定义了 `seqId` 字段用于保证消息顺序和去重。

## 枚举类型

使用枚举类型保证类型安全，避免无效值传入。

## 映射器

使用 MapStruct 自动生成 Avro 与 DTO 之间的转换代码，提高性能和类型安全性。