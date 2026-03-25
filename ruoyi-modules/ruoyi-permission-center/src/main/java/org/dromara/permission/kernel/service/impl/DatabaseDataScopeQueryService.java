package org.dromara.permission.kernel.service.impl;

import lombok.RequiredArgsConstructor;
import org.dromara.authcenter.api.enums.DataScopeType;
import org.dromara.authcenter.api.model.DataScopeDescriptor;
import org.dromara.permission.mapper.PermissionKernelSnapshotMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 基于数据库的标准数据范围查询服务
 *
 * 当前阶段只提供最小标准实现：
 * 当 capability 对应的资源-操作已被授权时，返回当前主体的 SELF 范围。
 *
 * @author RuoYi-Cloud-Plus
 */
@Service
@RequiredArgsConstructor
public class DatabaseDataScopeQueryService {

    private final PermissionKernelSnapshotMapper snapshotMapper;

    public List<DataScopeDescriptor> queryDataScopes(Long tenantId, Long abstractUserId, String capabilityCode) {
        if (tenantId == null || abstractUserId == null || capabilityCode == null || capabilityCode.isBlank()) {
            return List.of();
        }
        int separatorIndex = capabilityCode.lastIndexOf(':');
        if (separatorIndex <= 0 || separatorIndex >= capabilityCode.length() - 1) {
            return List.of();
        }

        String resourceCode = capabilityCode.substring(0, separatorIndex);
        String operationCode = capabilityCode.substring(separatorIndex + 1);
        boolean granted = snapshotMapper.existsGrantedCapability(tenantId, abstractUserId, resourceCode, operationCode);
        if (!granted) {
            return List.of();
        }

        DataScopeDescriptor descriptor = new DataScopeDescriptor();
        descriptor.setCapabilityCode(capabilityCode);
        descriptor.setScopeType(DataScopeType.SELF);
        descriptor.setSubjectIds(List.of(String.valueOf(abstractUserId)));
        return List.of(descriptor);
    }
}
