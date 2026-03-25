package org.dromara.permission.service.impl;

import lombok.RequiredArgsConstructor;
import org.dromara.permission.domain.PcResourceApiMapping;
import org.dromara.permission.mapper.PcResourceApiMappingMapper;
import org.dromara.permission.service.ResourceApiMappingService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 接口资源映射服务实现
 *
 * @author RuoYi-Cloud-Plus
 */
@Service
@RequiredArgsConstructor
public class ResourceApiMappingServiceImpl implements ResourceApiMappingService {

    private final PcResourceApiMappingMapper mapper;

    @Override
    public List<PcResourceApiMapping> listEnabledMappings(Long tenantId, Collection<Long> resourceEntityIds) {
        if (tenantId == null || resourceEntityIds == null || resourceEntityIds.isEmpty()) {
            return List.of();
        }
        return mapper.selectEnabledByTenantAndResourceIds(tenantId, resourceEntityIds);
    }
}
