package org.dromara.permission.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 域引用绑定表 domain_scope_binding
 * 将全局角色/资源/操作绑定到业务域，bound_type=ROLE|RESOURCE|OPERATION
 * 逻辑删除：基类 deleteFlag，0=未删除，删除时=id
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("domain_scope_binding")
public class PcDomainScopeBinding extends PermissionBaseEntity {

    /** 主键 */
    @TableId("id")
    private Long id;

    /** 被绑定的业务域ID */
    private Long bizDomainId;

    /** 绑定类型：ROLE/RESOURCE/OPERATION */
    private String boundType;

    /** 被绑定实体ID(对应表主键，须为全局即 biz_domain_id 为 NULL) */
    private Long boundEntityId;
}
