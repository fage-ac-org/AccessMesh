package org.dromara.permission.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.permission.domain.PcResourceApiMapping;

import java.util.Collection;
import java.util.List;

/**
 * 接口资源映射表 resource_api_mapping 数据层
 *
 * @author RuoYi-Cloud-Plus
 */
@Mapper
public interface PcResourceApiMappingMapper extends BaseMapper<PcResourceApiMapping> {

    /**
     * 查询租户下指定资源的启用接口映射
     */
    List<PcResourceApiMapping> selectEnabledByTenantAndResourceIds(@Param("tenantId") Long tenantId,
                                                                   @Param("resourceEntityIds") Collection<Long> resourceEntityIds);
}
