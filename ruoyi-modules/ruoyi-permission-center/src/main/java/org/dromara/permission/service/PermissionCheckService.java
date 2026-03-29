package org.dromara.permission.service;

import org.dromara.permission.domain.dto.PermissionCheckReq;
import org.dromara.permission.domain.vo.PermissionCheckVo;

/**
 * 鉴权服务：单次 check(user, resource, operation)
 */
public interface PermissionCheckService {

    PermissionCheckVo check(PermissionCheckReq req);
}
