# 将此文件夹下默认保留的配置文件内容复制到 `nacos` 对应的配置中

当前默认运行拓扑只包含三个核心服务：

- `ruoyi-gateway`
- `ruoyi-auth`
- `ruoyi-permission-center`

默认需要导入的配置文件：

- `application-common.yml`
- `datasource.yml`
- `ruoyi-gateway.yml`
- `ruoyi-auth.yml`
- `ruoyi-permission-center.yml`

其余历史模块配置已从默认部署集合移除，如需恢复对应模块，请按模块边界重新补充配置。
