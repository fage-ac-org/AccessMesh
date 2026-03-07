package org.dromara.permission.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 冲突检测请求（可选组合）
 */
@Data
public class ConflictDetectReq {
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;
    private Long abstractUserId;
    private Long abstractRoleId;
    private Long resourceEntityId;
}
