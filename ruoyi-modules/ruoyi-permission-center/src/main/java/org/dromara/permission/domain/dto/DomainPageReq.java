package org.dromara.permission.domain.dto;

import lombok.Data;

/**
 * 业务域分页查询请求
 */
@Data
public class DomainPageReq {
    private Long tenantId;
    private String code;
    private String name;
    private Integer pageNum;
    private Integer pageSize;
}
