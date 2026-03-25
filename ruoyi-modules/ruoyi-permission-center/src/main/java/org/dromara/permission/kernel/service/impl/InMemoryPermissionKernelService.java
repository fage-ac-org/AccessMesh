package org.dromara.permission.kernel.service.impl;

import org.dromara.authcenter.api.enums.CapabilityType;
import org.dromara.authcenter.api.enums.DataScopeType;
import org.dromara.authcenter.api.model.CapabilityDefinition;
import org.dromara.authcenter.api.model.CustomCapabilityManifest;
import org.dromara.authcenter.api.model.CustomScopeDescriptor;
import org.dromara.authcenter.api.model.DataScopeDescriptor;
import org.dromara.authcenter.api.model.InterfaceDecisionResult;
import org.dromara.authcenter.api.model.InterfacePermissionRule;
import org.dromara.authcenter.api.model.InterfacePermissionSnapshot;
import org.dromara.authcenter.api.model.PermissionVersionInfo;
import org.dromara.authcenter.api.model.PrincipalContext;
import org.dromara.authcenter.api.request.CatalogRegistrationRequest;
import org.dromara.authcenter.api.request.DataScopeQueryRequest;
import org.dromara.authcenter.api.request.InterfacePermissionDecisionRequest;
import org.dromara.authcenter.api.request.InterfacePermissionSnapshotRequest;
import org.dromara.authcenter.api.request.PermissionVersionQueryRequest;
import org.dromara.permission.kernel.service.PermissionKernelService;
import org.springframework.stereotype.Service;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 内存版权限内核服务
 *
 * 当前仅用于混合内核第一阶段骨架，不承担最终授权模型。
 *
 * @author RuoYi-Cloud-Plus
 */
@Service
public class InMemoryPermissionKernelService implements PermissionKernelService {

    private final PathPatternParser pathPatternParser = new PathPatternParser();
    private final Map<String, List<CapabilityDefinition>> capabilityCatalog = new ConcurrentHashMap<>();
    private final Map<String, List<CustomCapabilityManifest>> customManifestCatalog = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> tenantVersionCounters = new ConcurrentHashMap<>();

    @Override
    public PermissionVersionInfo registerCatalog(CatalogRegistrationRequest request) {
        capabilityCatalog.put(buildCatalogKey(request.getTenantId(), request.getServiceCode()),
            new ArrayList<>(request.getCapabilityDefinitions()));
        customManifestCatalog.put(buildCatalogKey(request.getTenantId(), request.getServiceCode()),
            new ArrayList<>(request.getCustomManifests()));
        bumpVersion(request.getTenantId());
        return buildVersionInfo(request.getTenantId(), "CATALOG");
    }

    @Override
    public InterfacePermissionSnapshot queryInterfaceSnapshot(InterfacePermissionSnapshotRequest request) {
        PrincipalContext principalContext = request.getPrincipalContext();
        InterfacePermissionSnapshot snapshot = new InterfacePermissionSnapshot();
        snapshot.setTenantId(principalContext.getTenantId());
        snapshot.setSubjectKey(principalContext.getSubjectKey());
        snapshot.setPermissionVersion(resolveCurrentVersion(principalContext.getTenantId()));
        snapshot.setGeneratedAtEpochMilli(System.currentTimeMillis());
        snapshot.setRules(collectInterfaceRules(principalContext.getTenantId()));
        return snapshot;
    }

    @Override
    public InterfaceDecisionResult decideInterface(InterfacePermissionDecisionRequest request) {
        InterfacePermissionSnapshotRequest snapshotRequest = new InterfacePermissionSnapshotRequest();
        snapshotRequest.setPrincipalContext(request.getPrincipalContext());
        InterfacePermissionSnapshot snapshot = queryInterfaceSnapshot(snapshotRequest);
        Optional<InterfacePermissionRule> matchedRule = matchRule(snapshot, request.getServiceCode(),
            request.getRequestMethod(), request.getRequestPath());

        InterfaceDecisionResult result = new InterfaceDecisionResult();
        result.setAllowed(matchedRule.isPresent());
        result.setPermissionVersion(snapshot.getPermissionVersion());
        result.setMatchedCapabilityCode(matchedRule.map(InterfacePermissionRule::getCapabilityCode).orElse(null));
        result.setReason(matchedRule.isPresent() ? "matched-interface-rule" : "no-interface-rule");
        return result;
    }

    @Override
    public List<DataScopeDescriptor> queryDataScopes(DataScopeQueryRequest request) {
        boolean matched = capabilityCatalog.values().stream()
            .flatMap(List::stream)
            .anyMatch(item -> item.getCapabilityType() == CapabilityType.DATA
                && item.getCapabilityCode().equals(request.getCapabilityCode()));
        if (!matched) {
            return List.of();
        }
        DataScopeDescriptor descriptor = new DataScopeDescriptor();
        descriptor.setCapabilityCode(request.getCapabilityCode());
        descriptor.setScopeType(DataScopeType.SELF);
        descriptor.getSubjectIds().add(request.getPrincipalContext().getSubjectId());
        return List.of(descriptor);
    }

    @Override
    public List<CustomScopeDescriptor> queryCustomScopes(DataScopeQueryRequest request) {
        return customManifestCatalog.values().stream()
            .flatMap(List::stream)
            .filter(item -> item.getCapabilityCode().equals(request.getCapabilityCode()))
            .map(item -> {
                CustomScopeDescriptor descriptor = new CustomScopeDescriptor();
                descriptor.setCapabilityCode(item.getCapabilityCode());
                descriptor.setSchemaVersion("v1");
                descriptor.setHandlerHint(item.getHandlerHint());
                descriptor.setParameters(item.getParameterSchema());
                return descriptor;
            })
            .toList();
    }

    @Override
    public PermissionVersionInfo queryVersion(PermissionVersionQueryRequest request) {
        return buildVersionInfo(request.getTenantId(), request.getSubjectType() + ":" + request.getSubjectId());
    }

    private String buildCatalogKey(String tenantId, String serviceCode) {
        return tenantId + ":" + serviceCode;
    }

    private List<InterfacePermissionRule> collectInterfaceRules(String tenantId) {
        return capabilityCatalog.entrySet().stream()
            .filter(entry -> entry.getKey().startsWith(tenantId + ":"))
            .flatMap(entry -> entry.getValue().stream())
            .filter(item -> item.getCapabilityType() == CapabilityType.INTERFACE)
            .sorted(Comparator.comparing(CapabilityDefinition::getCapabilityCode))
            .map(item -> {
                InterfacePermissionRule rule = new InterfacePermissionRule();
                rule.setCapabilityCode(item.getCapabilityCode());
                rule.setServiceCode(item.getServiceCode());
                rule.setHttpMethod(item.getHttpMethod());
                rule.setPathPattern(item.getPathPattern());
                return rule;
            })
            .toList();
    }

    private Optional<InterfacePermissionRule> matchRule(InterfacePermissionSnapshot snapshot, String serviceCode,
                                                        String requestMethod, String requestPath) {
        return snapshot.getRules().stream()
            .filter(rule -> rule.getServiceCode() == null || rule.getServiceCode().equals(serviceCode))
            .filter(rule -> rule.getHttpMethod() == null || rule.getHttpMethod().equalsIgnoreCase(requestMethod))
            .filter(rule -> isPathMatch(rule.getPathPattern(), requestPath))
            .findFirst();
    }

    private boolean isPathMatch(String pathPattern, String requestPath) {
        if (pathPattern == null || pathPattern.isBlank()) {
            return false;
        }
        PathPattern pattern = pathPatternParser.parse(pathPattern);
        return pattern.matches(org.springframework.http.server.PathContainer.parsePath(requestPath));
    }

    private PermissionVersionInfo buildVersionInfo(String tenantId, String subjectKey) {
        PermissionVersionInfo info = new PermissionVersionInfo();
        info.setTenantId(tenantId);
        info.setSubjectKey(subjectKey);
        info.setPermissionVersion(resolveCurrentVersion(tenantId));
        info.setUpdatedAtEpochMilli(System.currentTimeMillis());
        return info;
    }

    private String resolveCurrentVersion(String tenantId) {
        long version = tenantVersionCounters.computeIfAbsent(tenantId, key -> new AtomicLong(0))
            .get();
        return tenantId + "-v" + version;
    }

    private String bumpVersion(String tenantId) {
        long version = tenantVersionCounters.computeIfAbsent(tenantId, key -> new AtomicLong(0))
            .incrementAndGet();
        return tenantId + "-v" + version;
    }
}
