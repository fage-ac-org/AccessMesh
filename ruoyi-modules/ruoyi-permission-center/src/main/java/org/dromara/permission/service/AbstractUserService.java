package org.dromara.permission.service;

import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.permission.domain.dto.IdsReq;
import org.dromara.permission.domain.dto.UserPageReq;
import org.dromara.permission.domain.dto.UserSaveReq;
import org.dromara.permission.domain.vo.AbstractUserVo;

/**
 * 抽象用户 abstract_user 服务
 */
public interface AbstractUserService {

    TableDataInfo<AbstractUserVo> page(UserPageReq req);

    void save(UserSaveReq req);

    void remove(IdsReq req);
}
