package org.dromara.permission.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * 类型枚举查询请求
 */
@Data
public class SystemConfigListReq {
    private Long tenantId;
    private Long bizDomainId;
    private String configKey;
}
