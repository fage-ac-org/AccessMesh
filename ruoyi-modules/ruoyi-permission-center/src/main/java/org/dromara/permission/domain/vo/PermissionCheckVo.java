package org.dromara.permission.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 鉴权结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionCheckVo {
    private Boolean allowed;
    /** 原因：无角色 | 无授权 | 依赖不满足 | 条件不满足 */
    private String reason;

    public static PermissionCheckVo allow() {
        return new PermissionCheckVo(true, null);
    }

    public static PermissionCheckVo deny(String reason) {
        return new PermissionCheckVo(false, reason);
    }
}
