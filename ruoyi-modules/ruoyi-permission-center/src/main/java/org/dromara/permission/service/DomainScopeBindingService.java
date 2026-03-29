package org.dromara.permission.service;

import org.dromara.permission.domain.dto.DomainBindingListReq;
import org.dromara.permission.domain.dto.DomainBindingSaveReq;
import org.dromara.permission.domain.dto.IdsReq;
import org.dromara.permission.domain.vo.DomainScopeBindingVo;

import java.util.List;

/**
 * 域引用绑定 domain_scope_binding 服务
 */
public interface DomainScopeBindingService {

    List<DomainScopeBindingVo> list(DomainBindingListReq req);

    void save(DomainBindingSaveReq req);

    void remove(IdsReq req);
}
