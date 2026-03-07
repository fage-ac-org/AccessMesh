package org.dromara.permission.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * 域范围列表请求
 */
@Data
public class DomainScopeListReq {
    private Long tenantId;
    private Long bizDomainId;
}
