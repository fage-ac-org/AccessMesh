package org.dromara.authcenter.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

import org.dromara.authcenter.api.enums.SubjectType;

/**
 * 权限版本查询请求
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
@NoArgsConstructor
public class PermissionVersionQueryRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String tenantId;

    @NotBlank
    private String subjectId;

    @NotNull
    private SubjectType subjectType;
}
