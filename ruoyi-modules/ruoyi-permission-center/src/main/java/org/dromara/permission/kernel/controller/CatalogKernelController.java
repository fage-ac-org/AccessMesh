package org.dromara.permission.kernel.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dromara.authcenter.api.model.PermissionVersionInfo;
import org.dromara.authcenter.api.request.CatalogRegistrationRequest;
import org.dromara.common.core.domain.R;
import org.dromara.permission.kernel.service.PermissionKernelService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 能力目录骨架接口
 *
 * @author RuoYi-Cloud-Plus
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/perm/catalog")
public class CatalogKernelController {

    private final PermissionKernelService permissionKernelService;

    @PostMapping("/register")
    public R<PermissionVersionInfo> register(@Valid @RequestBody CatalogRegistrationRequest request) {
        return R.ok(permissionKernelService.registerCatalog(request));
    }
}
