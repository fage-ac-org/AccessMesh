package org.dromara.permission.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.permission.domain.PcPermissionChangeLog;
import org.dromara.permission.mapper.PcPermissionChangeLogMapper;
import org.dromara.permission.service.PermissionChangeLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 权限变更记录服务实现：写入 permission_change_log，供同步模块及其他模块调用
 *
 * @author RuoYi-Cloud-Plus
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionChangeLogServiceImpl implements PermissionChangeLogService {

    private final PcPermissionChangeLogMapper changeLogMapper;

    @Override
    public void writeChangeLog(Long tenantId, Long bizDomainId, String entityType, Long entityId,
                               String operation, Object oldSnapshot, Object newSnapshot,
                               String requestId, String changeSource) {
        try {
            PcPermissionChangeLog logEntity = new PcPermissionChangeLog();
            logEntity.setTenantId(tenantId);
            logEntity.setBizDomainId(bizDomainId);
            logEntity.setEntityType(entityType);
            logEntity.setEntityId(entityId);
            logEntity.setOperation(operation);
            logEntity.setOldSnapshot(oldSnapshot != null ? JsonUtils.toJsonString(oldSnapshot) : null);
            logEntity.setNewSnapshot(newSnapshot != null ? JsonUtils.toJsonString(newSnapshot) : null);
            logEntity.setChangeSource(changeSource);
            logEntity.setRequestId(requestId);
            logEntity.setCreatedAt(LocalDateTime.now());
            changeLogMapper.insert(logEntity);
        } catch (Exception e) {
            log.error("writeChangeLog failed, entityType={}, entityId={}, operation={}", entityType, entityId, operation, e);
        }
    }
}
