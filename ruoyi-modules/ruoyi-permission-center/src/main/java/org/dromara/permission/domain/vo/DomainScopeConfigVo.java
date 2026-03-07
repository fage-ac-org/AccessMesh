package org.dromara.permission.domain.vo;

import lombok.Data;

@Data
public class DomainScopeConfigVo {
    private Long id;
    private Long tenantId;
    private Long bizDomainId;
    private String scopeType;
    private Long scopeRefId;
}
