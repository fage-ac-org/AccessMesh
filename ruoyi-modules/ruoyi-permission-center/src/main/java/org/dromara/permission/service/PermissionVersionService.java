package org.dromara.permission.service;

import org.dromara.permission.domain.PcPermissionVersion;

/**
 * 权限版本服务
 *
 * @author RuoYi-Cloud-Plus
 */
public interface PermissionVersionService {

    /**
     * 查询租户当前权限版本；无记录时返回逻辑上的 v0
     */
    PcPermissionVersion queryCurrentVersion(Long tenantId);

    /**
     * 递增租户权限版本并记录触发来源
     */
    PcPermissionVersion bumpVersion(Long tenantId, String triggerEntityType, Long triggerEntityId, String remark);

    /**
     * 构建对外使用的版本号字符串
     */
    String buildVersionToken(Long tenantId, Long versionNo);
}
