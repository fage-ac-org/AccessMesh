package org.dromara.permission.domain.dto;

import lombok.Data;

/**
 * 抽象用户保存请求（单条）
 */
@Data
public class UserSaveReq {
    private Long id;
    private Long tenantId;
    private Integer userType;
    private String externalId;
    private String name;
    private String extra;
}
