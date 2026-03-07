-- =============================================================================
-- 通用权限中心 - PostgreSQL 表结构（无外键，逻辑关联由应用保证）
-- 执行顺序按依赖关系，建议按序号依次执行
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 0. 类型/枚举配置表
-- -----------------------------------------------------------------------------
CREATE TABLE system_config (
    id                BIGSERIAL PRIMARY KEY,
    tenant_id         BIGINT NOT NULL,
    biz_domain_id     BIGINT,
    config_key        VARCHAR(64) NOT NULL,
    type_value        INT NOT NULL,
    name              VARCHAR(128) NOT NULL,
    description       VARCHAR(512),
    sort_order        INT DEFAULT 0,
    created_by        BIGINT,
    updated_by        BIGINT,
    deleted_by        BIGINT,
    created_at        TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at        TIMESTAMPTZ NOT NULL DEFAULT now(),
    deleted_at        TIMESTAMPTZ
);

CREATE UNIQUE INDEX uk_system_config_domain ON system_config (tenant_id, biz_domain_id, config_key, type_value) WHERE biz_domain_id IS NOT NULL AND deleted_at IS NULL;
CREATE UNIQUE INDEX uk_system_config_global ON system_config (tenant_id, config_key, type_value) WHERE biz_domain_id IS NULL AND deleted_at IS NULL;

COMMENT ON TABLE system_config IS '类型/枚举 KV 配置：config_key 如 user_type/role_type/resource_type，type_value 为枚举整型';
COMMENT ON COLUMN system_config.id IS '主键';
COMMENT ON COLUMN system_config.tenant_id IS '租户ID';
COMMENT ON COLUMN system_config.biz_domain_id IS '业务域ID，NULL 表示全局类型';
COMMENT ON COLUMN system_config.config_key IS '类型键，如 user_type、role_type、resource_type';
COMMENT ON COLUMN system_config.type_value IS '枚举值，如 1=人员 2=服务 3=第三方';
COMMENT ON COLUMN system_config.name IS '显示名称';
COMMENT ON COLUMN system_config.description IS '描述';
COMMENT ON COLUMN system_config.sort_order IS '排序';
COMMENT ON COLUMN system_config.created_by IS '创建人ID';
COMMENT ON COLUMN system_config.updated_by IS '更新人ID';
COMMENT ON COLUMN system_config.deleted_by IS '删除人ID';
COMMENT ON COLUMN system_config.created_at IS '创建时间';
COMMENT ON COLUMN system_config.updated_at IS '更新时间';
COMMENT ON COLUMN system_config.deleted_at IS '软删时间';

-- -----------------------------------------------------------------------------
-- 1. 业务域表
-- -----------------------------------------------------------------------------
CREATE TABLE biz_domain (
    id          BIGSERIAL PRIMARY KEY,
    tenant_id   BIGINT NOT NULL,
    code        VARCHAR(64) NOT NULL,
    name        VARCHAR(128) NOT NULL,
    description VARCHAR(512),
    created_by  BIGINT,
    updated_by  BIGINT,
    deleted_by  BIGINT,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    deleted_at  TIMESTAMPTZ
);

CREATE UNIQUE INDEX uk_biz_domain ON biz_domain (tenant_id, code) WHERE deleted_at IS NULL;

COMMENT ON TABLE biz_domain IS '业务域，对权限对象分类';
COMMENT ON COLUMN biz_domain.id IS '主键';
COMMENT ON COLUMN biz_domain.tenant_id IS '租户ID';
COMMENT ON COLUMN biz_domain.code IS '域编码';
COMMENT ON COLUMN biz_domain.name IS '域名称';
COMMENT ON COLUMN biz_domain.description IS '描述';
COMMENT ON COLUMN biz_domain.created_by IS '创建人ID';
COMMENT ON COLUMN biz_domain.updated_by IS '更新人ID';
COMMENT ON COLUMN biz_domain.deleted_by IS '删除人ID';
COMMENT ON COLUMN biz_domain.created_at IS '创建时间';
COMMENT ON COLUMN biz_domain.updated_at IS '更新时间';
COMMENT ON COLUMN biz_domain.deleted_at IS '软删时间';

-- -----------------------------------------------------------------------------
-- 2. 抽象用户表（不含 biz_domain_id，通过角色关联域）
-- -----------------------------------------------------------------------------
CREATE TABLE abstract_user (
    id          BIGSERIAL PRIMARY KEY,
    tenant_id   BIGINT NOT NULL,
    user_type   INT NOT NULL,
    external_id VARCHAR(256) NOT NULL,
    name        VARCHAR(256),
    extra       JSONB DEFAULT '{}',
    created_by  BIGINT,
    updated_by  BIGINT,
    deleted_by  BIGINT,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    deleted_at  TIMESTAMPTZ
);

CREATE UNIQUE INDEX uk_abstract_user ON abstract_user (tenant_id, user_type, external_id) WHERE deleted_at IS NULL;
CREATE INDEX idx_abstract_user_tenant ON abstract_user (tenant_id) WHERE deleted_at IS NULL;

COMMENT ON TABLE abstract_user IS '抽象用户，user_type 来自 system_config';
COMMENT ON COLUMN abstract_user.id IS '主键';
COMMENT ON COLUMN abstract_user.tenant_id IS '租户ID';
COMMENT ON COLUMN abstract_user.user_type IS '用户类型枚举值，来自 system_config.config_key=user_type';
COMMENT ON COLUMN abstract_user.external_id IS '外部业务系统唯一标识';
COMMENT ON COLUMN abstract_user.name IS '显示名';
COMMENT ON COLUMN abstract_user.extra IS '扩展属性(JSON)';
COMMENT ON COLUMN abstract_user.created_by IS '创建人ID';
COMMENT ON COLUMN abstract_user.updated_by IS '更新人ID';
COMMENT ON COLUMN abstract_user.deleted_by IS '删除人ID';
COMMENT ON COLUMN abstract_user.created_at IS '创建时间';
COMMENT ON COLUMN abstract_user.updated_at IS '更新时间';
COMMENT ON COLUMN abstract_user.deleted_at IS '软删时间';

-- -----------------------------------------------------------------------------
-- 3. 抽象角色表（支持树形，biz_domain_id 可空表示全局角色）
-- -----------------------------------------------------------------------------
CREATE TABLE abstract_role (
    id            BIGSERIAL PRIMARY KEY,
    tenant_id     BIGINT NOT NULL,
    biz_domain_id BIGINT,
    role_type     INT NOT NULL,
    parent_id     BIGINT,
    external_id   VARCHAR(256),
    name          VARCHAR(256) NOT NULL,
    path          VARCHAR(1024),
    sort_order    INT DEFAULT 0,
    extra         JSONB DEFAULT '{}',
    created_by    BIGINT,
    updated_by    BIGINT,
    deleted_by    BIGINT,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    deleted_at    TIMESTAMPTZ
);

CREATE INDEX idx_abstract_role_tenant_domain ON abstract_role (tenant_id, biz_domain_id) WHERE deleted_at IS NULL;
CREATE INDEX idx_abstract_role_parent ON abstract_role (parent_id) WHERE deleted_at IS NULL;
CREATE INDEX idx_abstract_role_path ON abstract_role (path) WHERE deleted_at IS NULL AND path IS NOT NULL;

COMMENT ON TABLE abstract_role IS '抽象角色，树形；biz_domain_id 为 NULL 表示全局角色';
COMMENT ON COLUMN abstract_role.id IS '主键';
COMMENT ON COLUMN abstract_role.tenant_id IS '租户ID';
COMMENT ON COLUMN abstract_role.biz_domain_id IS '所属业务域ID，NULL 表示全局角色';
COMMENT ON COLUMN abstract_role.role_type IS '角色类型枚举，来自 system_config';
COMMENT ON COLUMN abstract_role.parent_id IS '父节点ID，NULL 为根';
COMMENT ON COLUMN abstract_role.external_id IS '外部业务标识';
COMMENT ON COLUMN abstract_role.name IS '名称';
COMMENT ON COLUMN abstract_role.path IS '树路径，如 /1/2/3';
COMMENT ON COLUMN abstract_role.sort_order IS '同层排序';
COMMENT ON COLUMN abstract_role.extra IS '扩展属性(JSON)';
COMMENT ON COLUMN abstract_role.created_by IS '创建人ID';
COMMENT ON COLUMN abstract_role.updated_by IS '更新人ID';
COMMENT ON COLUMN abstract_role.deleted_by IS '删除人ID';
COMMENT ON COLUMN abstract_role.created_at IS '创建时间';
COMMENT ON COLUMN abstract_role.updated_at IS '更新时间';
COMMENT ON COLUMN abstract_role.deleted_at IS '软删时间';

-- -----------------------------------------------------------------------------
-- 4. 操作权限表（binary_bit + inherit_mask，biz_domain_id 可空表示全局操作）
-- -----------------------------------------------------------------------------
CREATE TABLE operation_permission (
    id            BIGSERIAL PRIMARY KEY,
    tenant_id     BIGINT NOT NULL,
    biz_domain_id BIGINT,
    code          VARCHAR(64) NOT NULL,
    name          VARCHAR(128) NOT NULL,
    binary_bit    INT NOT NULL,
    inherit_mask  INT NOT NULL DEFAULT 0,
    created_by    BIGINT,
    updated_by    BIGINT,
    deleted_by    BIGINT,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    deleted_at    TIMESTAMPTZ
);

CREATE UNIQUE INDEX uk_operation_permission_domain ON operation_permission (tenant_id, biz_domain_id, code) WHERE biz_domain_id IS NOT NULL AND deleted_at IS NULL;
CREATE UNIQUE INDEX uk_operation_permission_global ON operation_permission (tenant_id, code) WHERE biz_domain_id IS NULL AND deleted_at IS NULL;

COMMENT ON TABLE operation_permission IS '操作权限，effective = binary_bit | inherit_mask';
COMMENT ON COLUMN operation_permission.id IS '主键';
COMMENT ON COLUMN operation_permission.tenant_id IS '租户ID';
COMMENT ON COLUMN operation_permission.biz_domain_id IS '所属业务域ID，NULL 表示全局操作';
COMMENT ON COLUMN operation_permission.code IS '操作编码，如 VIEW、EDIT';
COMMENT ON COLUMN operation_permission.name IS '显示名';
COMMENT ON COLUMN operation_permission.binary_bit IS '本操作独占位，如 1、2、4、8';
COMMENT ON COLUMN operation_permission.inherit_mask IS '继承的位掩码，实际权限=binary_bit|inherit_mask';
COMMENT ON COLUMN operation_permission.created_by IS '创建人ID';
COMMENT ON COLUMN operation_permission.updated_by IS '更新人ID';
COMMENT ON COLUMN operation_permission.deleted_by IS '删除人ID';
COMMENT ON COLUMN operation_permission.created_at IS '创建时间';
COMMENT ON COLUMN operation_permission.updated_at IS '更新时间';
COMMENT ON COLUMN operation_permission.deleted_at IS '软删时间';

-- -----------------------------------------------------------------------------
-- 5. 权限资源实体表（树形，biz_domain_id 可空表示全局资源）
-- -----------------------------------------------------------------------------
CREATE TABLE resource_entity (
    id            BIGSERIAL PRIMARY KEY,
    tenant_id     BIGINT NOT NULL,
    biz_domain_id BIGINT,
    parent_id     BIGINT,
    code          VARCHAR(128) NOT NULL,
    name          VARCHAR(256) NOT NULL,
    resource_type INT,
    path          VARCHAR(1024),
    sort_order    INT DEFAULT 0,
    extra         JSONB DEFAULT '{}',
    created_by    BIGINT,
    updated_by    BIGINT,
    deleted_by    BIGINT,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    deleted_at    TIMESTAMPTZ
);

CREATE UNIQUE INDEX uk_resource_entity_domain ON resource_entity (tenant_id, biz_domain_id, code) WHERE biz_domain_id IS NOT NULL AND deleted_at IS NULL;
CREATE UNIQUE INDEX uk_resource_entity_global ON resource_entity (tenant_id, code) WHERE biz_domain_id IS NULL AND deleted_at IS NULL;
CREATE INDEX idx_resource_entity_tenant_domain ON resource_entity (tenant_id, biz_domain_id) WHERE deleted_at IS NULL;
CREATE INDEX idx_resource_entity_parent ON resource_entity (parent_id) WHERE deleted_at IS NULL;

COMMENT ON TABLE resource_entity IS '权限资源实体，树形；resource_type 来自 system_config';
COMMENT ON COLUMN resource_entity.id IS '主键';
COMMENT ON COLUMN resource_entity.tenant_id IS '租户ID';
COMMENT ON COLUMN resource_entity.biz_domain_id IS '所属业务域ID，NULL 表示全局资源';
COMMENT ON COLUMN resource_entity.parent_id IS '父节点ID';
COMMENT ON COLUMN resource_entity.code IS '资源编码';
COMMENT ON COLUMN resource_entity.name IS '名称';
COMMENT ON COLUMN resource_entity.resource_type IS '资源类型枚举，来自 system_config';
COMMENT ON COLUMN resource_entity.path IS '树路径';
COMMENT ON COLUMN resource_entity.sort_order IS '同层排序';
COMMENT ON COLUMN resource_entity.extra IS '扩展属性(JSON)';
COMMENT ON COLUMN resource_entity.created_by IS '创建人ID';
COMMENT ON COLUMN resource_entity.updated_by IS '更新人ID';
COMMENT ON COLUMN resource_entity.deleted_by IS '删除人ID';
COMMENT ON COLUMN resource_entity.created_at IS '创建时间';
COMMENT ON COLUMN resource_entity.updated_at IS '更新时间';
COMMENT ON COLUMN resource_entity.deleted_at IS '软删时间';

-- -----------------------------------------------------------------------------
-- 6. 权限生效条件表（Java 表达式）
-- -----------------------------------------------------------------------------
CREATE TABLE permission_condition (
    id          BIGSERIAL PRIMARY KEY,
    tenant_id   BIGINT NOT NULL,
    code        VARCHAR(64) NOT NULL,
    name        VARCHAR(128) NOT NULL,
    expression  TEXT NOT NULL,
    description VARCHAR(512),
    created_by  BIGINT,
    updated_by  BIGINT,
    deleted_by  BIGINT,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    deleted_at  TIMESTAMPTZ
);

CREATE UNIQUE INDEX uk_permission_condition ON permission_condition (tenant_id, code) WHERE deleted_at IS NULL;

COMMENT ON TABLE permission_condition IS '权限生效条件，expression 为 Java 表达式';
COMMENT ON COLUMN permission_condition.id IS '主键';
COMMENT ON COLUMN permission_condition.tenant_id IS '租户ID';
COMMENT ON COLUMN permission_condition.code IS '条件编码';
COMMENT ON COLUMN permission_condition.name IS '名称';
COMMENT ON COLUMN permission_condition.expression IS 'Java 表达式';
COMMENT ON COLUMN permission_condition.description IS '说明/变量约定';
COMMENT ON COLUMN permission_condition.created_by IS '创建人ID';
COMMENT ON COLUMN permission_condition.updated_by IS '更新人ID';
COMMENT ON COLUMN permission_condition.deleted_by IS '删除人ID';
COMMENT ON COLUMN permission_condition.created_at IS '创建时间';
COMMENT ON COLUMN permission_condition.updated_at IS '更新时间';
COMMENT ON COLUMN permission_condition.deleted_at IS '软删时间';

-- -----------------------------------------------------------------------------
-- 7. 用户-角色关联表
-- -----------------------------------------------------------------------------
CREATE TABLE user_role (
    id               BIGSERIAL PRIMARY KEY,
    tenant_id        BIGINT NOT NULL,
    abstract_user_id BIGINT NOT NULL,
    abstract_role_id BIGINT NOT NULL,
    valid_from       TIMESTAMPTZ,
    valid_to         TIMESTAMPTZ,
    created_by       BIGINT,
    updated_by       BIGINT,
    deleted_by       BIGINT,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
    deleted_at       TIMESTAMPTZ
);

CREATE UNIQUE INDEX uk_user_role ON user_role (tenant_id, abstract_user_id, abstract_role_id) WHERE deleted_at IS NULL;
CREATE INDEX idx_user_role_user ON user_role (abstract_user_id) WHERE deleted_at IS NULL;
CREATE INDEX idx_user_role_role ON user_role (abstract_role_id) WHERE deleted_at IS NULL;

COMMENT ON TABLE user_role IS '用户-角色多对多，valid_from/valid_to 为生效时间范围';
COMMENT ON COLUMN user_role.id IS '主键';
COMMENT ON COLUMN user_role.tenant_id IS '租户ID';
COMMENT ON COLUMN user_role.abstract_user_id IS '抽象用户ID';
COMMENT ON COLUMN user_role.abstract_role_id IS '抽象角色ID';
COMMENT ON COLUMN user_role.valid_from IS '生效开始时间，NULL 不限制';
COMMENT ON COLUMN user_role.valid_to IS '生效结束时间，NULL 不限制';
COMMENT ON COLUMN user_role.created_by IS '创建人ID';
COMMENT ON COLUMN user_role.updated_by IS '更新人ID';
COMMENT ON COLUMN user_role.deleted_by IS '删除人ID';
COMMENT ON COLUMN user_role.created_at IS '创建时间';
COMMENT ON COLUMN user_role.updated_at IS '更新时间';
COMMENT ON COLUMN user_role.deleted_at IS '软删时间';

-- -----------------------------------------------------------------------------
-- 8. 角色-资源-操作中间表
-- -----------------------------------------------------------------------------
CREATE TABLE role_resource_permission (
    id                      BIGSERIAL PRIMARY KEY,
    tenant_id               BIGINT NOT NULL,
    abstract_role_id        BIGINT NOT NULL,
    resource_entity_id      BIGINT NOT NULL,
    operation_permission_id BIGINT NOT NULL,
    can_manage              BOOLEAN NOT NULL DEFAULT false,
    condition_id            BIGINT,
    created_by              BIGINT,
    updated_by              BIGINT,
    deleted_by              BIGINT,
    created_at              TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at              TIMESTAMPTZ NOT NULL DEFAULT now(),
    deleted_at              TIMESTAMPTZ
);

CREATE UNIQUE INDEX uk_role_resource_permission ON role_resource_permission (tenant_id, abstract_role_id, resource_entity_id, operation_permission_id) WHERE deleted_at IS NULL;
CREATE INDEX idx_role_resource_permission_role ON role_resource_permission (abstract_role_id) WHERE deleted_at IS NULL;
CREATE INDEX idx_role_resource_permission_resource ON role_resource_permission (resource_entity_id) WHERE deleted_at IS NULL;

COMMENT ON TABLE role_resource_permission IS '角色对某资源某操作的授权；condition_id 为 NULL 表示始终生效';
COMMENT ON COLUMN role_resource_permission.id IS '主键';
COMMENT ON COLUMN role_resource_permission.tenant_id IS '租户ID';
COMMENT ON COLUMN role_resource_permission.abstract_role_id IS '抽象角色ID';
COMMENT ON COLUMN role_resource_permission.resource_entity_id IS '资源实体ID';
COMMENT ON COLUMN role_resource_permission.operation_permission_id IS '操作权限ID';
COMMENT ON COLUMN role_resource_permission.can_manage IS '是否可管理(给他人授权)';
COMMENT ON COLUMN role_resource_permission.condition_id IS '生效条件ID，NULL 表示始终生效';
COMMENT ON COLUMN role_resource_permission.created_by IS '创建人ID';
COMMENT ON COLUMN role_resource_permission.updated_by IS '更新人ID';
COMMENT ON COLUMN role_resource_permission.deleted_by IS '删除人ID';
COMMENT ON COLUMN role_resource_permission.created_at IS '创建时间';
COMMENT ON COLUMN role_resource_permission.updated_at IS '更新时间';
COMMENT ON COLUMN role_resource_permission.deleted_at IS '软删时间';

-- -----------------------------------------------------------------------------
-- 9.1 域范围配置
-- -----------------------------------------------------------------------------
CREATE TABLE domain_scope_config (
    id            BIGSERIAL PRIMARY KEY,
    tenant_id     BIGINT NOT NULL,
    biz_domain_id BIGINT NOT NULL,
    scope_type    VARCHAR(32) NOT NULL,
    scope_ref_id  BIGINT NOT NULL,
    created_by    BIGINT,
    updated_by    BIGINT,
    deleted_by    BIGINT,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    deleted_at    TIMESTAMPTZ
);

CREATE UNIQUE INDEX uk_domain_scope_config ON domain_scope_config (tenant_id, biz_domain_id, scope_type, scope_ref_id) WHERE deleted_at IS NULL;

COMMENT ON TABLE domain_scope_config IS '域下允许的角色类型/资源类型/操作：scope_type=ROLE_TYPE|RESOURCE_TYPE|OPERATION';
COMMENT ON COLUMN domain_scope_config.id IS '主键';
COMMENT ON COLUMN domain_scope_config.tenant_id IS '租户ID';
COMMENT ON COLUMN domain_scope_config.biz_domain_id IS '业务域ID';
COMMENT ON COLUMN domain_scope_config.scope_type IS '范围类型：ROLE_TYPE/RESOURCE_TYPE/OPERATION';
COMMENT ON COLUMN domain_scope_config.scope_ref_id IS '引用值：类型时为 type_value，操作时为 operation_permission.id';
COMMENT ON COLUMN domain_scope_config.created_by IS '创建人ID';
COMMENT ON COLUMN domain_scope_config.updated_by IS '更新人ID';
COMMENT ON COLUMN domain_scope_config.deleted_by IS '删除人ID';
COMMENT ON COLUMN domain_scope_config.created_at IS '创建时间';
COMMENT ON COLUMN domain_scope_config.updated_at IS '更新时间';
COMMENT ON COLUMN domain_scope_config.deleted_at IS '软删时间';

-- -----------------------------------------------------------------------------
-- 9.2 域关系配置
-- -----------------------------------------------------------------------------
CREATE TABLE domain_relation_config (
    id                   BIGSERIAL PRIMARY KEY,
    tenant_id            BIGINT NOT NULL,
    biz_domain_id        BIGINT NOT NULL,
    relation_type        VARCHAR(32) NOT NULL,
    left_ref_id          BIGINT NOT NULL,
    right_ref_id         BIGINT NOT NULL,
    default_condition_id BIGINT,
    created_by           BIGINT,
    updated_by           BIGINT,
    deleted_by           BIGINT,
    created_at           TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at           TIMESTAMPTZ NOT NULL DEFAULT now(),
    deleted_at           TIMESTAMPTZ
);

CREATE UNIQUE INDEX uk_domain_relation_config ON domain_relation_config (tenant_id, biz_domain_id, relation_type, left_ref_id, right_ref_id) WHERE deleted_at IS NULL;

COMMENT ON TABLE domain_relation_config IS '域内可关联关系：ROLE_RESOURCE(角色类型-资源类型)、RESOURCE_OPERATION(资源类型-操作)';
COMMENT ON COLUMN domain_relation_config.id IS '主键';
COMMENT ON COLUMN domain_relation_config.tenant_id IS '租户ID';
COMMENT ON COLUMN domain_relation_config.biz_domain_id IS '业务域ID';
COMMENT ON COLUMN domain_relation_config.relation_type IS '关系类型：ROLE_RESOURCE/RESOURCE_OPERATION';
COMMENT ON COLUMN domain_relation_config.left_ref_id IS '左侧引用：ROLE_RESOURCE 为 role_type，RESOURCE_OPERATION 为 resource_type';
COMMENT ON COLUMN domain_relation_config.right_ref_id IS '右侧引用：ROLE_RESOURCE 为 resource_type，RESOURCE_OPERATION 为 operation_permission.id';
COMMENT ON COLUMN domain_relation_config.default_condition_id IS 'RESOURCE_OPERATION 时该资源类型+操作的默认生效条件ID';
COMMENT ON COLUMN domain_relation_config.created_by IS '创建人ID';
COMMENT ON COLUMN domain_relation_config.updated_by IS '更新人ID';
COMMENT ON COLUMN domain_relation_config.deleted_by IS '删除人ID';
COMMENT ON COLUMN domain_relation_config.created_at IS '创建时间';
COMMENT ON COLUMN domain_relation_config.updated_at IS '更新时间';
COMMENT ON COLUMN domain_relation_config.deleted_at IS '软删时间';

-- -----------------------------------------------------------------------------
-- 9.3 域引用绑定表（全局角色/资源/操作绑定到域）
-- -----------------------------------------------------------------------------
CREATE TABLE domain_scope_binding (
    id              BIGSERIAL PRIMARY KEY,
    tenant_id       BIGINT NOT NULL,
    biz_domain_id   BIGINT NOT NULL,
    bound_type      VARCHAR(32) NOT NULL,
    bound_entity_id BIGINT NOT NULL,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted_by      BIGINT,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    deleted_at      TIMESTAMPTZ
);

CREATE UNIQUE INDEX uk_domain_scope_binding ON domain_scope_binding (tenant_id, biz_domain_id, bound_type, bound_entity_id) WHERE deleted_at IS NULL;
CREATE INDEX idx_domain_scope_binding_domain ON domain_scope_binding (tenant_id, biz_domain_id) WHERE deleted_at IS NULL;

COMMENT ON TABLE domain_scope_binding IS '将全局角色/资源/操作绑定到业务域，实现一份配置多域生效';
COMMENT ON COLUMN domain_scope_binding.id IS '主键';
COMMENT ON COLUMN domain_scope_binding.tenant_id IS '租户ID';
COMMENT ON COLUMN domain_scope_binding.biz_domain_id IS '被绑定的业务域ID';
COMMENT ON COLUMN domain_scope_binding.bound_type IS '绑定类型：ROLE/RESOURCE/OPERATION';
COMMENT ON COLUMN domain_scope_binding.bound_entity_id IS '被绑定实体ID(对应表主键，须为全局即 biz_domain_id 为 NULL)';
COMMENT ON COLUMN domain_scope_binding.created_by IS '创建人ID';
COMMENT ON COLUMN domain_scope_binding.updated_by IS '更新人ID';
COMMENT ON COLUMN domain_scope_binding.deleted_by IS '删除人ID';
COMMENT ON COLUMN domain_scope_binding.created_at IS '创建时间';
COMMENT ON COLUMN domain_scope_binding.updated_at IS '更新时间';
COMMENT ON COLUMN domain_scope_binding.deleted_at IS '软删时间';

-- -----------------------------------------------------------------------------
-- 9.4 资源依赖表（鉴权时展开，不写库）
-- -----------------------------------------------------------------------------
CREATE TABLE resource_dependency (
    id                               BIGSERIAL PRIMARY KEY,
    tenant_id                        BIGINT NOT NULL,
    resource_entity_id               BIGINT NOT NULL,
    depends_on_resource_entity_id    BIGINT NOT NULL,
    source_operation_permission_id   BIGINT,
    required_operation_permission_id BIGINT NOT NULL,
    created_by                       BIGINT,
    updated_by                       BIGINT,
    deleted_by                       BIGINT,
    created_at                       TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at                       TIMESTAMPTZ NOT NULL DEFAULT now(),
    deleted_at                       TIMESTAMPTZ
);

CREATE UNIQUE INDEX uk_resource_dependency ON resource_dependency (tenant_id, resource_entity_id, depends_on_resource_entity_id, COALESCE(source_operation_permission_id, 0)) WHERE deleted_at IS NULL;
CREATE INDEX idx_resource_dependency_resource ON resource_dependency (resource_entity_id) WHERE deleted_at IS NULL;

COMMENT ON TABLE resource_dependency IS '资源依赖：鉴权时递归检查依赖资源上的 required_operation';
COMMENT ON COLUMN resource_dependency.id IS '主键';
COMMENT ON COLUMN resource_dependency.tenant_id IS '租户ID';
COMMENT ON COLUMN resource_dependency.resource_entity_id IS '主体资源ID(被授权方，如数据集/报表/菜单)';
COMMENT ON COLUMN resource_dependency.depends_on_resource_entity_id IS '依赖资源ID(需同时具备权限，如数据源/数据集)';
COMMENT ON COLUMN resource_dependency.source_operation_permission_id IS '仅当对主体资源做该操作时应用本依赖，NULL 表示任意操作都需满足';
COMMENT ON COLUMN resource_dependency.required_operation_permission_id IS '对依赖资源所需的操作ID';
COMMENT ON COLUMN resource_dependency.created_by IS '创建人ID';
COMMENT ON COLUMN resource_dependency.updated_by IS '更新人ID';
COMMENT ON COLUMN resource_dependency.deleted_by IS '删除人ID';
COMMENT ON COLUMN resource_dependency.created_at IS '创建时间';
COMMENT ON COLUMN resource_dependency.updated_at IS '更新时间';
COMMENT ON COLUMN resource_dependency.deleted_at IS '软删时间';

-- -----------------------------------------------------------------------------
-- 9.5 权限冲突规则表（同资源互斥，不同资源可同一人）
-- -----------------------------------------------------------------------------
CREATE TABLE permission_conflict_rule (
    id                             BIGSERIAL PRIMARY KEY,
    tenant_id                      BIGINT NOT NULL,
    biz_domain_id                  BIGINT,
    first_operation_permission_id  BIGINT NOT NULL,
    second_operation_permission_id BIGINT NOT NULL,
    resource_type_value            INT,
    created_by                     BIGINT,
    updated_by                     BIGINT,
    deleted_by                     BIGINT,
    created_at                     TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at                     TIMESTAMPTZ NOT NULL DEFAULT now(),
    deleted_at                     TIMESTAMPTZ
);

CREATE UNIQUE INDEX uk_permission_conflict_rule_domain ON permission_conflict_rule (tenant_id, biz_domain_id, first_operation_permission_id, second_operation_permission_id) WHERE biz_domain_id IS NOT NULL AND deleted_at IS NULL;
CREATE UNIQUE INDEX uk_permission_conflict_rule_global ON permission_conflict_rule (tenant_id, first_operation_permission_id, second_operation_permission_id) WHERE biz_domain_id IS NULL AND deleted_at IS NULL;

COMMENT ON TABLE permission_conflict_rule IS '同一用户对同一 resource_entity_id 不能同时拥有 first 与 second 操作；存库时 first_id < second_id';
COMMENT ON COLUMN permission_conflict_rule.id IS '主键';
COMMENT ON COLUMN permission_conflict_rule.tenant_id IS '租户ID';
COMMENT ON COLUMN permission_conflict_rule.biz_domain_id IS '业务域ID，NULL 表示全局规则';
COMMENT ON COLUMN permission_conflict_rule.first_operation_permission_id IS '互斥操作一';
COMMENT ON COLUMN permission_conflict_rule.second_operation_permission_id IS '互斥操作二(存库时 first_id < second_id)';
COMMENT ON COLUMN permission_conflict_rule.resource_type_value IS '仅当资源类型为该枚举值时生效，NULL 表示所有资源类型';
COMMENT ON COLUMN permission_conflict_rule.created_by IS '创建人ID';
COMMENT ON COLUMN permission_conflict_rule.updated_by IS '更新人ID';
COMMENT ON COLUMN permission_conflict_rule.deleted_by IS '删除人ID';
COMMENT ON COLUMN permission_conflict_rule.created_at IS '创建时间';
COMMENT ON COLUMN permission_conflict_rule.updated_at IS '更新时间';
COMMENT ON COLUMN permission_conflict_rule.deleted_at IS '软删时间';

-- -----------------------------------------------------------------------------
-- 10. 变更记录表
-- -----------------------------------------------------------------------------
CREATE TABLE permission_change_log (
    id                         BIGSERIAL PRIMARY KEY,
    tenant_id                  BIGINT NOT NULL,
    biz_domain_id              BIGINT,
    entity_type                VARCHAR(64) NOT NULL,
    entity_id                  BIGINT,
    operation                  VARCHAR(16) NOT NULL,
    old_snapshot               JSONB,
    new_snapshot               JSONB,
    affected_abstract_user_ids BIGINT[] DEFAULT '{}',
    affected_abstract_role_ids  BIGINT[] DEFAULT '{}',
    change_reason              VARCHAR(512),
    change_source              VARCHAR(32) NOT NULL,
    request_id                 VARCHAR(64),
    created_by                 BIGINT,
    created_at                 TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_change_log_tenant_users ON permission_change_log USING GIN (affected_abstract_user_ids);
CREATE INDEX idx_change_log_tenant_roles ON permission_change_log USING GIN (affected_abstract_role_ids);
CREATE INDEX idx_change_log_tenant_domain_time ON permission_change_log (tenant_id, biz_domain_id, created_at DESC);
CREATE INDEX idx_change_log_entity ON permission_change_log (tenant_id, entity_type, entity_id);

COMMENT ON TABLE permission_change_log IS '权限变更记录；batch 时 snapshot 含 resource_entity_ids/operation_permission_ids 便于按资源查';
COMMENT ON COLUMN permission_change_log.id IS '主键';
COMMENT ON COLUMN permission_change_log.tenant_id IS '租户ID';
COMMENT ON COLUMN permission_change_log.biz_domain_id IS '业务域ID，NULL 表示与域无关';
COMMENT ON COLUMN permission_change_log.entity_type IS '变更实体类型：user_role/batch_user_role/role_resource_permission 等';
COMMENT ON COLUMN permission_change_log.entity_id IS '被变更记录的主键ID，批量时可0或批次ID';
COMMENT ON COLUMN permission_change_log.operation IS '操作：INSERT/UPDATE/DELETE';
COMMENT ON COLUMN permission_change_log.old_snapshot IS '变更前快照(JSON)';
COMMENT ON COLUMN permission_change_log.new_snapshot IS '变更后快照(JSON)';
COMMENT ON COLUMN permission_change_log.affected_abstract_user_ids IS '本条变更影响的用户ID数组';
COMMENT ON COLUMN permission_change_log.affected_abstract_role_ids IS '本条变更影响的角色ID数组';
COMMENT ON COLUMN permission_change_log.change_reason IS '变更原因说明';
COMMENT ON COLUMN permission_change_log.change_source IS '变更来源：ADMIN/MQ_SYNC/API/SYSTEM';
COMMENT ON COLUMN permission_change_log.request_id IS '请求/追踪ID';
COMMENT ON COLUMN permission_change_log.created_by IS '执行变更的操作人ID';
COMMENT ON COLUMN permission_change_log.created_at IS '变更时间';
