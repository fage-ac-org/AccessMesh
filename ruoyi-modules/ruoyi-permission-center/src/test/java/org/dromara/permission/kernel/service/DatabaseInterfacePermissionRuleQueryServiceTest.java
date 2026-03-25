package org.dromara.permission.kernel.service;

import org.dromara.authcenter.api.model.InterfacePermissionRule;
import org.dromara.permission.domain.PermissionInterfaceRuleRecord;
import org.dromara.permission.kernel.service.impl.DatabaseInterfacePermissionRuleQueryService;
import org.dromara.permission.mapper.PermissionKernelSnapshotMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("dev")
class DatabaseInterfacePermissionRuleQueryServiceTest {

    @Mock
    private PermissionKernelSnapshotMapper snapshotMapper;

    @InjectMocks
    private DatabaseInterfacePermissionRuleQueryService service;

    @Test
    void listRules_mapsPersistentRecordsToInterfaceRules() {
        PermissionInterfaceRuleRecord first = new PermissionInterfaceRuleRecord();
        first.setResourceCode("ORDER_API");
        first.setOperationCode("VIEW");
        first.setServiceCode("order-service");
        first.setHttpMethod("GET");
        first.setPathPattern("/api/orders/**");
        first.setMatchOrder(10);

        PermissionInterfaceRuleRecord duplicate = new PermissionInterfaceRuleRecord();
        duplicate.setResourceCode("ORDER_API");
        duplicate.setOperationCode("VIEW");
        duplicate.setServiceCode("order-service");
        duplicate.setHttpMethod("GET");
        duplicate.setPathPattern("/api/orders/**");
        duplicate.setMatchOrder(20);

        PermissionInterfaceRuleRecord second = new PermissionInterfaceRuleRecord();
        second.setResourceCode("ORDER_API");
        second.setOperationCode("EDIT");
        second.setServiceCode("order-service");
        second.setHttpMethod("POST");
        second.setPathPattern("/api/orders");
        second.setMatchOrder(30);

        when(snapshotMapper.selectInterfaceRuleRecords(1L, 10001L)).thenReturn(List.of(first, duplicate, second));

        List<InterfacePermissionRule> rules = service.listRules(1L, 10001L);

        assertEquals(2, rules.size());
        assertEquals("ORDER_API:VIEW", rules.get(0).getCapabilityCode());
        assertEquals("GET", rules.get(0).getHttpMethod());
        assertEquals("/api/orders/**", rules.get(0).getPathPattern());
        assertEquals("ORDER_API:EDIT", rules.get(1).getCapabilityCode());
        verify(snapshotMapper).selectInterfaceRuleRecords(1L, 10001L);
    }

    @Test
    void listRules_withInvalidArgs_returnsEmptyList() {
        List<InterfacePermissionRule> rules = service.listRules(null, 10001L);

        assertTrue(rules.isEmpty());
        verifyNoInteractions(snapshotMapper);
    }
}
