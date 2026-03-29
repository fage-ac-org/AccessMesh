package org.dromara.auth.form;

import lombok.Data;

/**
 * 租户列表查询入参（JSON 对象，可为空体）
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
public class TenantListBody {

    /**
     * 可选：按域名过滤等扩展字段
     */
    private String domain;
}
