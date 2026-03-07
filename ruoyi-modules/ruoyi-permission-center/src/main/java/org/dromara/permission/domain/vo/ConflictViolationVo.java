package org.dromara.permission.domain.vo;

import lombok.Data;

/**
 * 冲突检测违规项
 */
@Data
public class ConflictViolationVo {
    private Long abstractUserId;
    private Long abstractRoleId;
    private Long resourceEntityId;
    private Long operationPermissionId;
    private String description;
}
