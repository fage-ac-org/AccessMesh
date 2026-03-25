package org.dromara.gateway.authz;

import org.dromara.authcenter.api.model.InterfacePermissionSnapshot;
import org.dromara.authcenter.api.model.PrincipalContext;

/**
 * 权限快照客户端
 *
 * @author RuoYi-Cloud-Plus
 */
public interface PermissionSnapshotClient {

    InterfacePermissionSnapshot loadSnapshot(PrincipalContext principalContext);
}
