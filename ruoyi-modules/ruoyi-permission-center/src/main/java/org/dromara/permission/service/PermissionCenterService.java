package org.dromara.permission.service;

import org.dromara.permission.domain.dto.*;
import org.dromara.system.api.model.LoginUser;

import java.util.Set;

/**
 * 权限中心服务：RBAC、数据范围、用户信息
 */
public interface PermissionCenterService {

    Set<String> getRolePermission(Long userId, String tenantId);

    Set<String> getMenuPermission(Long userId, String tenantId, String clientId);

    String getRoleCustom(Long roleId);

    String getDeptAndChild(Long deptId);

    LoginUser getUserInfo(Long userId, String tenantId);
}
