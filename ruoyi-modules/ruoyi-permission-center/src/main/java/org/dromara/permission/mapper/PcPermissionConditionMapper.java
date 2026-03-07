package org.dromara.permission.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.permission.domain.PcPermissionCondition;

/**
 * 权限生效条件表 permission_condition 数据层
 *
 * @author RuoYi-Cloud-Plus
 */
@Mapper
public interface PcPermissionConditionMapper extends BaseMapperPlus<PcPermissionCondition, PcPermissionCondition> {
}
