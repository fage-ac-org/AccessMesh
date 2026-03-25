package org.dromara.permission.service.impl;

import org.dromara.permission.domain.PcResourceApiMapping;
import org.dromara.permission.mapper.PcResourceApiMappingMapper;
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
class ResourceApiMappingServiceImplTest {

    @Mock
    private PcResourceApiMappingMapper mapper;

    @InjectMocks
    private ResourceApiMappingServiceImpl service;

    @Test
    void listEnabledMappings_returnsMapperResult() {
        PcResourceApiMapping mapping = new PcResourceApiMapping();
        mapping.setTenantId(1L);
        mapping.setResourceEntityId(10L);
        mapping.setServiceCode("order-service");
        mapping.setHttpMethod("POST");
        mapping.setPathPattern("/api/orders/query");
        when(mapper.selectEnabledByTenantAndResourceIds(1L, List.of(10L, 11L))).thenReturn(List.of(mapping));

        List<PcResourceApiMapping> result = service.listEnabledMappings(1L, List.of(10L, 11L));

        assertEquals(1, result.size());
        assertEquals("order-service", result.get(0).getServiceCode());
        verify(mapper).selectEnabledByTenantAndResourceIds(1L, List.of(10L, 11L));
    }

    @Test
    void listEnabledMappings_withEmptyResourceIds_returnsEmptyList() {
        List<PcResourceApiMapping> result = service.listEnabledMappings(1L, List.of());

        assertTrue(result.isEmpty());
        verifyNoInteractions(mapper);
    }
}
