package org.dromara.permission.service;

/**
 * 权限变更记录服务：写入变更日志，供本模块及后续其他模块调用
 *
 * @author RuoYi-Cloud-Plus
 */
public interface PermissionChangeLogService {

    /**
     * 写入一条变更记录
     *
     * @param tenantId    租户ID
     * @param bizDomainId 业务域ID，可为 null
     * @param entityType  变更实体类型（如 sync_user、sync_role、user_role 等）
     * @param entityId    被变更记录主键ID，批量时可传 0
     * @param operation   操作类型：INSERT/UPDATE/DELETE
     * @param oldSnapshot 变更前快照对象，会序列化为 JSON 存储；可为 null
     * @param newSnapshot 变更后快照对象，会序列化为 JSON 存储；可为 null
     * @param requestId   请求/追踪ID，可为 null
     * @param changeSource 变更来源：ADMIN/MQ_SYNC/API/SYSTEM 等
     */
    void writeChangeLog(Long tenantId, Long bizDomainId, String entityType, Long entityId,
                        String operation, Object oldSnapshot, Object newSnapshot,
                        String requestId, String changeSource);
}
