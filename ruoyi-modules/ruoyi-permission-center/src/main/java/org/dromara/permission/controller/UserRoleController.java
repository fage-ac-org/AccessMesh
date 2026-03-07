package org.dromara.permission.controller;

import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.permission.domain.dto.UserRoleAssignReq;
import org.dromara.permission.domain.dto.UserRoleListReq;
import org.dromara.permission.domain.dto.UserRoleRevokeReq;
import org.dromara.permission.domain.vo.UserRoleVo;
import org.dromara.permission.service.UserRoleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户-角色 user_role 接口
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/perm/user-roles")
public class UserRoleController {

    private final UserRoleService userRoleService;

    @PostMapping("/list")
    public R<List<UserRoleVo>> list(@RequestBody UserRoleListReq req) {
        return R.ok(userRoleService.list(req));
    }

    @PostMapping("/assign")
    public R<Void> assign(@Validated @RequestBody UserRoleAssignReq req) {
        userRoleService.assign(req);
        return R.ok();
    }

    @PostMapping("/revoke")
    public R<Void> revoke(@Validated @RequestBody UserRoleRevokeReq req) {
        userRoleService.revoke(req);
        return R.ok();
    }
}
