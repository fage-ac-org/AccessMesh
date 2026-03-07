package org.dromara.permission.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.permission.domain.PcUserRole;

/**
 * 用户-角色关联表 user_role 数据层
 *
 * @author RuoYi-Cloud-Plus
 */
@Mapper
public interface PcUserRoleMapper extends BaseMapperPlus<PcUserRole, PcUserRole> {
}
