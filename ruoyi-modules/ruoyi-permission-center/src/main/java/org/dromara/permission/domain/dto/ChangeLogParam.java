package org.dromara.permission.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 变更日志参数封装
 */
@Data
@Accessors(chain = true)
public class ChangeLogParam {
    private Long tenantId;
    private Long bizDomainId;
    private String entityType;
    private Long entityId;
    private String operation;
    private Object oldSnapshot;
    private Object newSnapshot;
    private String requestId;
    private String changeSource;
}
