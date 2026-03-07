package org.dromara.permission.domain.dto;

import lombok.Data;

/**
 * 抽象角色保存请求（单条）
 */
@Data
public class RoleSaveReq {
    private Long id;
    private Long tenantId;
    private Long bizDomainId;
    private Integer roleType;
    private Long parentId;
    private String externalId;
    private String name;
    private String path;
    private Integer sortOrder;
    private String extra;
}
