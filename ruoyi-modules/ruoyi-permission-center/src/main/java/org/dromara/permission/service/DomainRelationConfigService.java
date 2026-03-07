package org.dromara.permission.service;

import org.dromara.permission.domain.dto.DomainRelationListReq;
import org.dromara.permission.domain.dto.DomainRelationSaveReq;
import org.dromara.permission.domain.dto.IdsReq;
import org.dromara.permission.domain.vo.DomainRelationConfigVo;

import java.util.List;

/**
 * 域关系配置 domain_relation_config 服务
 */
public interface DomainRelationConfigService {

    List<DomainRelationConfigVo> list(DomainRelationListReq req);

    void save(DomainRelationSaveReq req);

    void remove(IdsReq req);
}
