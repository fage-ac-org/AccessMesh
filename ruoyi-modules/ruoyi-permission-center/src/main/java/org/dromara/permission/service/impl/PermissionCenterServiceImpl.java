package org.dromara.permission.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.constant.TenantConstants;
import org.dromara.common.core.utils.StreamUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.permission.domain.entity.*;
import org.dromara.permission.mapper.*;
import org.dromara.permission.service.PermissionCenterService;
import org.dromara.system.api.model.LoginUser;
import org.dromara.system.api.model.RoleDTO;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限中心服务实现：多租户过滤，超级管理员/租户管理员通配
 */
@RequiredArgsConstructor
@Service
public class PermissionCenterServiceImpl implements PermissionCenterService {

    private final PcUserMapper userMapper;
    private final PcRoleMapper roleMapper;
    private final PcMenuMapper menuMapper;
    private final PcDeptMapper deptMapper;
    private final PcUserRoleMapper userRoleMapper;
    private final PcRoleMenuMapper roleMenuMapper;
    private final PcRoleDeptMapper roleDeptMapper;

    @Override
    public Set<String> getRolePermission(Long userId, String tenantId) {
        Set<String> roles = new HashSet<>();
        if (isSuperAdmin(userId)) {
            roles.add(TenantConstants.SUPER_ADMIN_ROLE_KEY);
            return roles;
        }
        List<Long> roleIds = userRoleMapper.selectList(
                new LambdaQueryWrapper<PcUserRole>()
                    .eq(PcUserRole::getUserId, userId)
                    .select(PcUserRole::getRoleId))
            .stream().map(PcUserRole::getRoleId).distinct().toList();
        if (CollUtil.isEmpty(roleIds)) {
            return roles;
        }
        List<PcRole> list = roleMapper.selectList(
            new LambdaQueryWrapper<PcRole>()
                .in(PcRole::getRoleId, roleIds)
                .eq(StringUtils.isNotBlank(tenantId), PcRole::getTenantId, tenantId)
                .eq(PcRole::getStatus, "0")
                .select(PcRole::getRoleKey));
        return list.stream().map(PcRole::getRoleKey).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
    }

    @Override
    public Set<String> getMenuPermission(Long userId, String tenantId, String clientId) {
        Set<String> perms = new HashSet<>();
        if (isSuperAdmin(userId)) {
            perms.add("*:*:*");
            return perms;
        }
        List<Long> roleIds = userRoleMapper.selectList(
                new LambdaQueryWrapper<PcUserRole>().eq(PcUserRole::getUserId, userId).select(PcUserRole::getRoleId))
            .stream().map(PcUserRole::getRoleId).distinct().toList();
        if (CollUtil.isEmpty(roleIds)) {
            return perms;
        }
        List<Long> menuIds = roleMenuMapper.selectList(
                new LambdaQueryWrapper<PcRoleMenu>().in(PcRoleMenu::getRoleId, roleIds).select(PcRoleMenu::getMenuId))
            .stream().map(PcRoleMenu::getMenuId).distinct().toList();
        if (CollUtil.isEmpty(menuIds)) {
            return perms;
        }
        List<PcMenu> menus = menuMapper.selectList(
            new LambdaQueryWrapper<PcMenu>()
                .in(PcMenu::getMenuId, menuIds)
                .eq(PcMenu::getStatus, "0")
                .select(PcMenu::getPerms));
        return menus.stream()
            .map(PcMenu::getPerms)
            .filter(StringUtils::isNotBlank)
            .collect(Collectors.toSet());
    }

    @Override
    public String getRoleCustom(Long roleId) {
        if (ObjectUtil.isNull(roleId)) {
            return "-1";
        }
        List<PcRoleDept> list = roleDeptMapper.selectList(
            new LambdaQueryWrapper<PcRoleDept>()
                .eq(PcRoleDept::getRoleId, roleId)
                .select(PcRoleDept::getDeptId));
        if (CollUtil.isNotEmpty(list)) {
            return StreamUtils.join(list, rd -> Convert.toStr(rd.getDeptId()));
        }
        return "-1";
    }

    @Override
    public String getDeptAndChild(Long deptId) {
        if (ObjectUtil.isNull(deptId)) {
            return "-1";
        }
        List<Long> deptIds = deptMapper.selectDeptAndChildById(deptId);
        return CollUtil.isNotEmpty(deptIds) ? StreamUtils.join(deptIds, Convert::toStr) : "-1";
    }

    @Override
    public LoginUser getUserInfo(Long userId, String tenantId) {
        PcUser user = userMapper.selectOne(
            new LambdaQueryWrapper<PcUser>()
                .eq(PcUser::getUserId, userId)
                .eq(StringUtils.isNotBlank(tenantId), PcUser::getTenantId, tenantId));
        if (user == null) {
            return null;
        }
        LoginUser loginUser = new LoginUser();
        loginUser.setTenantId(user.getTenantId());
        loginUser.setUserId(user.getUserId());
        loginUser.setDeptId(user.getDeptId());
        loginUser.setUsername(user.getUserName());
        loginUser.setNickname(user.getNickName());
        loginUser.setPassword(user.getPassword());
        loginUser.setUserType(user.getUserType());
        loginUser.setMenuPermission(getMenuPermission(userId, tenantId, null));
        loginUser.setRolePermission(getRolePermission(userId, tenantId));
        if (user.getDeptId() != null) {
            PcDept dept = deptMapper.selectById(user.getDeptId());
            if (dept != null) {
                loginUser.setDeptName(dept.getDeptName());
                loginUser.setDeptCategory(dept.getDeptCategory());
            }
        }
        List<Long> roleIds = userRoleMapper.selectList(
                new LambdaQueryWrapper<PcUserRole>().eq(PcUserRole::getUserId, userId).select(PcUserRole::getRoleId))
            .stream().map(PcUserRole::getRoleId).distinct().toList();
        if (CollUtil.isNotEmpty(roleIds)) {
            List<PcRole> roles = roleMapper.selectList(
                new LambdaQueryWrapper<PcRole>()
                    .in(PcRole::getRoleId, roleIds)
                    .eq(PcRole::getStatus, "0"));
            List<RoleDTO> roleDTOs = roles.stream().map(r -> {
                RoleDTO dto = new RoleDTO();
                dto.setRoleId(r.getRoleId());
                dto.setRoleName(r.getRoleName());
                dto.setRoleKey(r.getRoleKey());
                dto.setDataScope(r.getDataScope());
                return dto;
            }).toList();
            loginUser.setRoles(roleDTOs);
        } else {
            loginUser.setRoles(Collections.emptyList());
        }
        return loginUser;
    }

    private boolean isSuperAdmin(Long userId) {
        return userId != null && TenantConstants.SUPER_ADMIN_ID.equals(userId);
    }
}
