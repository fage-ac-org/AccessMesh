package org.dromara.permission.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.permission.domain.dto.*;
import org.dromara.permission.service.PermissionCenterService;
import org.dromara.system.api.model.LoginUser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * 权限中心 REST API（全部 POST，JSON 入参）
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/permission")
@Validated
public class PermissionCenterController {

    private final PermissionCenterService permissionCenterService;

    /**
     * 获取用户角色权限
     */
    @PostMapping("/roles")
    public R<Set<String>> getRolePermission(@RequestBody @Valid RolePermissionReq req) {
        Set<String> roles = permissionCenterService.getRolePermission(req.getUserId(), req.getTenantId());
        return R.ok(roles);
    }

    /**
     * 获取用户菜单/权限标识
     */
    @PostMapping("/menus")
    public R<Set<String>> getMenuPermission(@RequestBody @Valid MenuPermissionReq req) {
        Set<String> menus = permissionCenterService.getMenuPermission(
            req.getUserId(), req.getTenantId(), req.getClientId());
        return R.ok(menus);
    }

    /**
     * 数据范围-角色自定义部门 ID 列表（逗号分隔）
     */
    @PostMapping("/data-scope/role-custom")
    public R<String> getRoleCustom(@RequestBody @Valid DataScopeRoleCustomReq req) {
        String result = permissionCenterService.getRoleCustom(req.getRoleId());
        return R.ok(result);
    }

    /**
     * 数据范围-部门及子部门 ID 列表（逗号分隔）
     */
    @PostMapping("/data-scope/dept-and-child")
    public R<String> getDeptAndChild(@RequestBody @Valid DataScopeDeptReq req) {
        String result = permissionCenterService.getDeptAndChild(req.getDeptId());
        return R.ok(result);
    }

    /**
     * 获取用户信息（供构建 LoginUser）
     */
    @PostMapping("/user/info")
    public R<LoginUser> getUserInfo(@RequestBody @Valid UserInfoReq req) {
        LoginUser user = permissionCenterService.getUserInfo(req.getUserId(), req.getTenantId());
        return user != null ? R.ok(user) : R.fail("用户不存在");
    }
}
