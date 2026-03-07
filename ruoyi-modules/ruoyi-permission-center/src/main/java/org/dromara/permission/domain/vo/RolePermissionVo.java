package org.dromara.permission.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RolePermissionVo {
    private Long id;
    private Long tenantId;
    private Long abstractRoleId;
    private Long resourceEntityId;
    private Long operationPermissionId;
    private Boolean canManage;
    private Long conditionId;
    private LocalDateTime createdAt;
    private String resourceName;
    private String operationName;
}
