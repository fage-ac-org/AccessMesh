package org.dromara.permission.domain.dto;

import lombok.Data;

/**
 * 生效条件保存请求（单条）
 */
@Data
public class ConditionSaveReq {
    private Long id;
    private Long tenantId;
    private String code;
    private String name;
    private String expression;
    private String description;
}
