package org.dromara.permission.service;

import org.dromara.permission.domain.PcResourceApiMapping;

import java.util.Collection;
import java.util.List;

/**
 * 接口资源映射服务
 *
 * @author RuoYi-Cloud-Plus
 */
public interface ResourceApiMappingService {

    /**
     * 查询租户下指定资源的启用接口映射
     */
    List<PcResourceApiMapping> listEnabledMappings(Long tenantId, Collection<Long> resourceEntityIds);
}
