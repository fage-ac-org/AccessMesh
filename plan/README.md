# 通用权限中心 - SQL 与设计文档

本目录包含通用权限中心的 PostgreSQL 表结构及详细设计文档，便于直接执行建表并交由 AI 或开发实现后端与管理端。

## 文件说明

| 文件 | 说明 |
|------|------|
| **permission_center_schema.sql** | 完整建表 DDL：15 张表、唯一约束与部分唯一索引、GIN 索引、表/列中文注释。无外键，按文件内顺序执行即可。 |
| **DESIGN.md** | 详细设计文档：概念、表清单、鉴权/授权流程、变更记录约定、管理端与接口建议，供 AI 开发项目使用。 |

## 使用方式

1. **建表**：在目标 PostgreSQL 库中按**文件内顺序**执行 `permission_center_schema.sql`（建议先建库或 schema，再执行）。建表顺序与计划 §11 一致：system_config → biz_domain → 基础实体表 → user_role/role_resource_permission → 配置表 → permission_change_log。
2. **开发**：将 `DESIGN.md` 与 `permission_center_schema.sql` 一并提供给 AI 或开发人员，作为实现权限中心后端、鉴权服务、管理端页面的依据。
3. **规划**：更完整的业务背景、表设计演进与核对要点（含 §13 Java 逻辑/鉴权性能/管理端便利性）见项目中的「通用权限中心表结构设计」规划文档。

## 表一览（15 张）

- 基础：system_config, biz_domain, abstract_user, abstract_role, operation_permission, resource_entity, permission_condition  
- 关联：user_role, role_resource_permission  
- 配置：domain_scope_config, domain_relation_config, domain_scope_binding, resource_dependency, permission_conflict_rule  
- 审计：permission_change_log  

所有表均含 tenant_id 及 created_by/updated_by/deleted_by/created_at/updated_at/deleted_at。
