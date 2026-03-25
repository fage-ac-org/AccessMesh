package org.dromara.authcenter.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 接口权限规则
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
@NoArgsConstructor
public class InterfacePermissionRule implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String capabilityCode;

    private String serviceCode;

    private String httpMethod;

    private String pathPattern;
}
