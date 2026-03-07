package org.dromara.permission.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 变更记录分页请求（规划文档 §3.8 字段命名）
 */
@Data
public class ChangeLogPagePlanReq {
    private Long tenantId;
    private Long bizDomainId;
    private String entityType;
    private Long affectedAbstractUserId;
    private Long affectedAbstractRoleId;
    private String requestId;
    private LocalDateTime timeFrom;
    private LocalDateTime timeTo;
    private Integer pageNum;
    private Integer pageSize;
}
