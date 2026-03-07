package org.dromara.permission.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 角色-资源-操作中间表 role_resource_permission
 * condition_id 为 NULL 表示始终生效
 * 逻辑删除：基类 deleteFlag，0=未删除，删除时=id
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("role_resource_permission")
public class PcRoleResourcePermission extends PermissionBaseEntity {

    /** 主键 */
    @TableId("id")
    private Long id;

    /** 抽象角色ID */
    private Long abstractRoleId;

    /** 资源实体ID */
    private Long resourceEntityId;

    /** 操作权限ID */
    private Long operationPermissionId;

    /** 是否可管理(给他人授权) */
    private Boolean canManage;

    /** 生效条件ID，NULL 表示始终生效 */
    private Long conditionId;
}
