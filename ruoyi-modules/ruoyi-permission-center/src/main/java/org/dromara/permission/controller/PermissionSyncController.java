package org.dromara.permission.controller;

import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.permission.domain.dto.*;
import org.dromara.permission.service.PermissionSyncService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 通用同步接口：从外部系统同步用户、角色、资源、用户-角色、角色-权限。
 * 统一前缀：/api/perm/sync，全部 POST + JSON。
 *
 * @author RuoYi-Cloud-Plus
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/perm/sync")
public class PermissionSyncController {

    private final PermissionSyncService permissionSyncService;

    /**
     * 同步用户（抽象用户）
     */
    @PostMapping("/users")
    public R<Void> syncUsers(@Validated @RequestBody SyncUsersReq req) {
        permissionSyncService.syncUsers(req);
        return R.ok();
    }

    /**
     * 同步角色（抽象角色）
     */
    @PostMapping("/roles")
    public R<Void> syncRoles(@Validated @RequestBody SyncRolesReq req) {
        permissionSyncService.syncRoles(req);
        return R.ok();
    }

    /**
     * 同步资源（资源实体）
     */
    @PostMapping("/resources")
    public R<Void> syncResources(@Validated @RequestBody SyncResourcesReq req) {
        permissionSyncService.syncResources(req);
        return R.ok();
    }

    /**
     * 同步用户-角色关联
     */
    @PostMapping("/user-roles")
    public R<Void> syncUserRoles(@Validated @RequestBody SyncUserRolesReq req) {
        permissionSyncService.syncUserRoles(req);
        return R.ok();
    }

    /**
     * 同步角色-权限（角色对资源的操作权限）
     */
    @PostMapping("/role-permissions")
    public R<Void> syncRolePermissions(@Validated @RequestBody SyncRolePermissionsReq req) {
        permissionSyncService.syncRolePermissions(req);
        return R.ok();
    }
}
