# Repository Guidelines

## 项目结构与模块组织
本仓库是 Maven 多模块微服务后端。根 `pom.xml` 默认聚合 `ruoyi-auth`、`ruoyi-gateway`、`ruoyi-modules`、`ruoyi-common`、`ruoyi-api`；当前默认运行拓扑已收敛为 `ruoyi-auth`、`ruoyi-gateway`、`ruoyi-modules/ruoyi-permission-center` 三个核心启动项目。`ruoyi-visual`、`ruoyi-example` 与其他历史业务模块仍保留在仓库中，但不再属于默认构建与默认部署集合。业务代码通常位于 `*/src/main/java`，配置与 SQL/脚本位于 `*/src/main/resources`、`script/`、`k8s/`。测试主要在 `*/src/test/java`，当前可参考 `ruoyi-modules/ruoyi-permission-center/src/test/java`。不要修改 `target/`、镜像产物或无关部署文件，除非任务明确要求。

## 构建、测试与开发命令
- `mvn clean install -DskipTests -Pdev`：构建全部模块，适合先验证依赖是否完整。
- `mvn -pl ruoyi-modules/ruoyi-permission-center -am test -DskipTests=false -Pdev`：运行指定模块及其依赖测试。
- `mvn -pl ruoyi-auth -am spring-boot:run -Pdev`：本地启动认证服务。
- `mvn -pl ruoyi-gateway -am package -DskipTests`：单独打包网关模块。

根 POM 默认 `skipTests=true`，代理在做验证时必须显式传 `-DskipTests=false`。

## 代码风格与命名约定
遵循 `.editorconfig`：Java 使用 4 空格缩进，`json/yml/yaml` 使用 2 空格，统一 UTF-8 与 LF。Java 包名全小写，类名使用 PascalCase，方法和变量使用 camelCase，测试类以 `*Test` 结尾。优先沿用现有模块边界和 Spring/Maven 组织方式，不做无关重构，不批量格式化未触及文件。

## 测试约定
测试通过 Maven Surefire 执行，并按 `profiles.active` 选择 `@Tag` 分组；默认 profile 为 `dev`。新增逻辑优先补充模块内单元测试，保持测试文件贴近被测类，例如 `service/impl/...Test`、`controller/...Test`。提交前至少运行受影响模块测试；如果跳过测试，需在说明中写明原因和风险。

## 提交与 Pull Request 约定
近期提交遵循 Conventional Commits，常见格式为 `feat(scope): 描述`，例如 `feat(permission-center): nacos镜像问题修复`。建议继续使用 `feat`、`fix`、`refactor`、`test` 等类型，并附带明确 scope。PR 说明至少包含：变更目的、影响模块、配置或数据库变更、测试命令与结果；涉及接口、部署或可视化变更时补充截图或示例。

## 代码代理专用说明
先读取目标模块的 `pom.xml`、`src/main/resources` 与相邻测试，再动手修改。优先做最小闭环改动：只改与任务相关的模块、脚本和文档；不要顺手清理历史代码；不要覆盖用户已有未提交改动。若需要新增配置，优先复用现有 profile、Nacos 和脚本目录约定。
