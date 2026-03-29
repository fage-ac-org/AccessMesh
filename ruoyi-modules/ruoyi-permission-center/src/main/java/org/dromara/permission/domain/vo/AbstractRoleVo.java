package org.dromara.permission.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AbstractRoleVo {
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
    private LocalDateTime createdAt;
    private List<AbstractRoleVo> children;
}
