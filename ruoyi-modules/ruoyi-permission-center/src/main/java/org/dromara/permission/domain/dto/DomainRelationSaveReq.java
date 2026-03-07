package org.dromara.permission.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * 域关系保存请求（单条或列表）
 */
@Data
public class DomainRelationSaveReq {
    private List<DomainRelationItem> items;

    @Data
    public static class DomainRelationItem {
        private Long id;
        private Long tenantId;
        private Long bizDomainId;
        private String relationType;
        private Long leftRefId;
        private Long rightRefId;
        private Long defaultConditionId;
    }
}
