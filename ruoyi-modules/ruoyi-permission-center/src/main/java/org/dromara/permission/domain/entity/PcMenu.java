package org.dromara.permission.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 权限中心-菜单（表 sys_menu）
 */
@Data
@TableName("sys_menu")
public class PcMenu {

    @TableId("menu_id")
    private Long menuId;
    private Long parentId;
    private String menuName;
    private String perms;
    private String status;
}
