package org.dromara.permission.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.permission.domain.PcPermissionChangeLog;
import org.dromara.permission.domain.bo.ChangeLogQueryBo;

/**
 * 权限变更记录表 permission_change_log 数据层
 *
 * @author RuoYi-Cloud-Plus
 */
@Mapper
public interface PcPermissionChangeLogMapper extends BaseMapper<PcPermissionChangeLog> {

    /**
     * 分页查询变更记录（支持按用户、角色、业务域、实体类型、时间、requestId 过滤）
     */
    Page<PcPermissionChangeLog> selectChangeLogPage(Page<PcPermissionChangeLog> page, @Param("bo") ChangeLogQueryBo bo);
}
