package org.dromara.auth.identity.service;

import org.dromara.authcenter.api.model.DelegationContext;
import org.dromara.authcenter.api.model.PrincipalContext;
import org.dromara.authcenter.api.model.SubjectProfile;
import org.dromara.authcenter.api.request.DelegationIssueRequest;
import org.dromara.authcenter.api.request.DelegationRevokeRequest;
import org.dromara.authcenter.api.request.IdentityContextIssueRequest;
import org.dromara.authcenter.api.request.SubjectQueryRequest;

/**
 * 身份内核服务
 *
 * @author RuoYi-Cloud-Plus
 */
public interface IdentityKernelService {

    PrincipalContext issueContext(IdentityContextIssueRequest request);

    DelegationContext issueDelegation(DelegationIssueRequest request);

    void revokeDelegation(DelegationRevokeRequest request);

    SubjectProfile getSubject(SubjectQueryRequest request);
}
