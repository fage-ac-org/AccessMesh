package org.dromara.permission.kernel.service;

import org.dromara.authcenter.api.model.CustomScopeDescriptor;
import org.dromara.authcenter.api.model.DataScopeDescriptor;
import org.dromara.authcenter.api.model.InterfaceDecisionResult;
import org.dromara.authcenter.api.model.InterfacePermissionSnapshot;
import org.dromara.authcenter.api.model.PermissionVersionInfo;
import org.dromara.authcenter.api.request.CatalogRegistrationRequest;
import org.dromara.authcenter.api.request.DataScopeQueryRequest;
import org.dromara.authcenter.api.request.InterfacePermissionDecisionRequest;
import org.dromara.authcenter.api.request.InterfacePermissionSnapshotRequest;
import org.dromara.authcenter.api.request.PermissionVersionQueryRequest;

import java.util.List;

/**
 * 权限内核服务
 *
 * @author RuoYi-Cloud-Plus
 */
public interface PermissionKernelService {

    PermissionVersionInfo registerCatalog(CatalogRegistrationRequest request);

    InterfacePermissionSnapshot queryInterfaceSnapshot(InterfacePermissionSnapshotRequest request);

    InterfaceDecisionResult decideInterface(InterfacePermissionDecisionRequest request);

    List<DataScopeDescriptor> queryDataScopes(DataScopeQueryRequest request);

    List<CustomScopeDescriptor> queryCustomScopes(DataScopeQueryRequest request);

    PermissionVersionInfo queryVersion(PermissionVersionQueryRequest request);
}
