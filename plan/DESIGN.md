# 通用权限中心 - 详细设计文档

本文档面向 AI 或开发人员实现权限中心后端与管理端，与 `permission_center_schema.sql` 及规划文档配套使用。

---

## 1. 概述与架构

### 1.1 设计原则

- **无数据库外键**：所有关联为逻辑 ID，由应用或 MQ 保证一致性。
- **租户隔离**：所有表带 `tenant_id`，查询必须带租户条件。
- **软删除**：统一使用 `deleted_at`，非 NULL 表示已删；唯一约束均带 `WHERE deleted_at IS NULL`。
- **审计字段**：每表含 `created_by`、`updated_by`、`deleted_by`、`created_at`、`updated_at`、`deleted_at`。

### 1.2 核心概念

| 概念 | 说明 |
|------|------|
| 业务域 (biz_domain) | 对权限对象分类，控制数据量与管理边界。 |
| 抽象用户 (abstract_user) | 对应具体业务的人/服务/第三方，通过 user_type 区分，无 biz_domain，通过角色关联到域。 |
| 抽象角色 (abstract_role) | 对应角色/组织/团队/职位等，属于某 biz_domain 或全局；与权限直接关联。 |
| 操作权限 (operation_permission) | 如 VIEW、EDIT、APPROVAL、AUDIT；用 binary_bit + inherit_mask 表示继承关系。 |
| 权限资源实体 (resource_entity) | 权限作用对象（菜单、报表、数据集等），支持树形；属于某域或全局。 |
| 用户-角色 (user_role) | 用户与角色多对多，可带 valid_from/valid_to。 |
| 角色-资源-操作 (role_resource_permission) | 角色对某资源在某操作上的授权，可带 can_manage、condition_id。 |

### 1.3 逻辑关系简图

```
biz_domain
    ├── abstract_role (biz_domain_id 可空=全局)
    ├── operation_permission (biz_domain_id 可空=全局)
    ├── resource_entity (biz_domain_id 可空=全局)
    ├── domain_scope_config / domain_relation_config / domain_scope_binding
    └── permission_conflict_rule (biz_domain_id 可空=全局)

abstract_user --[user_role]--> abstract_role
abstract_role --[role_resource_permission]--> resource_entity + operation_permission
resource_entity --[resource_dependency]--> resource_entity (依赖链，鉴权时展开)
role_resource_permission 可带 condition_id --> permission_condition
```

---

## 2. 表清单与用途

| 表名 | 用途 | 关键字段 |
|------|------|----------|
| system_config | 类型枚举 KV：user_type/role_type/resource_type 等 | tenant_id, biz_domain_id, config_key, type_value, name |
| biz_domain | 业务域 | tenant_id, code, name |
| abstract_user | 抽象用户 | tenant_id, user_type, external_id, name |
| abstract_role | 抽象角色（树） | tenant_id, biz_domain_id, role_type, parent_id, path, name |
| operation_permission | 操作权限 | tenant_id, biz_domain_id, code, binary_bit, inherit_mask |
| resource_entity | 资源实体（树） | tenant_id, biz_domain_id, parent_id, code, name, resource_type, path |
| permission_condition | 生效条件（Java 表达式） | tenant_id, code, expression |
| user_role | 用户-角色关联 | tenant_id, abstract_user_id, abstract_role_id, valid_from, valid_to |
| role_resource_permission | 角色-资源-操作 | tenant_id, abstract_role_id, resource_entity_id, operation_permission_id, can_manage, condition_id |
| domain_scope_config | 域下允许的类型/操作 | tenant_id, biz_domain_id, scope_type, scope_ref_id |
| domain_relation_config | 域内可关联关系 | tenant_id, biz_domain_id, relation_type, left_ref_id, right_ref_id, default_condition_id |
| domain_scope_binding | 域引用全局角色/资源/操作 | tenant_id, biz_domain_id, bound_type, bound_entity_id |
| resource_dependency | 资源依赖（鉴权时展开） | resource_entity_id, depends_on_resource_entity_id, source_operation_permission_id, required_operation_permission_id |
| permission_conflict_rule | 同资源互斥操作对 | first_operation_permission_id, second_operation_permission_id, resource_type_value(可选) |
| permission_change_log | 变更记录 | entity_type, entity_id, operation, old_snapshot, new_snapshot, affected_abstract_user_ids, affected_abstract_role_ids |

---

## 3. 类型与枚举

- **system_config**：`config_key` 如 `user_type`、`role_type`、`resource_type`；`type_value` 为 INT，业务表存 type_value。
- 显示名称与描述从 system_config 按 (config_key, type_value) 查询；biz_domain_id 可空表示租户全局类型。
- **domain_scope_config.scope_type**：`ROLE_TYPE` | `RESOURCE_TYPE` | `OPERATION`。
- **domain_relation_config.relation_type**：`ROLE_RESOURCE` | `RESOURCE_OPERATION`。
- **domain_scope_binding.bound_type**：`ROLE` | `RESOURCE` | `OPERATION`。
- **permission_change_log.entity_type**：`user_role` | `batch_user_role` | `role_resource_permission` | `batch_role_resource_permission` | `abstract_user` | `abstract_role` | `resource_entity` 等。
- **permission_change_log.change_source**：`ADMIN` | `MQ_SYNC` | `API` | `SYSTEM`。

---

## 4. 鉴权流程（Java 实现要点）

### 4.1 入参与出口

- **入参**：tenant_id, abstract_user_id, resource_entity_id, operation_permission_id；可选 biz_domain_id（不传则解析用户所有域下的权限）；可选 context（Map，供 condition 表达式使用）。
- **出口**：boolean 或 结果对象（是否通过 + 原因：无角色/无授权/依赖不满足/条件不满足）。

### 4.2 步骤（建议单一路径）

1. **解析用户在该（些）域下的角色**  
   查 user_role（abstract_user_id = ?，valid_from/valid_to 包含当前时间）+ abstract_role；若传入 biz_domain_id，则只保留 role.biz_domain_id = ? 或 role 为全局且被该域 binding 的角色。
2. **解析角色对 (resource_entity_id, operation_permission_id) 的授权**  
   查 role_resource_permission，abstract_role_id IN (上一步角色)，resource_entity_id = ?，operation_permission_id = ?；若有 condition_id，则执行 permission_condition.expression（传入 context），不通过则该条不通过。
3. **依赖展开**（若启用）  
   查 resource_dependency 中 resource_entity_id = 当前资源 且 (source_operation_permission_id IS NULL OR source_operation_permission_id = 当前操作)；对每条 depends_on_resource_entity_id、required_operation_permission_id 递归执行本鉴权流程（深度限制如 5，防环）。
4. **汇总**  
   若存在至少一条授权通过且依赖链全部通过，则鉴权通过。

### 4.3 性能建议

- **缓存**：key = (tenant_id, abstract_user_id, biz_domain_id)，value = Set of (resource_entity_id, operation_permission_id)。user_role 或 role_resource_permission 变更时按 user/role 失效；TTL 1～5 分钟。
- **resource_dependency**：表数据量通常不大，可启动时或按需加载到内存/本地缓存，鉴权时在内存递归。
- **列表接口**：仅返回“用户拥有的 (resource, op)”时不做依赖展开；依赖仅在单次 check(user, resource, op) 时使用。

---

## 5. 授权与配置流程

### 5.1 用户分配角色（user_role）

- **校验**：abstract_user_id、abstract_role_id 存在且未删；若启用域校验，则角色须在域范围内（域内角色或该域 binding 的全局角色）。
- **冲突校验**：调用冲突服务：该用户通过**该角色**将获得的 (resource_entity_id, operation_permission_id)，与用户通过**其他角色**在同一 resource_entity_id 上已有的操作，是否构成 permission_conflict_rule 中的互斥对；若存在则拒绝或告警。
- **写入**：INSERT user_role；写 permission_change_log（entity_type=user_role，affected_abstract_user_ids=[user_id]，affected_abstract_role_ids=[role_id]，new_snapshot 由入参组装）。

### 5.2 角色配置权限（role_resource_permission）

- **校验**：角色、资源、操作存在且未删；若启用域配置校验，则通过 domain_scope_config / domain_relation_config 校验该域下该角色类型可关联该资源类型、该资源类型支持该操作。
- **condition_id**：若未传，可从 domain_relation_config（relation_type=RESOURCE_OPERATION，对应 left_ref_id=资源类型, right_ref_id=操作）取 default_condition_id 填入。
- **冲突校验**：该角色下所有用户，检查其通过其他角色在同一 resource_entity_id 上是否已有与本次 (resource, op) 互斥的操作；若存在则拒绝或告警（大批量时可异步或抽样）。
- **写入**：INSERT role_resource_permission；写 permission_change_log（entity_type=role_resource_permission，affected_abstract_role_ids=[role_id]，new_snapshot 含 resource_entity_id、operation_permission_id 等）。

### 5.3 域配置

- **domain_scope_config**：维护域下允许的 ROLE_TYPE / RESOURCE_TYPE / OPERATION（scope_ref_id 为 type_value 或 operation_permission.id）。
- **domain_relation_config**：维护 ROLE_RESOURCE（角色类型-资源类型）、RESOURCE_OPERATION（资源类型-操作，可带 default_condition_id）。
- **domain_scope_binding**：维护域绑定的全局角色/资源/操作（bound_entity_id 须为对应表 biz_domain_id 为 NULL 的 id）。

### 5.4 资源依赖（resource_dependency）

- **写入**：由业务或同步逻辑维护；例如数据集→数据源、(Menu, VIEW)→(Report, QUERY)、(Report, QUERY)→(Dataset, VIEW)。
- **鉴权时使用**：见 4.2 步骤 3；不自动写入 role_resource_permission。

### 5.5 权限冲突规则（permission_conflict_rule）

- **写入**：first_operation_permission_id < second_operation_permission_id 存库；resource_type_value 可选，NULL 表示对所有资源类型生效。
- **校验时机**：见 5.1、5.2；可单独提供“冲突检测”接口，按用户或资源扫描违规并返回列表。

---

## 6. 变更记录（permission_change_log）

### 6.1 写入约定

- **单条 user_role**：entity_type=user_role，entity_id=user_role.id，affected_abstract_user_ids=[该用户]，affected_abstract_role_ids=[该角色]，old_snapshot/new_snapshot 由行或入参得到。
- **批量用户-角色**：一次请求一条 log；entity_type=batch_user_role，entity_id=0 或批次 id，affected_abstract_user_ids=本批全部用户 id，affected_abstract_role_ids=本批全部角色 id，new_snapshot 含 role_id、user_ids、valid_from、valid_to 等。
- **多用户×多角色**：同上，affected 数组为全部用户与全部角色；new_snapshot 建议含 assignments 列表 [{user_id, role_id}, ...]。
- **批量角色-资源-操作**：entity_type=batch_role_resource_permission，affected_abstract_role_ids=涉及角色；new_snapshot/old_snapshot 含 resource_entity_ids、operation_permission_ids（数组或 JSONB），便于按资源/操作查变更。

### 6.2 查询

- 按用户：“我的权限为什么变了”：`WHERE tenant_id = ? AND ? = ANY(affected_abstract_user_ids) ORDER BY created_at DESC`。
- 按角色：`WHERE tenant_id = ? AND ? = ANY(affected_abstract_role_ids) ORDER BY created_at DESC`。
- 按时间、biz_domain_id、entity_type、request_id 组合过滤。
- 按资源/操作：对 batch_role_resource_permission 记录，从 new_snapshot/old_snapshot 中解析 resource_entity_ids/operation_permission_ids 过滤（或建 GIN 索引在 snapshot 上按需）。

### 6.3 大批量

- 单次影响用户/角色数极大（如 >5000）时，可不把全部 id 写入 affected 数组，仅写 request_id 与 new_snapshot 中的 id 列表；按用户/角色查时用 request_id + snapshot 过滤。或约定单批上限（如 2000）并拆批写多条 log。

---

## 7. 管理端页面建议

- **用户管理**：按租户/域筛用户；多选用户 →「分配角色」选域、多选角色、valid_from/valid_to → 提交（批量 user_role + 冲突校验 + 变更 log）；「我的权限」「权限变更」按用户查。
- **角色与权限**：按域筛角色，树形展示；选角色 → 资源树 + 操作多选（受 domain_relation_config 限制）→ 每 (资源, 操作) 填 can_manage、condition_id；支持多选角色批量添加/移除同一批 (resource, op)。
- **域配置**：单页三 Tab——可用范围(domain_scope_config)、可关联关系(domain_relation_config)、域引用(domain_scope_binding)。
- **权限冲突**：维护 permission_conflict_rule；「检测冲突」按钮调用冲突检测接口，展示违规用户/资源列表。
- **变更记录**：按用户/角色/时间/request_id 查；列表展示 entity_type、operation、created_at、change_reason、change_source，可展开 old_snapshot/new_snapshot。

---

## 8. 接口建议（供 AI 实现参考）

| 能力 | 建议接口 | 说明 |
|------|----------|------|
| 鉴权 | POST /api/perm/check 或 GET /api/perm/check | 入参 tenant_id, user_id, resource_id, operation_id；返回是否通过及原因。 |
| 用户角色 | GET/POST/DELETE /api/perm/users/{userId}/roles | 列表/批量分配/回收；POST 可传 roleIds、valid_from、valid_to。 |
| 角色权限 | GET/POST/DELETE /api/perm/roles/{roleId}/permissions | 列表/批量添加/回收 (resource_id, operation_id)；可带 can_manage、condition_id。 |
| 域配置 | GET/PUT /api/perm/domains/{domainId}/scope, /relation, /binding | 域范围、域关系、域引用。 |
| 资源依赖 | GET/POST/DELETE /api/perm/resource-dependencies | 列表/新增/删除。 |
| 冲突规则 | GET/POST/DELETE /api/perm/conflict-rules | 列表/新增/删除。 |
| 冲突检测 | POST /api/perm/conflict-detection | 返回违规用户/资源/操作列表。 |
| 变更记录 | GET /api/perm/change-logs | 支持按 user_id、role_id、biz_domain_id、时间、entity_type、request_id 过滤。 |

---

## 9. 文件与约定速查

- **建表 SQL**：`permission_center_schema.sql`（按文件内顺序执行即可）。
- **规划文档**：见项目内权限中心规划（表说明、§12 数据流与使用要点、§13 核对与精简要点：表设计/Java 逻辑/鉴权性能/管理端便利性）。
- **软删除**：所有查询默认带 `deleted_at IS NULL`；删除时只更新 deleted_at、deleted_by。
- **唯一约束**：均带 `WHERE deleted_at IS NULL`（或规划中的部分唯一索引），注意 biz_domain_id 可空表的分开约束（域内唯一 / 全局唯一）。

---

## 10. 附录：操作权限二进制约定

- **operation_permission**：binary_bit 为单一比特（如 1,2,4,8）；inherit_mask 为继承的位掩码。
- **实际权限值**：effective = binary_bit | inherit_mask。例如 VIEW=1、EDIT=4 且 inherit_mask=1 则 EDIT 的 effective=5（含查看）。
- 业务侧若按位判断，可对用户在某资源上的所有授权做 OR 后再与请求操作 effective 做 AND 判断。

以上内容足以支撑 AI 或开发人员实现权限中心核心逻辑、接口与管理端页面；细节以 `permission_center_schema.sql` 与规划文档为准。
