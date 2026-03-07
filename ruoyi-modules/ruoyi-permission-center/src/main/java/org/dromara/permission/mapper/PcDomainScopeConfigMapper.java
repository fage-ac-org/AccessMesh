package org.dromara.permission.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.permission.domain.PcDomainScopeConfig;

/**
 * 域范围配置 domain_scope_config 数据层
 *
 * @author RuoYi-Cloud-Plus
 */
@Mapper
public interface PcDomainScopeConfigMapper extends BaseMapperPlus<PcDomainScopeConfig, PcDomainScopeConfig> {
}
