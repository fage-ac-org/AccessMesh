package org.dromara.authcenter.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 权限版本信息
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
@NoArgsConstructor
public class PermissionVersionInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String tenantId;

    private String subjectKey;

    private String permissionVersion;

    private Long updatedAtEpochMilli;
}
