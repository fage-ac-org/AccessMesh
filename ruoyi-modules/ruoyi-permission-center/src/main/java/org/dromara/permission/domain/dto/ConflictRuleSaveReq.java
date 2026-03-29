package org.dromara.permission.domain.dto;

import lombok.Data;

/**
 * 冲突规则保存请求（单条，存库前保证 first < second）
 */
@Data
public class ConflictRuleSaveReq {
    private Long id;
    private Long tenantId;
    private Long bizDomainId;
    private Long firstOperationPermissionId;
    private Long secondOperationPermissionId;
    /** 仅当资源类型为该值时生效，NULL 表示所有资源类型 */
    private Integer resourceTypeValue;
}
