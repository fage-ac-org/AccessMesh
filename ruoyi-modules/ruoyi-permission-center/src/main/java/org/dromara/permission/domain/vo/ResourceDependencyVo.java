package org.dromara.permission.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResourceDependencyVo {
    private Long id;
    private Long tenantId;
    private Long resourceEntityId;
    private Long dependsOnResourceEntityId;
    private Long sourceOperationPermissionId;
    private Long requiredOperationPermissionId;
    private LocalDateTime createdAt;
}
