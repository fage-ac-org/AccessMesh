package org.dromara.permission.domain.dto;

import lombok.Data;

/**
 * 抽象用户分页查询请求
 */
@Data
public class UserPageReq {
    private Long tenantId;
    private Integer userType;
    private String externalId;
    private String name;
    private Integer pageNum;
    private Integer pageSize;
}
