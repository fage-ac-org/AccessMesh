package org.dromara.common.satoken.core.service;

import cn.dev33.satoken.stp.StpInterface;

import java.util.Collections;
import java.util.List;

/**
 * 仅认证、不鉴权的 StpInterface 实现。
 * <p>
 * 不校验角色/权限，仅校验登录态。getPermissionList/getRoleList 返回通配通过，
 * 所有 @SaCheckPermission、@SaCheckRole 均不拦截。
 * 可通过 ruoyi.permission.source=center 切换为 SaPermissionImpl 使用权限中心数据。
 * </p>
 *
 * @author RuoYi-Cloud-Plus
 */
public class AuthOnlyStpInterface implements StpInterface {

    /**
     * 菜单权限通配符，与框架内“全部权限”一致
     */
    private static final String MENU_PERMISSION_ALL = "*:*:*";

    /**
     * 超级管理员角色 key，与 TenantConstants.SUPER_ADMIN_ROLE_KEY 一致
     */
    private static final String SUPER_ADMIN_ROLE_KEY = "superadmin";

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return Collections.singletonList(MENU_PERMISSION_ALL);
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return Collections.singletonList(SUPER_ADMIN_ROLE_KEY);
    }
}
