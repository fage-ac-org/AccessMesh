package org.dromara.permission.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.dromara.permission.domain.PcPermissionChangeLog;

/**
 * 权限变更记录表 permission_change_log 数据层
 *
 * @author RuoYi-Cloud-Plus
 */
@Mapper
public interface PcPermissionChangeLogMapper extends BaseMapper<PcPermissionChangeLog> {
}
