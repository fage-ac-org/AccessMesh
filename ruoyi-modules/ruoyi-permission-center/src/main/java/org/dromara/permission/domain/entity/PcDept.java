package org.dromara.permission.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 权限中心-部门（表 sys_dept）
 */
@Data
@TableName("sys_dept")
public class PcDept {

    @TableId("dept_id")
    private Long deptId;
    private String tenantId;
    private Long parentId;
    private String ancestors;
    private String deptName;
    private String deptCategory;
    private String status;
}
