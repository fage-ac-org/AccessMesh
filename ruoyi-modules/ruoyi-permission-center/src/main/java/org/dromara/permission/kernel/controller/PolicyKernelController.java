package org.dromara.permission.kernel.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dromara.authcenter.api.model.CustomScopeDescriptor;
import org.dromara.authcenter.api.model.DataScopeDescriptor;
import org.dromara.authcenter.api.model.InterfacePermissionSnapshot;
import org.dromara.authcenter.api.request.DataScopeQueryRequest;
import org.dromara.authcenter.api.request.InterfacePermissionSnapshotRequest;
import org.dromara.common.core.domain.R;
import org.dromara.permission.kernel.service.PermissionKernelService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 权限描述查询骨架接口
 *
 * @author RuoYi-Cloud-Plus
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/perm/policy")
public class PolicyKernelController {

    private final PermissionKernelService permissionKernelService;

    @PostMapping("/interface-snapshot")
    public R<InterfacePermissionSnapshot> interfaceSnapshot(@Valid @RequestBody InterfacePermissionSnapshotRequest request) {
        return R.ok(permissionKernelService.queryInterfaceSnapshot(request));
    }

    @PostMapping("/data-scope")
    public R<List<DataScopeDescriptor>> dataScope(@Valid @RequestBody DataScopeQueryRequest request) {
        return R.ok(permissionKernelService.queryDataScopes(request));
    }

    @PostMapping("/custom-scope")
    public R<List<CustomScopeDescriptor>> customScope(@Valid @RequestBody DataScopeQueryRequest request) {
        return R.ok(permissionKernelService.queryCustomScopes(request));
    }
}
