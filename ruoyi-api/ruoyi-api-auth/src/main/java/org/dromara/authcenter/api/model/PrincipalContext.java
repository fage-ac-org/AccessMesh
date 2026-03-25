package org.dromara.authcenter.api.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dromara.authcenter.api.enums.SubjectType;

/**
 * 主体上下文
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
@NoArgsConstructor
public class PrincipalContext implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String tenantId;

    @NotBlank
    private String subjectId;

    @NotNull
    private SubjectType subjectType;

    private String permissionVersion;

    private String serviceCode;

    private List<String> orgIds = new ArrayList<>();

    private List<String> positionIds = new ArrayList<>();

    @Valid
    private DelegationContext delegationContext;

    private Map<String, String> attributes = new LinkedHashMap<>();

    public String getSubjectKey() {
        return subjectType + ":" + subjectId;
    }
}
