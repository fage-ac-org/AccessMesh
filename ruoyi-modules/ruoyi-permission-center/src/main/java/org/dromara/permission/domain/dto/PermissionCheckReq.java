package org.dromara.permission.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * 单次鉴权请求
 */
@Data
public class PermissionCheckReq {
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @NotNull(message = "用户ID不能为空")
    private Long abstractUserId;

    @NotNull(message = "资源ID不能为空")
    private Long resourceEntityId;

    @NotNull(message = "操作权限ID不能为空")
    private Long operationPermissionId;

    /** 可选，传入则只考虑该域内角色或全局角色 */
    private Long bizDomainId;

    /** 可选，供 condition 表达式使用 */
    private Map<String, String> context;
}
