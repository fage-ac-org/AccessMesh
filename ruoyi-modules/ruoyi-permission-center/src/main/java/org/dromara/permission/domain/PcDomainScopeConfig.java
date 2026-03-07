package org.dromara.permission.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 域范围配置 domain_scope_config
 * scope_type=ROLE_TYPE|RESOURCE_TYPE|OPERATION
 * 逻辑删除：基类 deleteFlag，0=未删除，删除时=id
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("domain_scope_config")
public class PcDomainScopeConfig extends PermissionBaseEntity {

    /** 主键 */
    @TableId("id")
    private Long id;

    /** 业务域ID */
    private Long bizDomainId;

    /** 范围类型：ROLE_TYPE/RESOURCE_TYPE/OPERATION */
    private String scopeType;

    /** 引用值：类型时为 type_value，操作时为 operation_permission.id */
    private Long scopeRefId;
}
