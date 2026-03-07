package org.dromara.permission.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用户信息查询入参（POST JSON）
 */
@Data
public class UserInfoReq {

    @NotNull(message = "userId 不能为空")
    private Long userId;
    @NotBlank(message = "tenantId 不能为空")
    private String tenantId;
}
