package org.dromara.permission.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 权限中心-用户（表 sys_user）
 */
@Data
@TableName("sys_user")
public class PcUser {

    @TableId("user_id")
    private Long userId;
    private String tenantId;
    private Long deptId;
    private String userName;
    private String nickName;
    private String userType;
    private String email;
    private String phonenumber;
    private String password;
    private String status;
    @TableLogic
    private String delFlag;
    private String remark;
    @TableField(exist = false)
    private Date createTime;
}
