package org.dromara.permission.domain.vo;

import lombok.Data;

@Data
public class DomainRelationConfigVo {
    private Long id;
    private Long tenantId;
    private Long bizDomainId;
    private String relationType;
    private Long leftRefId;
    private Long rightRefId;
    private Long defaultConditionId;
}
