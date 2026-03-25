package org.dromara.auth.identity.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dromara.auth.identity.service.IdentityKernelService;
import org.dromara.authcenter.api.model.DelegationContext;
import org.dromara.authcenter.api.request.DelegationIssueRequest;
import org.dromara.authcenter.api.request.DelegationRevokeRequest;
import org.dromara.common.core.domain.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * identity-service 委托骨架接口
 *
 * @author RuoYi-Cloud-Plus
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/identity/delegation")
public class IdentityDelegationController {

    private final IdentityKernelService identityKernelService;

    @PostMapping("/issue")
    public R<DelegationContext> issue(@Valid @RequestBody DelegationIssueRequest request) {
        return R.ok(identityKernelService.issueDelegation(request));
    }

    @PostMapping("/revoke")
    public R<Void> revoke(@Valid @RequestBody DelegationRevokeRequest request) {
        identityKernelService.revokeDelegation(request);
        return R.ok();
    }
}
