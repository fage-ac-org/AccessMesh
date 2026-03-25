package org.dromara.gateway.config.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 网关接口鉴权配置
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
@NoArgsConstructor
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "gateway.authz")
public class PermissionAuthzProperties {

    /**
     * 是否启用接口鉴权入口
     */
    private boolean enabled = false;

    /**
     * 拉不到快照或无匹配规则时是否放行
     */
    private boolean failOpen = true;

    private String tenantHeader = "X-Tenant-Id";

    private String subjectTypeHeader = "X-Subject-Type";

    private String permissionVersionHeader = "X-Permission-Version";

    private String serviceCodeHeader = "X-Service-Code";
}
