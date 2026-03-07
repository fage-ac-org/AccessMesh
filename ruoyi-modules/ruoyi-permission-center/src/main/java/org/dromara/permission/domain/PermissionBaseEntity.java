package org.dromara.permission.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 权限中心实体基类（租户 + 审计 + 软删除）
 * 对应表字段：tenant_id, created_by, created_at, updated_by, updated_at, deleted_by, deleted_at, delete_flag
 * 逻辑删除：delete_flag=0 未删除，delete_flag=id 表示已删除（删除时写入本行主键 id）
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
public abstract class PermissionBaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 租户ID */
    @TableField("tenant_id")
    private Long tenantId;

    /** 创建人ID */
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private Long createdBy;

    /** 创建时间 */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private Date createdAt;

    /** 更新人ID */
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private Long updatedBy;

    /** 更新时间 */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private Date updatedAt;

    /** 删除人ID */
    @TableField("deleted_by")
    private Long deletedBy;

    /** 软删时间，NULL 表示未删除 */
    @TableField("deleted_at")
    private Date deletedAt;

    /** 逻辑删除标识：0=未删除，删除时填本行主键 id */
    @TableField("delete_flag")
    private Long deleteFlag = 0L;
}
