package org.dromara.permission.domain.dto;

import lombok.Data;

/**
 * 域引用列表请求
 */
@Data
public class DomainBindingListReq {
    private Long tenantId;
    private Long bizDomainId;
}
