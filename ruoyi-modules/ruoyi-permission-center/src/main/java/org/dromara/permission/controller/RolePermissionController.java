package org.dromara.permission.controller;

import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.permission.domain.dto.RolePermissionAddReq;
import org.dromara.permission.domain.dto.RolePermissionListReq;
import org.dromara.permission.domain.dto.RolePermissionRemoveReq;
import org.dromara.permission.domain.vo.RolePermissionVo;
import org.dromara.permission.service.RolePermissionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色-资源-操作权限 role_resource_permission 接口
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/perm/role-permissions")
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;

    @PostMapping("/list")
    public R<List<RolePermissionVo>> list(@RequestBody RolePermissionListReq req) {
        return R.ok(rolePermissionService.list(req));
    }

    @PostMapping("/add")
    public R<Void> add(@Validated @RequestBody RolePermissionAddReq req) {
        rolePermissionService.add(req);
        return R.ok();
    }

    @PostMapping("/remove")
    public R<Void> remove(@Validated @RequestBody RolePermissionRemoveReq req) {
        rolePermissionService.remove(req);
        return R.ok();
    }
}
