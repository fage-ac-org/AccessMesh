package org.dromara.permission.domain.dto;

import lombok.Data;

/**
 * 冲突规则列表请求
 */
@Data
public class ConflictRuleListReq {
    private Long tenantId;
    private Long bizDomainId;
}
