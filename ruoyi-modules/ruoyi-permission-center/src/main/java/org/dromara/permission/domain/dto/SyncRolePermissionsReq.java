package org.dromara.permission.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 同步角色-权限请求体
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
public class SyncRolePermissionsReq {

    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @NotBlank(message = "roleExternalId不能为空")
    private String roleExternalId;

    /** 角色所属业务域ID，用于解析 roleExternalId（可空） */
    private Long bizDomainId;

    @Valid
    @NotNull(message = "items不能为空")
    private List<SyncRolePermissionItem> items;

    /** true 时按本批移除该角色下未在 items 中的权限（逻辑删除） */
    private Boolean deleteNotInList = false;

    @Data
    public static class SyncRolePermissionItem {
        @NotBlank(message = "resourceCode不能为空")
        private String resourceCode;
        @NotBlank(message = "operationCode不能为空")
        private String operationCode;
        private Boolean canManage = false;
        /** 条件编码，可选 */
        private String conditionCode;
    }
}
