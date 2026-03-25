package org.dromara.permission.kernel.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dromara.authcenter.api.model.PermissionVersionInfo;
import org.dromara.authcenter.api.request.PermissionVersionQueryRequest;
import org.dromara.common.core.domain.R;
import org.dromara.permission.kernel.service.PermissionKernelService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 版本查询骨架接口
 *
 * @author RuoYi-Cloud-Plus
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/perm/version")
public class VersionKernelController {

    private final PermissionKernelService permissionKernelService;

    @PostMapping("/query")
    public R<PermissionVersionInfo> query(@Valid @RequestBody PermissionVersionQueryRequest request) {
        return R.ok(permissionKernelService.queryVersion(request));
    }
}
