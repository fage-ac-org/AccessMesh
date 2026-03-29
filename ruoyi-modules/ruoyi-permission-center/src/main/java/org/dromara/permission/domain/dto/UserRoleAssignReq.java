package org.dromara.permission.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 批量分配角色请求
 */
@Data
public class UserRoleAssignReq {
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @NotNull(message = "用户ID不能为空")
    private Long abstractUserId;

    @NotEmpty(message = "角色列表不能为空")
    private List<Long> roleIds;

    private LocalDateTime validFrom;
    private LocalDateTime validTo;
}
