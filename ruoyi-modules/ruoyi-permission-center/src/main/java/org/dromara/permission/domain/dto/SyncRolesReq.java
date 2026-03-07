package org.dromara.permission.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 同步角色（批量）请求体
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
public class SyncRolesReq {

    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    /** 业务域ID，null 表示全局角色 */
    private Long bizDomainId;

    @Valid
    @NotNull(message = "items不能为空")
    @Size(max = 500, message = "单批数量不得超过500")
    private List<SyncRoleItem> items;

    @Data
    public static class SyncRoleItem {
        @NotBlank(message = "externalId不能为空")
        private String externalId;
        @NotNull(message = "roleType不能为空")
        private Integer roleType;
        @NotBlank(message = "name不能为空")
        private String name;
        /** 父角色外部标识，空为根 */
        private String parentExternalId;
        private Integer sortOrder;
        private String extra;
    }
}
