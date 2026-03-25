package org.dromara.authcenter.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

import org.dromara.authcenter.api.enums.CapabilityType;

/**
 * 能力定义
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
@NoArgsConstructor
public class CapabilityDefinition implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String capabilityCode;

    @NotNull
    private CapabilityType capabilityType;

    @NotBlank
    private String serviceCode;

    private String displayName;

    private String pathPattern;

    private String httpMethod;
}
