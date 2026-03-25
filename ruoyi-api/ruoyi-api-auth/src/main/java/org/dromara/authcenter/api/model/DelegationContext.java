package org.dromara.authcenter.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 委托上下文
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
@NoArgsConstructor
public class DelegationContext implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String delegationId;

    private String serviceSubjectId;

    private String delegatedUserId;

    private Long issuedAtEpochMilli;

    private Long expiresAtEpochMilli;

    private String reason;
}
