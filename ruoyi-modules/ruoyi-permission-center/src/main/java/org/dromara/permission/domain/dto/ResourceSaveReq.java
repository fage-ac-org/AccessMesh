package org.dromara.permission.domain.dto;

import lombok.Data;

/**
 * 资源实体保存请求（单条）
 */
@Data
public class ResourceSaveReq {
    private Long id;
    private Long tenantId;
    private Long bizDomainId;
    private Long parentId;
    private String code;
    private String name;
    private Integer resourceType;
    private String path;
    private Integer sortOrder;
    private String extra;
}
