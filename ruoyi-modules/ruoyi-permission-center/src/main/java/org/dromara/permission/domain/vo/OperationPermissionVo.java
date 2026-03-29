package org.dromara.permission.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OperationPermissionVo {
    private Long id;
    private Long tenantId;
    private Long bizDomainId;
    private String code;
    private String name;
    private Long binaryBit;
    private Long inheritMask;
    private LocalDateTime createdAt;
}
