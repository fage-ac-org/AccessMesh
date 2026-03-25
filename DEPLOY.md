# RuoYi-Cloud-Plus 部署文档

## 1. 默认交付拓扑

当前仓库默认只围绕 3 个核心启动项目构建、部署与运行：

| 服务 | 端口 | 角色 |
|------|------|------|
| `ruoyi-gateway` | `8080` | 统一入口、令牌校验、接口级权限拦截 |
| `ruoyi-auth` | `9210` | `identity-service`，负责认证、主体主数据、服务账号、委托证明 |
| `ruoyi-permission-center` | `9320` | `permission-center`，负责授权模型、快照、数据范围、扩展能力 |

以下模块不再属于默认交付拓扑：`ruoyi-system`、`ruoyi-resource`、`ruoyi-gen`、`ruoyi-job`、`ruoyi-workflow`、`ruoyi-visual/*`、`ruoyi-example/*`。

## 2. 基础依赖

默认三核心服务依赖以下基础设施：

| 组件 | 版本建议 | 说明 |
|------|----------|------|
| JDK | 17 / 21 | 建议 JDK 21 |
| Maven | 3.6+ | 构建工具 |
| MySQL | 8.0+ | 主数据库 |
| Redis | 7.0+ | 缓存 |
| Nacos | 2.3+ | 注册中心与配置中心 |

## 3. 数据与配置初始化

### 3.1 数据库初始化

默认只初始化一个业务库：

```sql
CREATE DATABASE ry-cloud DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

导入以下脚本：

```bash
mysql -uroot -p ry-cloud < script/sql/ry-cloud.sql
mysql -uroot -p nacos < script/sql/ry-config.sql
```

说明：

- `script/sql/ry-cloud.sql`：三核心服务当前默认使用的业务表结构。
- `script/sql/ry-config.sql`：默认 Nacos 配置数据占位，只包含 `application-common.yml`、`datasource.yml`、`ruoyi-gateway.yml`、`ruoyi-auth.yml`、`ruoyi-permission-center.yml`。
- `script/sql/ry-job.sql`、`script/sql/ry-workflow.sql`、`script/sql/ry-seata.sql` 仅保留为历史/可选脚本，不属于默认安装步骤。

### 3.2 Nacos 配置初始化

将 [script/config/nacos/README.md](/mnt/d/project/ruoyi-cloud-plus/script/config/nacos/README.md) 中列出的默认配置导入 Nacos：

- `application-common.yml`
- `datasource.yml`
- `ruoyi-gateway.yml`
- `ruoyi-auth.yml`
- `ruoyi-permission-center.yml`

至少需要修改 [script/config/nacos/datasource.yml](/mnt/d/project/ruoyi-cloud-plus/script/config/nacos/datasource.yml) 中的数据库连接信息。

## 4. 本地启动顺序

建议启动顺序：

1. MySQL / Redis / Nacos
2. `ruoyi-auth`
3. `ruoyi-permission-center`
4. `ruoyi-gateway`

参考命令：

```bash
mvn -pl ruoyi-auth -am spring-boot:run -Pdev
mvn -pl ruoyi-modules/ruoyi-permission-center -am spring-boot:run -Pdev
mvn -pl ruoyi-gateway -am spring-boot:run -Pdev
```

## 5. Docker Compose

默认 [script/docker/docker-compose.yml](/mnt/d/project/ruoyi-cloud-plus/script/docker/docker-compose.yml) 只包含三核心服务与基础依赖。

```bash
cd script/docker
docker compose up -d
```

如需扩展型组件，显式启用 `optional` profile：

```bash
docker compose --profile optional up -d
```

常用挂载目录：

```bash
mkdir -p /docker/mysql/data
mkdir -p /docker/mysql/conf
mkdir -p /docker/nacos/logs
mkdir -p /docker/nacos/conf
mkdir -p /docker/redis/data
mkdir -p /docker/redis/conf
mkdir -p /docker/ruoyi-gateway/logs
mkdir -p /docker/ruoyi-auth/logs
mkdir -p /docker/ruoyi-permission-center/logs
```

## 6. Kubernetes

当前 `k8s/` 目录仅保留三核心服务清单：

- [k8s/namespace.yaml](/mnt/d/project/ruoyi-cloud-plus/k8s/namespace.yaml)
- [k8s/auth-deployment.yaml](/mnt/d/project/ruoyi-cloud-plus/k8s/auth-deployment.yaml)
- [k8s/auth-service.yaml](/mnt/d/project/ruoyi-cloud-plus/k8s/auth-service.yaml)
- [k8s/permission-center-deployment.yaml](/mnt/d/project/ruoyi-cloud-plus/k8s/permission-center-deployment.yaml)
- [k8s/permission-center-service.yaml](/mnt/d/project/ruoyi-cloud-plus/k8s/permission-center-service.yaml)
- [k8s/gateway-deployment.yaml](/mnt/d/project/ruoyi-cloud-plus/k8s/gateway-deployment.yaml)
- [k8s/gateway-service.yaml](/mnt/d/project/ruoyi-cloud-plus/k8s/gateway-service.yaml)

部署顺序：

```bash
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/auth-deployment.yaml -f k8s/auth-service.yaml
kubectl apply -f k8s/permission-center-deployment.yaml -f k8s/permission-center-service.yaml
kubectl apply -f k8s/gateway-deployment.yaml -f k8s/gateway-service.yaml
```

环境变量、镜像仓库账号、Nacos/Redis/DB 连接等非固定项，按 [k8s/README.md](/mnt/d/project/ruoyi-cloud-plus/k8s/README.md) 补齐。

## 7. 验证清单

默认启动后至少验证：

- 网关健康检查：`http://localhost:8080/actuator/health`
- 认证服务健康检查：`http://localhost:9210/actuator/health`
- 权限中心健康检查：`http://localhost:9320/actuator/health`
- Nacos 控制台：`http://localhost:8848/nacos`

## 8. 历史资产说明

以下内容当前仅作为历史资料或后续按需恢复的参考，不再属于默认交付面：

- 非核心业务启动模块目录
- `ruoyi-visual/*`、`ruoyi-example/*`
- `script/sql` 下的历史可选脚本与升级脚本
- 旧拓扑对应的部署示例与镜像构建流程
