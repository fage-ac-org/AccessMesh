package org.dromara.permission.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 资源依赖表 resource_dependency
 * 鉴权时递归检查依赖资源上的 required_operation
 * 逻辑删除：基类 deleteFlag，0=未删除，删除时=id
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("resource_dependency")
public class PcResourceDependency extends PermissionBaseEntity {

    /** 主键 */
    @TableId("id")
    private Long id;

    /** 主体资源ID(被授权方) */
    private Long resourceEntityId;

    /** 依赖资源ID(需同时具备权限) */
    private Long dependsOnResourceEntityId;

    /** 仅当对主体资源做该操作时应用本依赖，NULL 表示任意操作都需满足 */
    private Long sourceOperationPermissionId;

    /** 对依赖资源所需的操作ID */
    private Long requiredOperationPermissionId;
}
