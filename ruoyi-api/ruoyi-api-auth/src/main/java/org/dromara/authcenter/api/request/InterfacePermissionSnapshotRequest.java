package org.dromara.authcenter.api.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

import org.dromara.authcenter.api.model.PrincipalContext;

/**
 * 快照查询请求
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
@NoArgsConstructor
public class InterfacePermissionSnapshotRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Valid
    @NotNull
    private PrincipalContext principalContext;
}
