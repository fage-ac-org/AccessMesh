package org.dromara.permission.domain.dto;

import lombok.Data;

/**
 * 域关系列表请求
 */
@Data
public class DomainRelationListReq {
    private Long tenantId;
    private Long bizDomainId;
    private String relationType;
}
