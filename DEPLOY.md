# RuoYi-Cloud-Plus 部署文档

## 1 项目概述

RuoYi-Cloud-Plus 是基于 Dromara 生态的微服务权限管理系统。

| 项目信息 | 说明 |
|---------|------|
| 版本 | 2.5.2 |
| 组织 | Dromara |
| 官方文档 | https://plus-doc.dromara.org |

### 技术栈

| 技术领域 | 技术选型 |
|---------|---------|
| Java版本 | JDK 17 / JDK 21 |
| Spring Boot | 3.5.9 |
| Spring Cloud | 2025.0.1 |
| Web容器 | Undertow |
| ORM框架 | Mybatis-Plus 3.5.14 |
| 缓存客户端 | Redisson 3.52.0 |
| 权限认证 | Sa-Token + JWT 1.44.0 |
| RPC框架 | Apache Dubbo 3.X |
| 消息队列 | RocketMQ / Kafka / RabbitMQ |
| 工作流 | Warm-Flow 1.8.4 |
| 任务调度 | SnailJob 1.9.0 |
| 搜索引擎 | ElasticSearch + Easy-Es |

---

## 2 服务模块说明

### 2.1 核心服务模块

| 模块 | 端口 | 功能描述 |
|-----|------|---------|
| **ruoyi-gateway** | 8080 | API网关：路由转发、请求体缓存、跨域配置、请求响应日志、内网鉴权 |
| **ruoyi-auth** | 9210 | 认证中心：登录授权、Token管理、OAuth2认证、第三方登录绑定 |
| **ruoyi-system** | 9201 | 系统管理：用户、角色、部门、岗位、菜单、字典、参数、通知公告等 |
| **ruoyi-resource** | 9204 | 资源服务：文件上传/下载、OSS配置、短信服务、邮件服务 |
| **ruoyi-gen** | 9202 | 代码生成：多数据源代码生成、CRUD自动生成、前后端代码生成 |
| **ruoyi-job** | 9203 | 定时任务：任务管理、执行器管理、任务日志、分布式任务调度 |
| **ruoyi-workflow** | 9205 | 工作流：审批流程、转办、委派、加减签、会签等复杂流程 |

### 2.2 可视化模块

| 模块 | 端口 | 功能描述 |
|-----|------|---------|
| **ruoyi-monitor** | 9100 | 服务监控：CPU、内存、磁盘、堆栈、在线日志、Spring配置监控 |
| **ruoyi-snailjob-server** | 8800/17888 | 分布式任务调度：分片任务、DAG任务流、重试机制 |
| **ruoyi-seata-server** | 7091/8091 | 分布式事务：AT模式、TCC模式、Saga模式 |

### 2.3 模块关系说明

```
┌─────────────────────────────────────────────────────────────┐
│                      ruoyi-gateway (8080)                   │
│                      API网关 - 路由转发                      │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      ruoyi-auth (9210)                      │
│                      认证中心 - 登录授权                     │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                    ruoyi-system (9201)                      │
│                    系统管理 - 用户权限管理                   │
└───────────────────────┬─────────────────────────────────────┘
                        │
        ┌───────────────┼───────────────┐
        ▼               ▼               ▼
┌──────────────┐ ┌──────────────┐ ┌──────────────┐
│ ruoyi-gen    │ │ ruoyi-job    │ │ ruoyi-workflow│
│ 代码生成器   │ │ 定时任务     │ │ 工作流引擎   │
└──────────────┘ └──────────────┘ └──────────────┘
                        │
                        ▼
┌─────────────────────────────────────────────────────────────┐
│               ruoyi-snailjob-server (8800)                  │
│               任务调度控制台 - 任务管理界面                  │
└─────────────────────────────────────────────────────────────┘
```

---

## 3 环境要求

### 3.1 基础环境

| 组件 | 版本要求 | 说明 |
|-----|---------|------|
| JDK | 17 / 21 | 建议使用JDK 21 |
| Maven | 3.6+ | 构建工具 |
| MySQL | 8.0+ | 主数据库 |
| Redis | 7.0+ | 缓存 |
| Nacos | 2.3+ | 服务注册与配置中心 |

### 3.2 可选组件

| 组件 | 用途 | 端口 |
|-----|------|------|
| MinIO | 对象存储 | 9000/9001 |
| Elasticsearch | 搜索引擎 | 9200/9300 |
| RocketMQ | 消息队列 | 9876/10911 |
| RabbitMQ | 消息队列 | 5672/15672 |
| Kafka | 消息队列 | 9092/9093 |
| Seata | 分布式事务 | 7091/8091 |
| SnailJob | 任务调度 | 8800/17888 |
| SkyWalking | 链路追踪 | 11800/12800/18080 |
| Prometheus | 监控 | 9090 |
| Grafana | 监控面板 | 3000 |

---

## 4 Docker Compose 部署

### 4.1 环境准备

```bash
# 克隆项目
git clone https://gitee.com/dromara/RuoYi-Cloud-Plus.git
cd RuoYi-Cloud-Plus

# 确保 Docker 和 Docker Compose 已安装
docker --version
docker-compose --version
```

### 4.2 数据库初始化

```sql
-- 创建数据库
CREATE DATABASE ry-cloud DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE ry-job DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE ry-workflow DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 导入SQL脚本
source sql/ry_cloud_20241010.sql;
source sql/ry_job_20241010.sql;
source sql/ry_workflow_20241010.sql;
```

### 4.3 Nacos配置

1. 启动 Nacos 服务 (默认端口 8848)
2. 访问 http://localhost:8848/nacos (admin/admin)
3. 在 **配置管理 > 配置列表** 中导入 `script/config/nacos/` 下的所有 .yml 文件

### 4.4 修改数据库连接

编辑 `script/config/nacos/datasource.yml`：

```yaml
datasource:
  system-master:
    url: jdbc:mysql://localhost:3306/ry-cloud?useUnicode=true&characterEncoding=utf8
    username: root
    password: your_password
```

### 4.5 启动服务

```bash
cd script/docker

# 创建挂载目录
mkdir -p /docker/mysql/data
mkdir -p /docker/mysql/conf
mkdir -p /docker/nacos/logs
mkdir -p /docker/nacos/conf
mkdir -p /docker/redis/data
mkdir -p /docker/redis/conf
mkdir -p /docker/minio/data
mkdir -p /docker/minio/config
mkdir -p /docker/ruoyi-*/logs

# 启动所有服务
docker-compose up -d

# 查看启动日志
docker-compose logs -f

# 查看服务状态
docker-compose ps
```

### 4.6 服务访问地址

| 服务 | 地址 | 说明 |
|-----|------|------|
| API网关 | http://localhost:8080 |  |
| 认证服务 | http://localhost:9210 |  |
| 系统接口文档 | http://localhost:9201/doc.html |  |
| 资源接口文档 | http://localhost:9204/doc.html |  |
| 服务监控 | http://localhost:9100/monitor | admin/admin |
| Nacos控制台 | http://localhost:8848/nacos | admin/admin |
| SnailJob | http://localhost:17888 | admin/admin |

---

## 5 Kubernetes 部署

### 5.1 集群要求

| 组件 | 版本要求 |
|-----|---------|
| Kubernetes | 1.20+ |
| Helm | 3.0+ |
| Ingress Controller | nginx-ingress |

### 5.2 命名空间创建

```bash
# 创建命名空间
kubectl create namespace ruoyi

# 查看命名空间
kubectl get namespace ruoyi
```

### 5.3 MySQL 部署

```yaml
# mysql.yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: mysql-pvc
  namespace: ruoyi
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 20Gi
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: mysql
  namespace: ruoyi
spec:
  replicas: 1
  selector:
    matchLabels:
      app: mysql
  template:
    metadata:
      labels:
        app: mysql
    spec:
      containers:
        - name: mysql
          image: mysql:8.0.42
          env:
            - name: MYSQL_ROOT_PASSWORD
              value: "ruoyi123"
            - name: MYSQL_DATABASE
              value: "ry-cloud"
          ports:
            - containerPort: 3306
          volumeMounts:
            - name: mysql-data
              mountPath: /var/lib/mysql
          resources:
            requests:
              memory: "512Mi"
              cpu: "500m"
            limits:
              memory: "2Gi"
              cpu: "2"
      volumes:
        - name: mysql-data
          persistentVolumeClaim:
            claimName: mysql-pvc
---
apiVersion: v1
kind: Service
metadata:
  name: mysql
  namespace: ruoyi
spec:
  selector:
    app: mysql
  ports:
    - port: 3306
      targetPort: 3306
  clusterIP: None
```

```bash
kubectl apply -f mysql.yaml
```

### 5.4 Redis 部署

```yaml
# redis.yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: redis-pvc
  namespace: ruoyi
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 5Gi
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: redis
  namespace: ruoyi
spec:
  replicas: 1
  selector:
    matchLabels:
      app: redis
  template:
    metadata:
      labels:
        app: redis
    spec:
      containers:
        - name: redis
          image: redis:7.2.8
          ports:
            - containerPort: 6379
          volumeMounts:
            - name: redis-data
              mountPath: /data
          resources:
            requests:
              memory: "256Mi"
              cpu: "200m"
            limits:
              memory: "1Gi"
              cpu: "1"
      volumes:
        - name: redis-data
          persistentVolumeClaim:
            claimName: redis-pvc
---
apiVersion: v1
kind: Service
metadata:
  name: redis
  namespace: ruoyi
spec:
  selector:
    app: redis
  ports:
    - port: 6379
      targetPort: 6379
  clusterIP: None
```

```bash
kubectl apply -f redis.yaml
```

### 5.5 Nacos 部署

```yaml
# nacos.yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: nacos-pvc
  namespace: ruoyi
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 2Gi
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nacos
  namespace: ruoyi
spec:
  replicas: 1
  selector:
    matchLabels:
      app: nacos
  template:
    metadata:
      labels:
        app: nacos
    spec:
      containers:
        - name: nacos
          image: ruoyi/ruoyi-nacos:2.5.2
          env:
            - name: TZ
              value: "Asia/Shanghai"
            - name: JAVA_OPTS
              value: "-Xms256m -Xmx512m"
          ports:
            - containerPort: 8848
            - containerPort: 9848
            - containerPort: 9849
          volumeMounts:
            - name: nacos-data
              mountPath: /root/nacos/data
            - name: nacos-logs
              mountPath: /root/nacos/logs
          resources:
            requests:
              memory: "512Mi"
              cpu: "500m"
            limits:
              memory: "1Gi"
              cpu: "1"
      volumes:
        - name: nacos-data
          persistentVolumeClaim:
            claimName: nacos-pvc
        - name: nacos-logs
          emptyDir: {}
---
apiVersion: v1
kind: Service
metadata:
  name: nacos
  namespace: ruoyi
spec:
  selector:
    app: nacos
  ports:
    - name: http
      port: 8848
      targetPort: 8848
    - name: grpc1
      port: 9848
      targetPort: 9848
    - name: grpc2
      port: 9849
      targetPort: 9849
  clusterIP: None
---
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: nacos-ingress
  namespace: ruoyi
  annotations:
    kubernetes.io/ingress.class: nginx
spec:
  rules:
    - host: nacos.ruoyi.local
      http:
        paths:
          - path: /
            pathType: Prefix
            backend:
              service:
                name: nacos
                port:
                  number: 8848
```

```bash
kubectl apply -f nacos.yaml
```

### 5.6 核心服务部署 (Helm)

```bash
# 创建 values.yaml
cat > values.yaml << EOF
global:
  imagePullPolicy: IfNotPresent
  imageRegistry: docker.io

replicaCount:
  gateway: 1
  auth: 1
  system: 1

service:
  type: ClusterIP
  gateway:
    port: 8080
  auth:
    port: 9210
  system:
    port: 9201

resources:
  gateway:
    requests:
      cpu: 500m
      memory: 512Mi
    limits:
      cpu: 1
      memory: 1Gi
  auth:
    requests:
      cpu: 500m
      memory: 512Mi
    limits:
      cpu: 1
      memory: 1Gi
  system:
    requests:
      cpu: 500m
      memory: 512Mi
    limits:
      cpu: 1
      memory: 1Gi

nacos:
  serverAddr: "nacos.ruoyi.local:8848"

redis:
  host: "redis.ruoyi.local"
  port: 6379

database:
  host: "mysql.ruoyi.local"
  port: 3306
  username: "root"
  password: "ruoyi123"
  name: "ry-cloud"
EOF

# 使用 Helm 部署 (需要提前编写 helm chart)
helm install ruoyi ./helm/ruoyi -f values.yaml -n ruoyi
```

### 5.7 K8s 完整部署清单

```yaml
# k8s-deployment.yaml
# 包含所有服务的 K8s 部署配置

---
# MySQL
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: mysql-pvc
  namespace: ruoyi
spec:
  accessModes: [ReadWriteOnce]
  resources:
    requests:
      storage: 20Gi
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: mysql
  namespace: ruoyi
spec:
  replicas: 1
  selector:
    matchLabels:
      app: mysql
  template:
    spec:
      containers:
        - image: mysql:8.0.42
          name: mysql
          env:
            - name: MYSQL_ROOT_PASSWORD
              value: "ruoyi123"
            - name: MYSQL_DATABASE
              value: "ry-cloud"
          ports:
            - containerPort: 3306
          volumeMounts:
            - name: mysql-data
              mountPath: /var/lib/mysql
          resources:
            requests:
              cpu: 500m
              memory: 512Mi
            limits:
              cpu: 2
              memory: 2Gi
      volumes:
        - name: mysql-data
          persistentVolumeClaim:
            claimName: mysql-pvc
---
apiVersion: v1
kind: Service
metadata:
  name: mysql
  namespace: ruoyi
spec:
  selector:
    app: mysql
  ports:
    - port: 3306
      targetPort: 3306

---
# Redis
apiVersion: apps/v1
kind: Deployment
metadata:
  name: redis
  namespace: ruoyi
spec:
  replicas: 1
  selector:
    matchLabels:
      app: redis
  template:
    spec:
      containers:
        - image: redis:7.2.8
          name: redis
          ports:
            - containerPort: 6379
          resources:
            requests:
              cpu: 200m
              memory: 256Mi
            limits:
              cpu: 1
              memory: 1Gi
---
apiVersion: v1
kind: Service
metadata:
  name: redis
  namespace: ruoyi
spec:
  selector:
    app: redis
  ports:
    - port: 6379
      targetPort: 6379

---
# Gateway
apiVersion: apps/v1
kind: Deployment
metadata:
  name: ruoyi-gateway
  namespace: ruoyi
spec:
  replicas: 2
  selector:
    matchLabels:
      app: ruoyi-gateway
  template:
    spec:
      containers:
        - image: ruoyi/ruoyi-gateway:2.5.2
          name: ruoyi-gateway
          ports:
            - containerPort: 8080
          env:
            - name: TZ
              value: "Asia/Shanghai"
          resources:
            requests:
              cpu: 500m
              memory: 512Mi
            limits:
              cpu: 1
              memory: 1Gi
---
apiVersion: v1
kind: Service
metadata:
  name: ruoyi-gateway
  namespace: ruoyi
spec:
  selector:
    app: ruoyi-gateway
  ports:
    - port: 80
      targetPort: 8080
  type: ClusterIP

---
# Auth
apiVersion: apps/v1
kind: Deployment
metadata:
  name: ruoyi-auth
  namespace: ruoyi
spec:
  replicas: 2
  selector:
    matchLabels:
      app: ruoyi-auth
  template:
    spec:
      containers:
        - image: ruoyi/ruoyi-auth:2.5.2
          name: ruoyi-auth
          ports:
            - containerPort: 9210
          env:
            - name: TZ
              value: "Asia/Shanghai"
          resources:
            requests:
              cpu: 500m
              memory: 512Mi
            limits:
              cpu: 1
              memory: 1Gi
---
apiVersion: v1
kind: Service
metadata:
  name: ruoyi-auth
  namespace: ruoyi
spec:
  selector:
    app: ruoyi-auth
  ports:
    - port: 80
      targetPort: 9210
  type: ClusterIP

---
# System
apiVersion: apps/v1
kind: Deployment
metadata:
  name: ruoyi-system
  namespace: ruoyi
spec:
  replicas: 2
  selector:
    matchLabels:
      app: ruoyi-system
  template:
    spec:
      containers:
        - image: ruoyi/ruoyi-system:2.5.2
          name: ruoyi-system
          ports:
            - containerPort: 9201
          env:
            - name: TZ
              value: "Asia/Shanghai"
          resources:
            requests:
              cpu: 500m
              memory: 512Mi
            limits:
              cpu: 1
              memory: 1Gi
---
apiVersion: v1
kind: Service
metadata:
  name: ruoyi-system
  namespace: ruoyi
spec:
  selector:
    app: ruoyi-system
  ports:
    - port: 80
      targetPort: 9201
  type: ClusterIP
```

```bash
# 部署到 K8s
kubectl apply -f k8s-deployment.yaml -n ruoyi

# 查看部署状态
kubectl get pods -n ruoyi

# 查看服务
kubectl get svc -n ruoyi

# 查看日志
kubectl logs -f ruoyi-gateway-xxx -n ruoyi
```

### 5.8 Ingress 配置

```yaml
# ingress.yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: ruoyi-ingress
  namespace: ruoyi
  annotations:
    kubernetes.io/ingress.class: nginx
    nginx.ingress.kubernetes.io/proxy-body-size: "100m"
    nginx.ingress.kubernetes.io/proxy-read-timeout: 300
    nginx.ingress.kubernetes.io/proxy-send-timeout: 300
spec:
  rules:
    - host: api.ruoyi.local
      http:
        paths:
          - path: /
            pathType: Prefix
            backend:
              service:
                name: ruoyi-gateway
                port:
                  number: 80
```

```bash
kubectl apply -f ingress.yaml -n ruoyi
```

---

## 6 最小化部署

### 6.1 必须启动的模块

| 服务 | 端口 | 说明 |
|------|------|------|
| MySQL | 3306 | 数据库 |
| Nacos | 8848 | 服务注册与配置中心 |
| Redis | 6379 | 缓存 |
| ruoyi-gateway | 8080 | API网关 |
| ruoyi-auth | 9210 | 认证服务 |
| ruoyi-system | 9201 | 系统服务 |

### 6.2 可选模块

| 模块 | 端口 | 建议 | 说明 |
|------|------|------|------|
| **ruoyi-snailjob-server** | 8800/17888 | 关闭 | 任务调度服务端，无需任务调度时可关闭 |
| **ruoyi-job** | 9203 | 关闭 | 任务执行器，无需定时任务时可关闭 |
| ruoyi-gen | 9202 | 关闭 | 代码生成器，开发环境才需要 |
| ruoyi-resource | 9204 | 关闭 | 文件服务，无需OSS/短信/邮件时可关闭 |
| ruoyi-workflow | 9205 | 关闭 | 工作流，无需审批流程时可关闭 |
| ruoyi-monitor | 9100 | 关闭 | 服务监控，可选功能 |
| MinIO | 9000/9001 | 关闭 | 对象存储，可用本地存储替代 |
| Seata | 7091/8091 | 关闭 | 分布式事务，无分布式事务需求时可关闭 |
| ES/Kibana | 9200/5601 | 关闭 | 日志搜索，无搜索需求时可关闭 |
| SkyWalking | 11800/12800/18080 | 关闭 | 链路追踪，可选监控 |
| Prometheus/Grafana | 9090/3000 | 关闭 | 监控告警，可选 |

### 6.3 Docker Compose 最小化配置

```yaml
# docker-compose-minimal.yml
services:
  mysql:
    image: mysql:8.0.42
    container_name: mysql
    environment:
      TZ: Asia/Shanghai
      MYSQL_ROOT_PASSWORD: ruoyi123
      MYSQL_DATABASE: ry-cloud
    ports:
      - "3306:3306"
    volumes:
      - /docker/mysql/data:/var/lib/mysql
    command:
      --default-authentication-plugin=mysql_native_password
      --character-set-server=utf8mb4
      --collation-server=utf8mb4_general_ci
    privileged: true
    network_mode: "host"

  nacos:
    image: ruoyi/ruoyi-nacos:2.5.2
    container_name: nacos
    ports:
      - "8848:8848"
      - "9848:9848"
      - "9849:9849"
    environment:
      TZ: Asia/Shanghai
      JAVA_OPTS: "-Xms256m -Xmx512m"
    volumes:
      - /docker/nacos/logs:/root/nacos/logs
      - /docker/nacos/conf/cluster.conf:/root/nacos/conf/cluster.conf
    network_mode: "host"

  redis:
    image: redis:7.2.8
    container_name: redis
    ports:
      - "6379:6379"
    environment:
      TZ: Asia/Shanghai
    volumes:
      - /docker/redis/conf:/redis/config
      - /docker/redis/data:/redis/data
    command: redis-server /redis/config/redis.conf
    privileged: true
    network_mode: "host"

  ruoyi-gateway:
    image: ruoyi/ruoyi-gateway:2.5.2
    container_name: ruoyi-gateway
    environment:
      TZ: Asia/Shanghai
    ports:
      - "8080:8080"
    volumes:
      - /docker/ruoyi-gateway/logs:/ruoyi/gateway/logs
    privileged: true
    network_mode: "host"

  ruoyi-auth:
    image: ruoyi/ruoyi-auth:2.5.2
    container_name: ruoyi-auth
    environment:
      TZ: Asia/Shanghai
    ports:
      - "9210:9210"
    volumes:
      - /docker/ruoyi-auth/logs:/ruoyi/auth/logs
    privileged: true
    network_mode: "host"

  ruoyi-system:
    image: ruoyi/ruoyi-system:2.5.2
    container_name: ruoyi-system
    environment:
      TZ: Asia/Shanghai
    ports:
      - "9201:9201"
    volumes:
      - /docker/ruoyi-system/logs:/ruoyi/system/logs
    privileged: true
    network_mode: "host"
```

```bash
# 启动最小化环境
docker-compose -f docker-compose-minimal.yml up -d

# 查看日志
docker-compose -f docker-compose-minimal.yml logs -f
```

---

## 7 配置文件说明

### 7.1 Nacos 配置文件

| 文件 | 说明 |
|-----|------|
| `script/config/nacos/application-common.yml` | 通用配置(Undertow、Dubbo、Redis、Sa-Token、Mybatis-Plus等) |
| `script/config/nacos/datasource.yml` | 数据源配置 |
| `script/config/nacos/ruoyi-gateway.yml` | 网关路由配置 |
| `script/config/nacos/ruoyi-auth.yml` | 认证服务配置 |
| `script/config/nacos/ruoyi-system.yml` | 系统模块数据源配置 |
| `script/config/nacos/ruoyi-gen.yml` | 代码生成配置 |
| `script/config/nacos/ruoyi-job.yml` | 定时任务配置 |
| `script/config/nacos/ruoyi-resource.yml` | 资源服务配置 |
| `script/config/nacos/ruoyi-workflow.yml` | 工作流配置 |

### 7.2 数据库列表

| 数据库 | 用途 |
|-------|------|
| `ry-cloud` | 系统核心数据 |
| `ry-job` | 定时任务数据 |
| `ry-workflow` | 工作流数据 |

### 7.3 连接池配置 (HikariCP)

```yaml
spring.datasource.dynamic.hikari:
  maxPoolSize: 20          # 最大连接池
  minIdle: 10              # 最小空闲
  connectionTimeout: 30000 # 获取连接超时
  maxLifetime: 1800000     # 连接最大生命周期
```

---

## 8 常见问题

### Q: Nacos 连接失败?

A: 检查 `script/config/nacos/application-common.yml` 中的 nacos 地址配置

### Q: Redis 连接被拒绝?

A: 确保 Redis 已启动，检查密码配置

### Q: 接口报 401?

A: 首次访问需先登录认证服务获取 token

### Q: Docker Compose 启动失败?

A: 检查端口是否被占用，查看日志确认具体错误

```bash
# 查看详细日志
docker-compose logs -f

# 检查端口占用
netstat -tlnp | grep -E "3306|6379|8848"
```

### Q: K8s Pod 无法启动?

A: 检查资源限制、镜像拉取、网络配置

```bash
# 查看 Pod 状态
kubectl get pods -n ruoyi

# 查看 Pod 事件
kubectl describe pod <pod-name> -n ruoyi

# 查看 Pod 日志
kubectl logs <pod-name> -n ruoyi
```

---

## 9 附录

### 9.1 默认账号密码

| 服务 | 用户名 | 密码 |
|-----|--------|------|
| 系统管理 | admin | admin123 |
| Nacos | admin | admin |
| SnailJob | admin | admin |
| Grafana | admin | admin |

### 9.2 Docker 镜像版本

| 服务 | 镜像版本 |
|-----|---------|
| Gateway | ruoyi/ruoyi-gateway:2.5.2 |
| Auth | ruoyi/ruoyi-auth:2.5.2 |
| System | ruoyi/ruoyi-system:2.5.2 |
| Gen | ruoyi/ruoyi-gen:2.5.2 |
| Job | ruoyi/ruoyi-job:2.5.2 |
| Resource | ruoyi/ruoyi-resource:2.5.2 |
| Workflow | ruoyi/ruoyi-workflow:2.5.2 |
| Monitor | ruoyi/ruoyi-monitor:2.5.2 |
| SnailJob | ruoyi/ruoyi-snailjob-server:2.5.2 |
| Nacos | ruoyi/ruoyi-nacos:2.5.2 |
| Seata | ruoyi/ruoyi-seata-server:2.5.2 |

### 9.3 官方文档

- 项目官网：https://www.dromara.org/
- 项目文档：https://plus-doc.dromara.org/
- 项目源码：https://gitee.com/dromara/RuoYi-Cloud-Plus
