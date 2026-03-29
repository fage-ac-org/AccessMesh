package org.dromara.permission.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * 批量移除角色权限请求（成对列表）
 */
@Data
public class RolePermissionRemoveReq {
    private Long tenantId;
    private Long abstractRoleId;
    /** 要移除的 (resourceEntityId, operationPermissionId) 对 */
    private List<RolePermissionPair> items;

    @Data
    public static class RolePermissionPair {
        private Long resourceEntityId;
        private Long operationPermissionId;
    }
}
