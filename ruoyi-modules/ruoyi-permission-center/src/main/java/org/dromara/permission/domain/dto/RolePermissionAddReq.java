package org.dromara.permission.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 批量添加角色权限请求
 */
@Data
public class RolePermissionAddReq {
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @NotNull(message = "角色ID不能为空")
    private Long abstractRoleId;

    @Valid
    @NotEmpty(message = "权限项不能为空")
    private List<RolePermissionItem> items;

    @Data
    public static class RolePermissionItem {
        @NotNull(message = "资源ID不能为空")
        private Long resourceEntityId;
        @NotNull(message = "操作权限ID不能为空")
        private Long operationPermissionId;
        private Boolean canManage;
        private Long conditionId;
    }
}
