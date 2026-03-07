package org.dromara.permission.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * 类型枚举保存请求（单条或列表）
 */
@Data
public class SystemConfigSaveReq {
    private List<SystemConfigItem> items;

    @Data
    public static class SystemConfigItem {
        private Long id;
        private Long tenantId;
        private Long bizDomainId;
        private String configKey;
        private Integer typeValue;
        private String name;
        private String description;
        private Integer sortOrder;
    }
}
