package org.dromara.permission.domain.dto;

import lombok.Data;

/**
 * 业务域保存请求（单条）
 */
@Data
public class DomainSaveReq {
    private Long id;
    private Long tenantId;
    private String code;
    private String name;
    private String description;
}
