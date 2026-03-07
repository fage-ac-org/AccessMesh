package org.dromara.permission.service;

import org.dromara.permission.domain.dto.*;

/**
 * 通用同步服务：外部系统同步用户、角色、资源及关联到权限中心，幂等写入
 *
 * @author RuoYi-Cloud-Plus
 */
public interface PermissionSyncService {

    /**
     * 同步用户（批量），按 (tenantId, userType, externalId) 幂等
     */
    void syncUsers(SyncUsersReq req);

    /**
     * 同步角色（批量），按 (tenantId, bizDomainId, externalId) 幂等
     */
    void syncRoles(SyncRolesReq req);

    /**
     * 同步资源（批量），按 (tenantId, bizDomainId, code) 幂等
     */
    void syncResources(SyncResourcesReq req);

    /**
     * 同步用户-角色关联
     */
    void syncUserRoles(SyncUserRolesReq req);

    /**
     * 同步角色-权限
     */
    void syncRolePermissions(SyncRolePermissionsReq req);
}
