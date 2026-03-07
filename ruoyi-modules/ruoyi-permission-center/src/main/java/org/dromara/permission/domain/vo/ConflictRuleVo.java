package org.dromara.permission.domain.vo;

import lombok.Data;

/**
 * 冲突规则 VO
 */
@Data
public class ConflictRuleVo {
    private Long id;
    private Long tenantId;
    private Long bizDomainId;
    private Long firstOperationPermissionId;
    private Long secondOperationPermissionId;
    private Integer resourceTypeValue;
}
