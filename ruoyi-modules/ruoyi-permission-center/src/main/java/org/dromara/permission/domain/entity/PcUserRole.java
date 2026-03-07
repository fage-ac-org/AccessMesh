package org.dromara.permission.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户角色关联（表 sys_user_role）
 */
@Data
@TableName("sys_user_role")
public class PcUserRole {

    private Long userId;
    private Long roleId;
}
