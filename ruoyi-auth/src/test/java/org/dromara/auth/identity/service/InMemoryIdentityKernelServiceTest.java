package org.dromara.auth.identity.service;

import org.dromara.auth.identity.service.impl.InMemoryIdentityKernelService;
import org.dromara.authcenter.api.enums.SubjectType;
import org.dromara.authcenter.api.model.DelegationContext;
import org.dromara.authcenter.api.model.PrincipalContext;
import org.dromara.authcenter.api.model.SubjectProfile;
import org.dromara.authcenter.api.request.DelegationIssueRequest;
import org.dromara.authcenter.api.request.IdentityContextIssueRequest;
import org.dromara.authcenter.api.request.SubjectQueryRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("InMemoryIdentityKernelService Tests")
@Tag("dev")
class InMemoryIdentityKernelServiceTest {

    private final InMemoryIdentityKernelService service = new InMemoryIdentityKernelService();

    @Test
    @DisplayName("issueContext should build principal context from bootstrap subject")
    void issueContextShouldBuildPrincipalContextFromBootstrapSubject() {
        IdentityContextIssueRequest request = new IdentityContextIssueRequest();
        request.setTenantId("000000");
        request.setSubjectId("10001");
        request.setSubjectType(SubjectType.USER);
        request.setPermissionVersion("000000-v3");

        PrincipalContext principalContext = service.issueContext(request);

        assertEquals("000000", principalContext.getTenantId());
        assertEquals("10001", principalContext.getSubjectId());
        assertEquals(SubjectType.USER, principalContext.getSubjectType());
        assertEquals("000000-v3", principalContext.getPermissionVersion());
        assertEquals("platform-admin", principalContext.getAttributes().get("username"));
    }

    @Test
    @DisplayName("issueDelegation should return delegation descriptor for service principal")
    void issueDelegationShouldReturnDelegationDescriptorForServicePrincipal() {
        DelegationIssueRequest request = new DelegationIssueRequest();
        request.setTenantId("000000");
        request.setServiceSubjectId("svc-reporting");
        request.setDelegatedUserId("10001");
        request.setReason("nightly-report");
        request.setExpiresAtEpochMilli(1893456000000L);

        DelegationContext delegationContext = service.issueDelegation(request);

        assertNotNull(delegationContext.getDelegationId());
        assertEquals("svc-reporting", delegationContext.getServiceSubjectId());
        assertEquals("10001", delegationContext.getDelegatedUserId());
        assertEquals("nightly-report", delegationContext.getReason());
    }

    @Test
    @DisplayName("getSubject should return bootstrap service profile")
    void getSubjectShouldReturnBootstrapServiceProfile() {
        SubjectQueryRequest request = new SubjectQueryRequest();
        request.setTenantId("000000");
        request.setSubjectId("svc-reporting");
        request.setSubjectType(SubjectType.SERVICE);

        SubjectProfile subjectProfile = service.getSubject(request);

        assertEquals("svc-reporting", subjectProfile.getSubjectId());
        assertEquals("reporting-service", subjectProfile.getServiceCode());
        assertEquals(SubjectType.SERVICE, subjectProfile.getSubjectType());
    }
}
