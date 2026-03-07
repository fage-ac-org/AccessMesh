package org.dromara.permission.dubbo;

import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.dromara.common.tenant.helper.TenantHelper;
import org.dromara.permission.service.PermissionCenterService;
import org.dromara.system.api.RemoteDataScopeService;
import org.springframework.stereotype.Service;

/**
 * 数据权限 Dubbo 实现，供 RuoYi 接入权限中心
 */
@RequiredArgsConstructor
@Service
@DubboService
public class RemoteDataScopeServiceImpl implements RemoteDataScopeService {

    private final PermissionCenterService permissionCenterService;

    @Override
    public String getRoleCustom(Long roleId) {
        return permissionCenterService.getRoleCustom(roleId);
    }

    @Override
    public String getDeptAndChild(Long deptId) {
        return permissionCenterService.getDeptAndChild(deptId);
    }
}
