package org.dromara.permission.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 数据范围-角色自定义部门入参（POST JSON）
 */
@Data
public class DataScopeRoleCustomReq {

    @NotNull(message = "roleId 不能为空")
    private Long roleId;
}
