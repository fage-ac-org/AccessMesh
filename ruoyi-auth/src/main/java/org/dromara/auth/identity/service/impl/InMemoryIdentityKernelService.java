package org.dromara.auth.identity.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.dromara.auth.identity.service.IdentityKernelService;
import org.dromara.authcenter.api.enums.SubjectType;
import org.dromara.authcenter.api.model.DelegationContext;
import org.dromara.authcenter.api.model.PrincipalContext;
import org.dromara.authcenter.api.model.SubjectProfile;
import org.dromara.authcenter.api.request.DelegationIssueRequest;
import org.dromara.authcenter.api.request.DelegationRevokeRequest;
import org.dromara.authcenter.api.request.IdentityContextIssueRequest;
import org.dromara.authcenter.api.request.SubjectQueryRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存版身份内核服务
 *
 * 当前实现仅用于混合内核架构占位，不影响既有登录链路。
 *
 * @author RuoYi-Cloud-Plus
 */
@Slf4j
@Service
public class InMemoryIdentityKernelService implements IdentityKernelService {

    private final Map<String, SubjectProfile> subjects = new ConcurrentHashMap<>();
    private final Map<String, DelegationContext> delegations = new ConcurrentHashMap<>();

    public InMemoryIdentityKernelService() {
        bootstrapSubjects();
    }

    @Override
    public PrincipalContext issueContext(IdentityContextIssueRequest request) {
        SubjectProfile subjectProfile = getOrCreateSubject(request.getTenantId(), request.getSubjectId(), request.getSubjectType());
        PrincipalContext principalContext = new PrincipalContext();
        principalContext.setTenantId(subjectProfile.getTenantId());
        principalContext.setSubjectId(subjectProfile.getSubjectId());
        principalContext.setSubjectType(subjectProfile.getSubjectType());
        principalContext.setPermissionVersion(resolvePermissionVersion(request.getTenantId(), request.getPermissionVersion()));
        principalContext.setServiceCode(subjectProfile.getServiceCode());
        principalContext.setOrgIds(subjectProfile.getOrgIds());
        principalContext.setPositionIds(subjectProfile.getPositionIds());
        principalContext.setAttributes(subjectProfile.getAttributes());
        if (request.getDelegationId() != null && !request.getDelegationId().isBlank()) {
            DelegationContext delegationContext = delegations.get(request.getDelegationId());
            if (delegationContext != null) {
                principalContext.setDelegationContext(delegationContext);
                principalContext.setSubjectType(SubjectType.DELEGATED);
            }
        }
        return principalContext;
    }

    @Override
    public DelegationContext issueDelegation(DelegationIssueRequest request) {
        DelegationContext delegationContext = new DelegationContext();
        delegationContext.setDelegationId(UUID.randomUUID().toString());
        delegationContext.setServiceSubjectId(request.getServiceSubjectId());
        delegationContext.setDelegatedUserId(request.getDelegatedUserId());
        delegationContext.setIssuedAtEpochMilli(System.currentTimeMillis());
        delegationContext.setExpiresAtEpochMilli(request.getExpiresAtEpochMilli());
        delegationContext.setReason(request.getReason());
        delegations.put(delegationContext.getDelegationId(), delegationContext);
        log.info("Issued delegation {} for service {} on behalf of user {}", delegationContext.getDelegationId(),
            request.getServiceSubjectId(), request.getDelegatedUserId());
        return delegationContext;
    }

    @Override
    public void revokeDelegation(DelegationRevokeRequest request) {
        delegations.remove(request.getDelegationId());
        log.info("Revoked delegation {}", request.getDelegationId());
    }

    @Override
    public SubjectProfile getSubject(SubjectQueryRequest request) {
        return getOrCreateSubject(request.getTenantId(), request.getSubjectId(), request.getSubjectType());
    }

    private SubjectProfile getOrCreateSubject(String tenantId, String subjectId, SubjectType subjectType) {
        return subjects.computeIfAbsent(buildSubjectKey(tenantId, subjectType, subjectId),
            key -> createEphemeralSubject(tenantId, subjectId, subjectType));
    }

    private String buildSubjectKey(String tenantId, SubjectType subjectType, String subjectId) {
        return tenantId + ":" + subjectType + ":" + subjectId;
    }

    private String resolvePermissionVersion(String tenantId, String permissionVersion) {
        if (permissionVersion != null && !permissionVersion.isBlank()) {
            return permissionVersion;
        }
        return tenantId + "-v0";
    }

    private SubjectProfile createEphemeralSubject(String tenantId, String subjectId, SubjectType subjectType) {
        SubjectProfile subjectProfile = new SubjectProfile();
        subjectProfile.setTenantId(tenantId);
        subjectProfile.setSubjectId(subjectId);
        subjectProfile.setSubjectType(subjectType);
        subjectProfile.setDisplayName(subjectType == SubjectType.SERVICE ? "ephemeral-service" : "ephemeral-user");
        subjectProfile.setUsername(subjectId);
        subjectProfile.setAttributes(new ConcurrentHashMap<>());
        return subjectProfile;
    }

    private void bootstrapSubjects() {
        SubjectProfile user = new SubjectProfile();
        user.setTenantId("000000");
        user.setSubjectId("10001");
        user.setSubjectType(SubjectType.USER);
        user.setUsername("platform-admin");
        user.setDisplayName("平台管理员");
        user.setOrgIds(List.of("20001"));
        user.setPositionIds(List.of("30001"));
        user.setAttributes(Map.of("username", "platform-admin", "nickname", "平台管理员"));
        subjects.put(buildSubjectKey(user.getTenantId(), user.getSubjectType(), user.getSubjectId()), user);

        SubjectProfile service = new SubjectProfile();
        service.setTenantId("000000");
        service.setSubjectId("svc-reporting");
        service.setSubjectType(SubjectType.SERVICE);
        service.setUsername("svc-reporting");
        service.setDisplayName("报表服务账号");
        service.setServiceCode("reporting-service");
        service.setAttributes(Map.of("serviceCode", "reporting-service"));
        subjects.put(buildSubjectKey(service.getTenantId(), service.getSubjectType(), service.getSubjectId()), service);
    }
}
