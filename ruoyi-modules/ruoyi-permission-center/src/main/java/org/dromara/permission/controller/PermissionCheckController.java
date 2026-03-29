package org.dromara.permission.controller;

import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.permission.domain.dto.PermissionCheckReq;
import org.dromara.permission.domain.vo.PermissionCheckVo;
import org.dromara.permission.service.PermissionCheckService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 鉴权接口
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/perm")
public class PermissionCheckController {

    private final PermissionCheckService permissionCheckService;

    @PostMapping("/check")
    public R<PermissionCheckVo> check(@Validated @RequestBody PermissionCheckReq req) {
        return R.ok(permissionCheckService.check(req));
    }
}
