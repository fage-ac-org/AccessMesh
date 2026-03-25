package org.dromara.authcenter.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

import org.dromara.authcenter.api.enums.SubjectType;

/**
 * 身份上下文签发请求
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
@NoArgsConstructor
public class IdentityContextIssueRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String tenantId;

    @NotBlank
    private String subjectId;

    @NotNull
    private SubjectType subjectType;

    private String permissionVersion;

    private String delegationId;
}
