package org.dromara.permission.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SystemConfigVo {
    private Long id;
    private Long tenantId;
    private Long bizDomainId;
    private String configKey;
    private Integer typeValue;
    private String name;
    private String description;
    private Integer sortOrder;
    private LocalDateTime createdAt;
}
