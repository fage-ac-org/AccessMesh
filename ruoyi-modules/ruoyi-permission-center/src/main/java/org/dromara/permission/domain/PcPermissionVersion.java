package org.dromara.permission.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 权限版本表 permission_version
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("permission_version")
public class PcPermissionVersion extends PermissionBaseEntity {

    /** 主键 */
    @TableId("id")
    private Long id;

    /** 权限版本号，按租户递增 */
    private Long versionNo;

    /** 触发版本变更的实体类型 */
    private String triggerEntityType;

    /** 触发版本变更的实体ID */
    private Long triggerEntityId;

    /** 版本变更说明 */
    private String remark;
}
