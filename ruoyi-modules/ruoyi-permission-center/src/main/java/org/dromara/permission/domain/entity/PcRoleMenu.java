package org.dromara.permission.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 角色菜单关联（表 sys_role_menu）
 */
@Data
@TableName("sys_role_menu")
public class PcRoleMenu {

    private Long roleId;
    private Long menuId;
}
