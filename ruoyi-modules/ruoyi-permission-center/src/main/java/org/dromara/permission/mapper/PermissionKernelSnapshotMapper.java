package org.dromara.permission.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.permission.domain.PermissionInterfaceRuleRecord;

import java.util.List;

/**
 * kernel 快照查询数据层
 *
 * @author RuoYi-Cloud-Plus
 */
@Mapper
public interface PermissionKernelSnapshotMapper {

    /**
     * 查询用户当前可访问的接口规则记录
     */
    List<PermissionInterfaceRuleRecord> selectInterfaceRuleRecords(@Param("tenantId") Long tenantId,
                                                                  @Param("abstractUserId") Long abstractUserId);

    /**
     * 判断用户是否拥有指定 capability 对应的资源-操作授权
     */
    boolean existsGrantedCapability(@Param("tenantId") Long tenantId,
                                    @Param("abstractUserId") Long abstractUserId,
                                    @Param("resourceCode") String resourceCode,
                                    @Param("operationCode") String operationCode);
}
