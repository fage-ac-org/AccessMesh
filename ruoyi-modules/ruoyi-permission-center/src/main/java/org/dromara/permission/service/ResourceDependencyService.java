package org.dromara.permission.service;

import org.dromara.permission.domain.dto.IdsReq;
import org.dromara.permission.domain.dto.ResourceDependencyListReq;
import org.dromara.permission.domain.dto.ResourceDependencySaveReq;
import org.dromara.permission.domain.vo.ResourceDependencyVo;

import java.util.List;

/**
 * 资源依赖 resource_dependency 服务
 */
public interface ResourceDependencyService {

    List<ResourceDependencyVo> list(ResourceDependencyListReq req);

    void save(ResourceDependencySaveReq req);

    void remove(IdsReq req);
}
