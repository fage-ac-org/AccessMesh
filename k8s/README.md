# RuoYi Cloud Plus - Kubernetes 部署配置

本目录当前仅保留默认部署所需的三个核心服务清单。

| 服务 | Deployment 名称 | 容器名 | 端口 |
|------|-----------------|--------|------|
| gateway | ruoyi-gateway | gateway | 8080 |
| auth | ruoyi-auth | auth | 9210 |
| permission-center | ruoyi-permission-center | permission-center | 9320 |

## 必须替换的配置

- **镜像仓库用户名**：所有 `*-deployment.yaml` 中的 `docker.io/<DOCKER_HUB_USERNAME>/ruoyi-xxx:latest` 需将 `<DOCKER_HUB_USERNAME>` 替换为实际 Docker Hub 用户名（与 CI 中 `secrets.DOCKER_HUB_USERNAME` 一致）。若使用带 tag 的镜像，将 `:latest` 改为实际 tag（如 CI 生成的时间戳 tag）。

## 未知/需按环境补充的配置

以下配置未在 workflow 中体现，需根据实际环境自行补充：

1. **镜像拉取**  
   - 若使用私有仓库，在 Deployment 的 `spec.template.spec` 下增加 `imagePullSecrets`，并事先创建对应 Secret（如 `docker-hub-secret`）。

2. **副本数**  
   - 当前各 Deployment 的 `replicas: 1` 为示例值，生产环境可按需要调整（如 gateway 多副本）。

3. **资源限制**  
   - `resources.requests/limits` 为示例，请按实际压测与集群规划调整 CPU/内存。

4. **应用配置（Nacos / Redis / DB 等）**  
   - 未在 YAML 中配置 Nacos 地址、Redis、数据库等。可通过以下方式之一注入：  
     - 在对应 Deployment 的 `containers[].env` 中增加环境变量（如 `NACOS_SERVER_ADDR`、`SPRING_PROFILES_ACTIVE` 等）；  
     - 或使用 ConfigMap/Secret，并通过 `envFrom` 或 `valueFrom` 引用。  
   - 具体变量名需与各模块 `application*.yml` 及 Nacos 配置保持一致。

5. **健康检查**  
   - 所有探针使用 ` /actuator/health`。若项目未启用 Spring Boot Actuator 或路径不同，需修改 `livenessProbe`/`readinessProbe` 的 `path` 或端口。

6. **对外暴露**
   - Service 均为 `ClusterIP`。若需公网访问网关，可：  
     - 将 `ruoyi-gateway` 的 Service 改为 `LoadBalancer`，或  
     - 配置 Ingress 指向 `ruoyi-gateway:8080`。  
   - `auth`、`permission-center` 若需对外直连调试，可按需改为 NodePort/LoadBalancer 或通过 Ingress 暴露。

7. **依赖服务**
   - 若 Nacos、Redis、MySQL 等也部署在 K8s 中，需自行编写对应 Deployment/Service 或 Helm，并确保各微服务能通过 K8s 服务名（如 `nacos.ruoyi.svc:8848`）访问。

## 部署顺序建议

1. 创建 namespace：`kubectl apply -f namespace.yaml`
2. 若有 Nacos/Redis/DB 等，先部署并确认可访问。
3. 按依赖顺序部署各微服务（建议先 auth、permission-center，再 gateway）。
4. 一次性应用当前目录下所有配置示例：
   ```bash
   kubectl apply -f namespace.yaml
   kubectl apply -f k8s/
   ```
   或按服务逐个：`kubectl apply -f gateway-deployment.yaml -f gateway-service.yaml` 等。

## 与 CI 的对应关系

- 命名空间：`ruoyi`（与 workflow 中 `-n ruoyi` 一致）。  
- 更新镜像时，CI 执行的是：  
  `kubectl set image deployment/<DEPLOY> <SERVICE>=<IMAGE>:<TAG> -n ruoyi`  
  其中 `<DEPLOY>` 为表中「Deployment 名称」，`<SERVICE>` 为「容器名」，需与 YAML 中 `metadata.name` 和 `containers[].name` 一致，本目录配置已对齐。
