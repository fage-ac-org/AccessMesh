package org.dromara.permission.domain.dto;

import lombok.Data;

/**
 * 抽象角色列表查询请求（树形或列表）
 */
@Data
public class RoleListReq {
    private Long tenantId;
    private Long bizDomainId;
    private Long parentId;
}
