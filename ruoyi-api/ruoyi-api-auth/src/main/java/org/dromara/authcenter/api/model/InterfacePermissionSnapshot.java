package org.dromara.authcenter.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 接口权限快照
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
@NoArgsConstructor
public class InterfacePermissionSnapshot implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String tenantId;

    private String subjectKey;

    private String permissionVersion;

    private Long generatedAtEpochMilli;

    private List<InterfacePermissionRule> rules = new ArrayList<>();
}
