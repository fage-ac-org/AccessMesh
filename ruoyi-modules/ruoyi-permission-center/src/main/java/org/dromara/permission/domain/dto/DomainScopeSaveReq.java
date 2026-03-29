package org.dromara.permission.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * 域范围保存请求（单条或列表）
 */
@Data
public class DomainScopeSaveReq {
    private List<DomainScopeItem> items;

    @Data
    public static class DomainScopeItem {
        private Long id;
        private Long tenantId;
        private Long bizDomainId;
        private String scopeType;
        private Long scopeRefId;
    }
}
