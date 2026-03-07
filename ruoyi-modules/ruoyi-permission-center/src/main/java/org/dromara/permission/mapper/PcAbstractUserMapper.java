package org.dromara.permission.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.permission.domain.PcAbstractUser;

/**
 * 抽象用户表 abstract_user 数据层
 *
 * @author RuoYi-Cloud-Plus
 */
@Mapper
public interface PcAbstractUserMapper extends BaseMapperPlus<PcAbstractUser, PcAbstractUser> {
}
