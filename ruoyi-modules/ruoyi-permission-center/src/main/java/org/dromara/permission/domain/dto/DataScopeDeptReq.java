package org.dromara.permission.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 数据范围-部门及子部门入参（POST JSON）
 */
@Data
public class DataScopeDeptReq {

    @NotNull(message = "deptId 不能为空")
    private Long deptId;
}
