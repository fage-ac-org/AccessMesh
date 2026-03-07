package org.dromara.permission.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 角色部门关联-自定义数据权限（表 sys_role_dept）
 */
@Data
@TableName("sys_role_dept")
public class PcRoleDept {

    private Long roleId;
    private Long deptId;
}
