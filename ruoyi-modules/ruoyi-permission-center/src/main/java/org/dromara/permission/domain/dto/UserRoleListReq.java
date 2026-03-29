package org.dromara.permission.domain.dto;

import lombok.Data;

/**
 * 用户下的角色列表请求
 */
@Data
public class UserRoleListReq {
    private Long tenantId;
    private Long abstractUserId;
}
