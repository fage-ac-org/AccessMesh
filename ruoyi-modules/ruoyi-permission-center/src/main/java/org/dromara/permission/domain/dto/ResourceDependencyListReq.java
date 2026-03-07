package org.dromara.permission.domain.dto;

import lombok.Data;

/**
 * 资源依赖列表请求
 */
@Data
public class ResourceDependencyListReq {
    private Long tenantId;
    private Long resourceEntityId;
}
