package org.dromara.permission.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 权限冲突规则表 permission_conflict_rule
 * 同一用户对同一 resource_entity_id 不能同时拥有 first 与 second 操作；存库时 first_id < second_id
 * 逻辑删除：基类 deleteFlag，0=未删除，删除时=id
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("permission_conflict_rule")
public class PcPermissionConflictRule extends PermissionBaseEntity {

    /** 主键 */
    @TableId("id")
    private Long id;

    /** 业务域ID，NULL 表示全局规则 */
    private Long bizDomainId;

    /** 互斥操作一 */
    private Long firstOperationPermissionId;

    /** 互斥操作二(存库时 first_id < second_id) */
    private Long secondOperationPermissionId;

    /** 仅当资源类型为该枚举值时生效，NULL 表示所有资源类型 */
    private Integer resourceTypeValue;
}
