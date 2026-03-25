package org.dromara.authcenter.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 委托撤销请求
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
@NoArgsConstructor
public class DelegationRevokeRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String tenantId;

    @NotBlank
    private String delegationId;
}
