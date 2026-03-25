package org.dromara.authcenter.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 自定义范围描述
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
@NoArgsConstructor
public class CustomScopeDescriptor implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String capabilityCode;

    private String schemaVersion;

    private String handlerHint;

    private Map<String, Object> parameters = new LinkedHashMap<>();
}
