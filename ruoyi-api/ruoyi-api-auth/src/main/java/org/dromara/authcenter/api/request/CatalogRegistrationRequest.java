package org.dromara.authcenter.api.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.dromara.authcenter.api.model.CapabilityDefinition;
import org.dromara.authcenter.api.model.CustomCapabilityManifest;

/**
 * 能力目录注册请求
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
@NoArgsConstructor
public class CatalogRegistrationRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String tenantId;

    @NotBlank
    private String serviceCode;

    @Valid
    private List<CapabilityDefinition> capabilityDefinitions = new ArrayList<>();

    @Valid
    private List<CustomCapabilityManifest> customManifests = new ArrayList<>();
}
