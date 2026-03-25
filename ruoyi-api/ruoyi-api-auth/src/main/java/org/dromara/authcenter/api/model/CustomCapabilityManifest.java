package org.dromara.authcenter.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.dromara.authcenter.api.enums.CapabilityType;

/**
 * 自定义能力清单
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
@NoArgsConstructor
public class CustomCapabilityManifest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String capabilityCode;

    private CapabilityType capabilityType = CapabilityType.CUSTOM;

    private String serviceCode;

    private String displayName;

    private Map<String, Object> parameterSchema = new LinkedHashMap<>();

    private Map<String, Object> displaySchema = new LinkedHashMap<>();

    private Map<String, Object> validationRules = new LinkedHashMap<>();

    private String handlerHint;
}
