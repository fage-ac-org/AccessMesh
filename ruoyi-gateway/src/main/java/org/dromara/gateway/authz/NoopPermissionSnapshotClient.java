package org.dromara.gateway.authz;

import org.dromara.authcenter.api.model.InterfacePermissionSnapshot;
import org.dromara.authcenter.api.model.PrincipalContext;
import org.springframework.stereotype.Component;

/**
 * 权限快照客户端占位实现
 *
 * 真正的远程拉取将在后续阶段接入 permission-center。
 *
 * @author RuoYi-Cloud-Plus
 */
@Component
public class NoopPermissionSnapshotClient implements PermissionSnapshotClient {

    @Override
    public InterfacePermissionSnapshot loadSnapshot(PrincipalContext principalContext) {
        InterfacePermissionSnapshot snapshot = new InterfacePermissionSnapshot();
        snapshot.setTenantId(principalContext.getTenantId());
        snapshot.setSubjectKey(principalContext.getSubjectKey());
        snapshot.setPermissionVersion(principalContext.getPermissionVersion());
        snapshot.setGeneratedAtEpochMilli(System.currentTimeMillis());
        return snapshot;
    }
}
