package org.dromara.permission.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ResourceEntityVo {
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
    private LocalDateTime createdAt;
    private List<ResourceEntityVo> children;
}
