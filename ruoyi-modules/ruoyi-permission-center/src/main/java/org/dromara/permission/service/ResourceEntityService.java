package org.dromara.permission.service;

import org.dromara.permission.domain.dto.IdsReq;
import org.dromara.permission.domain.dto.ResourceListReq;
import org.dromara.permission.domain.dto.ResourceSaveReq;
import org.dromara.permission.domain.vo.ResourceEntityVo;

import java.util.List;

/**
 * 资源实体 resource_entity 服务
 */
public interface ResourceEntityService {

    List<ResourceEntityVo> list(ResourceListReq req);

    void save(ResourceSaveReq req);

    void remove(IdsReq req);
}
