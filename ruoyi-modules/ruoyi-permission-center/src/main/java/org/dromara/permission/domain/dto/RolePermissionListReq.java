package org.dromara.permission.domain.dto;

import lombok.Data;

/**
 * 角色下的权限列表请求
 */
@Data
public class RolePermissionListReq {
    private Long tenantId;
    private Long abstractRoleId;
}
