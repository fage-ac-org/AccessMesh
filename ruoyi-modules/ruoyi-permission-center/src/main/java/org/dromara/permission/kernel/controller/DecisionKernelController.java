package org.dromara.permission.kernel.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dromara.authcenter.api.model.InterfaceDecisionResult;
import org.dromara.authcenter.api.request.InterfacePermissionDecisionRequest;
import org.dromara.common.core.domain.R;
import org.dromara.permission.kernel.service.PermissionKernelService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 鉴权决策骨架接口
 *
 * @author RuoYi-Cloud-Plus
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/perm/decision")
public class DecisionKernelController {

    private final PermissionKernelService permissionKernelService;

    @PostMapping("/interface")
    public R<InterfaceDecisionResult> interfaceDecision(@Valid @RequestBody InterfacePermissionDecisionRequest request) {
        return R.ok(permissionKernelService.decideInterface(request));
    }
}
