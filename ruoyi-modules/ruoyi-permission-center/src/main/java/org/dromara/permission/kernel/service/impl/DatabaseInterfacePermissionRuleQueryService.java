package org.dromara.permission.kernel.service.impl;

import lombok.RequiredArgsConstructor;
import org.dromara.authcenter.api.model.InterfacePermissionRule;
import org.dromara.permission.domain.PermissionInterfaceRuleRecord;
import org.dromara.permission.kernel.service.InterfacePermissionRuleQueryService;
import org.dromara.permission.mapper.PermissionKernelSnapshotMapper;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于数据库的接口权限规则查询服务
 *
 * @author RuoYi-Cloud-Plus
 */
@Service
@RequiredArgsConstructor
public class DatabaseInterfacePermissionRuleQueryService implements InterfacePermissionRuleQueryService {

    private final PermissionKernelSnapshotMapper snapshotMapper;

    @Override
    public List<InterfacePermissionRule> listRules(Long tenantId, Long abstractUserId) {
        if (tenantId == null || abstractUserId == null) {
            return List.of();
        }
        List<PermissionInterfaceRuleRecord> records = snapshotMapper.selectInterfaceRuleRecords(tenantId, abstractUserId);
        if (records == null || records.isEmpty()) {
            return List.of();
        }

        Map<String, InterfacePermissionRule> deduplicatedRules = new LinkedHashMap<>();
        for (PermissionInterfaceRuleRecord record : records) {
            InterfacePermissionRule rule = new InterfacePermissionRule();
            rule.setCapabilityCode(record.getResourceCode() + ":" + record.getOperationCode());
            rule.setServiceCode(record.getServiceCode());
            rule.setHttpMethod(record.getHttpMethod());
            rule.setPathPattern(record.getPathPattern());
            deduplicatedRules.putIfAbsent(buildRuleKey(rule), rule);
        }
        return List.copyOf(deduplicatedRules.values());
    }

    private String buildRuleKey(InterfacePermissionRule rule) {
        return String.join("|",
            defaultString(rule.getCapabilityCode()),
            defaultString(rule.getServiceCode()),
            defaultString(rule.getHttpMethod()),
            defaultString(rule.getPathPattern()));
    }

    private String defaultString(String value) {
        return value == null ? "" : value;
    }
}
