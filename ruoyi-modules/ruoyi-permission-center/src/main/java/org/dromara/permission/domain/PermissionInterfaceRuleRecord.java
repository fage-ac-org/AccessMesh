package org.dromara.permission.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接口权限规则查询记录
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
@NoArgsConstructor
public class PermissionInterfaceRuleRecord {

    private String resourceCode;

    private String operationCode;

    private String serviceCode;

    private String httpMethod;

    private String pathPattern;

    private Integer matchOrder;
}
