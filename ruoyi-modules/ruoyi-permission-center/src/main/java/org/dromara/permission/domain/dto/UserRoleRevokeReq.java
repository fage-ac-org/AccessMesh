package org.dromara.permission.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * 批量回收角色请求
 */
@Data
public class UserRoleRevokeReq {
    private Long tenantId;
    private Long abstractUserId;
    private List<Long> roleIds;
}
