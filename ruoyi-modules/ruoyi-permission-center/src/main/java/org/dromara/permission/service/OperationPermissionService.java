package org.dromara.permission.service;

import org.dromara.permission.domain.dto.IdsReq;
import org.dromara.permission.domain.dto.OperationListReq;
import org.dromara.permission.domain.dto.OperationSaveReq;
import org.dromara.permission.domain.vo.OperationPermissionVo;

import java.util.List;

/**
 * 操作权限 operation_permission 服务
 */
public interface OperationPermissionService {

    List<OperationPermissionVo> list(OperationListReq req);

    void save(OperationSaveReq req);

    void remove(IdsReq req);
}
