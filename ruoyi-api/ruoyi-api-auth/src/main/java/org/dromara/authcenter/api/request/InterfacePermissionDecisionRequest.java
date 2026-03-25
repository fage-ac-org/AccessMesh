package org.dromara.authcenter.api.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

import org.dromara.authcenter.api.model.PrincipalContext;

/**
 * 接口鉴权请求
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
@NoArgsConstructor
public class InterfacePermissionDecisionRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Valid
    @NotNull
    private PrincipalContext principalContext;

    @NotBlank
    private String serviceCode;

    @NotBlank
    private String requestPath;

    @NotBlank
    private String requestMethod;
}
