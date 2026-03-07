package org.dromara.permission.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 权限生效条件表 permission_condition
 * expression 为 Java 表达式
 * 逻辑删除：基类 deleteFlag，0=未删除，删除时=id
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("permission_condition")
public class PcPermissionCondition extends PermissionBaseEntity {

    /** 主键 */
    @TableId("id")
    private Long id;

    /** 条件编码 */
    private String code;

    /** 名称 */
    private String name;

    /** Java 表达式 */
    private String expression;

    /** 说明/变量约定 */
    private String description;
}
