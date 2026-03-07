package org.dromara.permission.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.permission.domain.PcRoleResourcePermission;

/**
 * 角色-资源-操作中间表 role_resource_permission 数据层
 *
 * @author RuoYi-Cloud-Plus
 */
@Mapper
public interface PcRoleResourcePermissionMapper extends BaseMapperPlus<PcRoleResourcePermission, PcRoleResourcePermission> {
}
