package org.dromara.permission.dubbo;

import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.dromara.common.tenant.helper.TenantHelper;
import org.dromara.permission.service.PermissionCenterService;
import org.dromara.system.api.RemotePermissionService;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 权限查询 Dubbo 实现，供 RuoYi 接入权限中心
 */
@RequiredArgsConstructor
@Service
@DubboService
public class RemotePermissionServiceImpl implements RemotePermissionService {

    private final PermissionCenterService permissionCenterService;

    @Override
    public Set<String> getRolePermission(Long userId) {
        String tenantId = TenantHelper.getTenantId();
        return permissionCenterService.getRolePermission(userId, tenantId != null ? tenantId : "000000");
    }

    @Override
    public Set<String> getMenuPermission(Long userId) {
        String tenantId = TenantHelper.getTenantId();
        return permissionCenterService.getMenuPermission(userId, tenantId != null ? tenantId : "000000", null);
    }
}
