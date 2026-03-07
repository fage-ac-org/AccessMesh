package org.dromara.system.dubbo;

import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.dromara.system.api.RemotePermissionService;
import org.dromara.system.service.ISysPermissionService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 权限服务（ruoyi.permission.source=local 时启用；接入权限中心时设为 center 则使用权限中心实现）
 */
@ConditionalOnProperty(name = "ruoyi.permission.source", havingValue = "local", matchIfMissing = true)
@RequiredArgsConstructor
@Service
@DubboService
public class RemotePermissionServiceImpl implements RemotePermissionService {

    private final ISysPermissionService permissionService;

    @Override
    public Set<String> getRolePermission(Long userId) {
        return permissionService.getRolePermission(userId);
    }

    @Override
    public Set<String> getMenuPermission(Long userId) {
        return permissionService.getMenuPermission(userId);
    }
}
