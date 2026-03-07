package org.dromara.permission.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AbstractUserVo {
    private Long id;
    private Long tenantId;
    private Integer userType;
    private String externalId;
    private String name;
    private String extra;
    private LocalDateTime createdAt;
}
