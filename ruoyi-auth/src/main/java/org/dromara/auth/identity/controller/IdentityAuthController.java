package org.dromara.auth.identity.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dromara.auth.identity.service.IdentityKernelService;
import org.dromara.authcenter.api.model.PrincipalContext;
import org.dromara.authcenter.api.request.IdentityContextIssueRequest;
import org.dromara.common.core.domain.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * identity-service 认证骨架接口
 *
 * @author RuoYi-Cloud-Plus
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/identity/auth")
public class IdentityAuthController {

    private final IdentityKernelService identityKernelService;

    @PostMapping("/issue-context")
    public R<PrincipalContext> issueContext(@Valid @RequestBody IdentityContextIssueRequest request) {
        return R.ok(identityKernelService.issueContext(request));
    }
}
