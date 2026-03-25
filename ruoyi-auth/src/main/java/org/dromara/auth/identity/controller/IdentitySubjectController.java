package org.dromara.auth.identity.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dromara.auth.identity.service.IdentityKernelService;
import org.dromara.authcenter.api.model.SubjectProfile;
import org.dromara.authcenter.api.request.SubjectQueryRequest;
import org.dromara.common.core.domain.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * identity-service 主体查询骨架接口
 *
 * @author RuoYi-Cloud-Plus
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/identity/subjects")
public class IdentitySubjectController {

    private final IdentityKernelService identityKernelService;

    @PostMapping("/get")
    public R<SubjectProfile> get(@Valid @RequestBody SubjectQueryRequest request) {
        return R.ok(identityKernelService.getSubject(request));
    }
}
