package org.dromara.permission.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.permission.domain.PcResourceEntity;

/**
 * 权限资源实体表 resource_entity 数据层
 *
 * @author RuoYi-Cloud-Plus
 */
@Mapper
public interface PcResourceEntityMapper extends BaseMapperPlus<PcResourceEntity, PcResourceEntity> {
}
