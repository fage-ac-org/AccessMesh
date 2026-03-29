package org.dromara.permission.service;

import org.dromara.permission.domain.dto.DomainScopeListReq;
import org.dromara.permission.domain.dto.DomainScopeSaveReq;
import org.dromara.permission.domain.dto.IdsReq;
import org.dromara.permission.domain.vo.DomainScopeConfigVo;

import java.util.List;

/**
 * 域范围配置 domain_scope_config 服务
 */
public interface DomainScopeConfigService {

    List<DomainScopeConfigVo> list(DomainScopeListReq req);

    void save(DomainScopeSaveReq req);

    void remove(IdsReq req);
}
