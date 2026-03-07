package org.dromara.permission.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 域关系配置 domain_relation_config
 * relation_type=ROLE_RESOURCE|RESOURCE_OPERATION
 * 逻辑删除：基类 deleteFlag，0=未删除，删除时=id
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("domain_relation_config")
public class PcDomainRelationConfig extends PermissionBaseEntity {

    /** 主键 */
    @TableId("id")
    private Long id;

    /** 业务域ID */
    private Long bizDomainId;

    /** 关系类型：ROLE_RESOURCE/RESOURCE_OPERATION */
    private String relationType;

    /** 左侧引用：ROLE_RESOURCE 为 role_type，RESOURCE_OPERATION 为 resource_type */
    private Long leftRefId;

    /** 右侧引用：ROLE_RESOURCE 为 resource_type，RESOURCE_OPERATION 为 operation_permission.id */
    private Long rightRefId;

    /** RESOURCE_OPERATION 时该资源类型+操作的默认生效条件ID */
    private Long defaultConditionId;
}
