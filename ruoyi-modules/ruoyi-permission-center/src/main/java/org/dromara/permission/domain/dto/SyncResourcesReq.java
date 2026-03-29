package org.dromara.permission.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 同步资源（批量）请求体
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
public class SyncResourcesReq {

    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    /** 业务域ID，null 表示全局资源 */
    private Long bizDomainId;

    /** 资源类型 */
    private Integer resourceType;

    @Valid
    @NotNull(message = "items不能为空")
    @Size(max = 500, message = "单批数量不得超过500")
    private List<SyncResourceItem> items;

    @Data
    public static class SyncResourceItem {
        @NotBlank(message = "code不能为空")
        private String code;
        @NotBlank(message = "name不能为空")
        private String name;
        /** 父资源 code，空为根 */
        private String parentCode;
        private Integer sortOrder;
        private String extra;
    }
}
