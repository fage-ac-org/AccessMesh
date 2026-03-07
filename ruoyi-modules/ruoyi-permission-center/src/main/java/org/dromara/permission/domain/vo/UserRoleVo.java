package org.dromara.permission.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserRoleVo {
    private Long id;
    private Long tenantId;
    private Long abstractUserId;
    private Long abstractRoleId;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private LocalDateTime createdAt;
    /** 角色名称，可选填充 */
    private String roleName;
}
