package org.dromara.authcenter.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 接口鉴权结果
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
@NoArgsConstructor
public class InterfaceDecisionResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private boolean allowed;

    private String matchedCapabilityCode;

    private String permissionVersion;

    private String reason;
}
