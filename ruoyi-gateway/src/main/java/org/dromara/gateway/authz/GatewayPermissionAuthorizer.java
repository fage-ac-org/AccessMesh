package org.dromara.gateway.authz;

import org.dromara.authcenter.api.model.InterfacePermissionSnapshot;
import org.dromara.authcenter.api.model.PrincipalContext;
import org.dromara.gateway.config.properties.PermissionAuthzProperties;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

/**
 * 网关接口鉴权入口
 *
 * @author RuoYi-Cloud-Plus
 */
@Component
public class GatewayPermissionAuthorizer {

    private final PermissionAuthzProperties properties;
    private final PrincipalContextResolver principalContextResolver;
    private final PermissionSnapshotClient permissionSnapshotClient;
    private final PermissionRuleMatcher permissionRuleMatcher;

    public GatewayPermissionAuthorizer(PermissionAuthzProperties properties,
                                       PrincipalContextResolver principalContextResolver,
                                       PermissionSnapshotClient permissionSnapshotClient,
                                       PermissionRuleMatcher permissionRuleMatcher) {
        this.properties = properties;
        this.principalContextResolver = principalContextResolver;
        this.permissionSnapshotClient = permissionSnapshotClient;
        this.permissionRuleMatcher = permissionRuleMatcher;
    }

    public void authorize(ServerHttpRequest request) {
        if (!properties.isEnabled()) {
            return;
        }
        PrincipalContext principalContext = principalContextResolver.resolve(request);
        InterfacePermissionSnapshot snapshot = permissionSnapshotClient.loadSnapshot(principalContext);
        boolean matched = permissionRuleMatcher.match(snapshot, principalContext.getServiceCode(),
            request.getMethod() == null ? "GET" : request.getMethod().name(), request.getPath().value()).isPresent();
        if (matched || properties.isFailOpen() || snapshot.getRules().isEmpty()) {
            return;
        }
        throw new GatewayPermissionDeniedException("接口访问被权限快照拒绝");
    }
}
