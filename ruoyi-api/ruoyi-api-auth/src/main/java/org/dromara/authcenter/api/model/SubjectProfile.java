package org.dromara.authcenter.api.model;

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
 * 主体资料
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
@NoArgsConstructor
public class SubjectProfile implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String tenantId;

    private String subjectId;

    private SubjectType subjectType;

    private String username;

    private String displayName;

    private String serviceCode;

    private List<String> orgIds = new ArrayList<>();

    private List<String> positionIds = new ArrayList<>();

    private Map<String, String> attributes = new LinkedHashMap<>();
}
