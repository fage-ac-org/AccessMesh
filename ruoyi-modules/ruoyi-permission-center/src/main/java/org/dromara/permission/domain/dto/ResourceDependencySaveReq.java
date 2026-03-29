package org.dromara.permission.domain.dto;

import lombok.Data;

/**
 * 资源依赖保存请求（单条）
 */
@Data
public class ResourceDependencySaveReq {
    private Long id;
    private Long tenantId;
    private Long resourceEntityId;
    private Long dependsOnResourceEntityId;
    private Long sourceOperationPermissionId;
    private Long requiredOperationPermissionId;
}
