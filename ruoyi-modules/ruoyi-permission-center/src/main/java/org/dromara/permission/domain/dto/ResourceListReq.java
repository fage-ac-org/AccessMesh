package org.dromara.permission.domain.dto;

import lombok.Data;

/**
 * 资源实体列表查询请求（树形或列表）
 */
@Data
public class ResourceListReq {
    private Long tenantId;
    private Long bizDomainId;
    private Integer resourceType;
    private Long parentId;
}
