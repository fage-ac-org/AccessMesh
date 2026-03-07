package org.dromara.permission.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 菜单权限查询入参（POST JSON）
 */
@Data
public class MenuPermissionReq {

    @NotNull(message = "userId 不能为空")
    private Long userId;
    @NotBlank(message = "tenantId 不能为空")
    private String tenantId;
    /** 可选，按应用过滤 */
    private String clientId;
}
