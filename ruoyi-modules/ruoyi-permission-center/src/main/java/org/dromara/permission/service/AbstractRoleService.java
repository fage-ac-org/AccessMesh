package org.dromara.permission.service;

import org.dromara.permission.domain.dto.IdsReq;
import org.dromara.permission.domain.dto.RoleListReq;
import org.dromara.permission.domain.dto.RoleSaveReq;
import org.dromara.permission.domain.vo.AbstractRoleVo;

import java.util.List;

/**
 * 抽象角色 abstract_role 服务
 */
public interface AbstractRoleService {

    List<AbstractRoleVo> list(RoleListReq req);

    void save(RoleSaveReq req);

    void remove(IdsReq req);
}
