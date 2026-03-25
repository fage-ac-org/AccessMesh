package org.dromara.permission.kernel.service;

import org.dromara.authcenter.api.model.InterfacePermissionRule;

import java.util.List;

/**
 * 接口权限规则查询服务
 *
 * @author RuoYi-Cloud-Plus
 */
public interface InterfacePermissionRuleQueryService {

    /**
     * 查询用户当前可访问的接口规则
     */
    List<InterfacePermissionRule> listRules(Long tenantId, Long abstractUserId);
}
