package org.dromara.permission.kernel.service;

import org.dromara.authcenter.api.enums.CapabilityType;
import org.dromara.authcenter.api.enums.DataScopeType;
import org.dromara.authcenter.api.enums.SubjectType;
import org.dromara.authcenter.api.model.CapabilityDefinition;
import org.dromara.authcenter.api.model.CustomCapabilityManifest;
import org.dromara.authcenter.api.model.CustomScopeDescriptor;
import org.dromara.authcenter.api.model.DataScopeDescriptor;
import org.dromara.authcenter.api.model.InterfaceDecisionResult;
import org.dromara.authcenter.api.model.PermissionVersionInfo;
import org.dromara.authcenter.api.model.PrincipalContext;
import org.dromara.authcenter.api.request.CatalogRegistrationRequest;
import org.dromara.authcenter.api.request.DataScopeQueryRequest;
import org.dromara.authcenter.api.request.InterfacePermissionDecisionRequest;
import org.dromara.authcenter.api.request.PermissionVersionQueryRequest;
import org.dromara.permission.kernel.service.impl.InMemoryPermissionKernelService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("InMemoryPermissionKernelService Tests")
@Tag("dev")
class InMemoryPermissionKernelServiceTest {

    private final InMemoryPermissionKernelService service = new InMemoryPermissionKernelService();

    @Test
    @DisplayName("registerCatalog should build version and allow matching interface")
    void registerCatalogShouldBuildVersionAndAllowMatchingInterface() {
        CapabilityDefinition interfaceCapability = new CapabilityDefinition();
        interfaceCapability.setCapabilityCode("order:query");
        interfaceCapability.setCapabilityType(CapabilityType.INTERFACE);
        interfaceCapability.setServiceCode("order-service");
        interfaceCapability.setDisplayName("订单查询");
        interfaceCapability.setHttpMethod("POST");
        interfaceCapability.setPathPattern("/api/orders/query");

        CatalogRegistrationRequest request = new CatalogRegistrationRequest();
        request.setTenantId("000000");
        request.setServiceCode("order-service");
        request.setCapabilityDefinitions(List.of(interfaceCapability));

        PermissionVersionInfo versionInfo = service.registerCatalog(request);

        PrincipalContext principalContext = new PrincipalContext();
        principalContext.setTenantId("000000");
        principalContext.setSubjectId("10001");
        principalContext.setSubjectType(SubjectType.USER);
        principalContext.setPermissionVersion(versionInfo.getPermissionVersion());

        InterfacePermissionDecisionRequest decisionRequest = new InterfacePermissionDecisionRequest();
        decisionRequest.setPrincipalContext(principalContext);
        decisionRequest.setServiceCode("order-service");
        decisionRequest.setRequestMethod("POST");
        decisionRequest.setRequestPath("/api/orders/query");

        InterfaceDecisionResult result = service.decideInterface(decisionRequest);

        assertTrue(result.isAllowed());
        assertEquals("order:query", result.getMatchedCapabilityCode());
        assertEquals(versionInfo.getPermissionVersion(), result.getPermissionVersion());
    }

    @Test
    @DisplayName("data and custom scope query should return standard descriptors")
    void dataAndCustomScopeQueryShouldReturnStandardDescriptors() {
        CapabilityDefinition dataCapability = new CapabilityDefinition();
        dataCapability.setCapabilityCode("order:data:self");
        dataCapability.setCapabilityType(CapabilityType.DATA);
        dataCapability.setServiceCode("order-service");
        dataCapability.setDisplayName("订单本人数据");

        CustomCapabilityManifest customManifest = new CustomCapabilityManifest();
        customManifest.setCapabilityCode("order:custom:warehouse");
        customManifest.setCapabilityType(CapabilityType.CUSTOM);
        customManifest.setServiceCode("order-service");
        customManifest.setDisplayName("仓库特殊权限");
        customManifest.setHandlerHint("warehouseScopeHandler");
        customManifest.setParameterSchema(Map.of("warehouseId", "string"));

        CatalogRegistrationRequest request = new CatalogRegistrationRequest();
        request.setTenantId("000000");
        request.setServiceCode("order-service");
        request.setCapabilityDefinitions(List.of(dataCapability));
        request.setCustomManifests(List.of(customManifest));
        service.registerCatalog(request);

        PrincipalContext principalContext = new PrincipalContext();
        principalContext.setTenantId("000000");
        principalContext.setSubjectId("10001");
        principalContext.setSubjectType(SubjectType.USER);

        DataScopeQueryRequest dataScopeRequest = new DataScopeQueryRequest();
        dataScopeRequest.setPrincipalContext(principalContext);
        dataScopeRequest.setCapabilityCode("order:data:self");

        List<DataScopeDescriptor> dataScopes = service.queryDataScopes(dataScopeRequest);
        List<CustomScopeDescriptor> customScopes = service.queryCustomScopes(new DataScopeQueryRequest(principalContext, "order:custom:warehouse"));

        assertFalse(dataScopes.isEmpty());
        assertEquals(DataScopeType.SELF, dataScopes.get(0).getScopeType());
        assertFalse(customScopes.isEmpty());
        assertEquals("warehouseScopeHandler", customScopes.get(0).getHandlerHint());
    }

    @Test
    @DisplayName("queryVersion should reflect current tenant version")
    void queryVersionShouldReflectCurrentTenantVersion() {
        PermissionVersionQueryRequest request = new PermissionVersionQueryRequest();
        request.setTenantId("000000");
        request.setSubjectId("10001");
        request.setSubjectType(SubjectType.USER);

        PermissionVersionInfo versionInfo = service.queryVersion(request);

        assertEquals("USER:10001", versionInfo.getSubjectKey());
        assertTrue(versionInfo.getPermissionVersion().startsWith("000000-v"));
    }
}
