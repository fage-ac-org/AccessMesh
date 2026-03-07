package org.dromara.permission.domain.vo;

import lombok.Data;

@Data
public class DomainScopeBindingVo {
    private Long id;
    private Long tenantId;
    private Long bizDomainId;
    private String boundType;
    private Long boundEntityId;
}
