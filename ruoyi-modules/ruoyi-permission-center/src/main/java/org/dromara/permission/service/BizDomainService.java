package org.dromara.permission.service;

import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.permission.domain.dto.DomainPageReq;
import org.dromara.permission.domain.dto.DomainSaveReq;
import org.dromara.permission.domain.dto.IdsReq;
import org.dromara.permission.domain.vo.BizDomainVo;

/**
 * 业务域 biz_domain 服务
 */
public interface BizDomainService {

    TableDataInfo<BizDomainVo> page(DomainPageReq req);

    void save(DomainSaveReq req);

    void remove(IdsReq req);
}
