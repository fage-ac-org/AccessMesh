package org.dromara.permission.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户-角色关联表 user_role
 * valid_from/valid_to 为生效时间范围
 * 逻辑删除：基类 deleteFlag，0=未删除，删除时=id
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("user_role")
public class PcUserRole extends PermissionBaseEntity {

    /** 主键 */
    @TableId("id")
    private Long id;

    /** 抽象用户ID */
    private Long abstractUserId;

    /** 抽象角色ID */
    private Long abstractRoleId;

    /** 生效开始时间，NULL 不限制 */
    private Date validFrom;

    /** 生效结束时间，NULL 不限制 */
    private Date validTo;
}
