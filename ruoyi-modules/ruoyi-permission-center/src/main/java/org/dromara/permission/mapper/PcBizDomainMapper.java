package org.dromara.permission.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.permission.domain.PcBizDomain;

/**
 * 业务域表 biz_domain 数据层
 *
 * @author RuoYi-Cloud-Plus
 */
@Mapper
public interface PcBizDomainMapper extends BaseMapperPlus<PcBizDomain, PcBizDomain> {
}
