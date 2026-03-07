package org.dromara.permission.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 同步用户（批量）请求体
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
public class SyncUsersReq {

    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @NotNull(message = "用户类型不能为空")
    private Integer userType;

    @Valid
    @NotNull(message = "items不能为空")
    private List<SyncUserItem> items;

    @Data
    public static class SyncUserItem {
        @NotBlank(message = "externalId不能为空")
        private String externalId;
        private String name;
        private String extra;
    }
}
