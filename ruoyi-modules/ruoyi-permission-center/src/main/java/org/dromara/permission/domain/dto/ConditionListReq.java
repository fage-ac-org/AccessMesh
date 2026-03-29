package org.dromara.permission.domain.dto;

import lombok.Data;

/**
 * 生效条件列表查询请求
 */
@Data
public class ConditionListReq {
    private Long tenantId;
    private String code;
}
