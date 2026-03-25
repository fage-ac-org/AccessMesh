package org.dromara.permission.kernel.service;

import org.dromara.authcenter.api.enums.DataScopeType;
import org.dromara.authcenter.api.model.DataScopeDescriptor;
import org.dromara.permission.kernel.service.impl.DatabaseDataScopeQueryService;
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
class DatabaseDataScopeQueryServiceTest {

    @Mock
    private PermissionKernelSnapshotMapper snapshotMapper;

    @InjectMocks
    private DatabaseDataScopeQueryService service;

    @Test
    void queryDataScopes_returnsSelfScopeWhenCapabilityGranted() {
        when(snapshotMapper.existsGrantedCapability(1L, 10001L, "ORDER_DATA", "VIEW")).thenReturn(true);

        List<DataScopeDescriptor> scopes = service.queryDataScopes(1L, 10001L, "ORDER_DATA:VIEW");

        assertEquals(1, scopes.size());
        assertEquals("ORDER_DATA:VIEW", scopes.get(0).getCapabilityCode());
        assertEquals(DataScopeType.SELF, scopes.get(0).getScopeType());
        assertEquals(List.of("10001"), scopes.get(0).getSubjectIds());
        verify(snapshotMapper).existsGrantedCapability(1L, 10001L, "ORDER_DATA", "VIEW");
    }

    @Test
    void queryDataScopes_withInvalidCapabilityCode_returnsEmptyList() {
        List<DataScopeDescriptor> scopes = service.queryDataScopes(1L, 10001L, "ORDER_DATA");

        assertTrue(scopes.isEmpty());
        verifyNoInteractions(snapshotMapper);
    }
}
