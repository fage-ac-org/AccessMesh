package org.dromara.permission.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * 域引用保存请求（单条或列表），boundEntityId 须为全局实体
 */
@Data
public class DomainBindingSaveReq {
    private List<DomainBindingItem> items;

    @Data
    public static class DomainBindingItem {
        private Long id;
        private Long tenantId;
        private Long bizDomainId;
        private String boundType;
        private Long boundEntityId;
    }
}
