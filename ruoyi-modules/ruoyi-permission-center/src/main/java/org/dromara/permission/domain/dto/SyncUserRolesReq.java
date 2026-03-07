package org.dromara.permission.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 同步用户-角色请求体
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
public class SyncUserRolesReq {

    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @Valid
    @NotNull(message = "items不能为空")
    private List<SyncUserRoleItem> items;

    /** true 时按本批回收未出现在列表中的关联（该用户下不在 items 内的角色关联做逻辑删除） */
    private Boolean deleteNotInList = false;

    @Data
    public static class SyncUserRoleItem {
        @NotBlank(message = "userExternalId不能为空")
        private String userExternalId;
        @NotNull(message = "userType不能为空")
        private Integer userType;
        @NotBlank(message = "roleExternalId不能为空")
        private String roleExternalId;
        private LocalDateTime validFrom;
        private LocalDateTime validTo;
    }
}
