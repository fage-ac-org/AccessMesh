package org.dromara.permission.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 权限中心-角色（表 sys_role）
 */
@Data
@TableName("sys_role")
public class PcRole {

    @TableId("role_id")
    private Long roleId;
    private String tenantId;
    private String roleName;
    private String roleKey;
    private String dataScope;
    private String status;
    @TableLogic
    private String delFlag;
}
