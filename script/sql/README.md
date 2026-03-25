# SQL 目录说明

当前默认交付拓扑只包含：

- `ruoyi-gateway`
- `ruoyi-auth`
- `ruoyi-permission-center`

默认初始化脚本：

- `ry-cloud.sql`：三核心服务当前默认使用的业务库脚本
- `ry-config.sql`：Nacos 配置库初始化脚本，默认只包含 `application-common.yml`、`datasource.yml`、`ruoyi-gateway.yml`、`ruoyi-auth.yml`、`ruoyi-permission-center.yml`

非默认脚本：

- `ry-job.sql`
- `ry-workflow.sql`
- `ry-seata.sql`
- `update/`
- `oracle/`
- `postgres/`

以上文件仅保留为历史能力、异构数据库或升级迁移参考，不再属于默认三核心部署步骤。
