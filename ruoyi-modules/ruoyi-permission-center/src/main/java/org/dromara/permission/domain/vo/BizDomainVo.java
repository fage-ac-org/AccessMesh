package org.dromara.permission.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BizDomainVo {
    private Long id;
    private Long tenantId;
    private String code;
    private String name;
    private String description;
    private LocalDateTime createdAt;
}
