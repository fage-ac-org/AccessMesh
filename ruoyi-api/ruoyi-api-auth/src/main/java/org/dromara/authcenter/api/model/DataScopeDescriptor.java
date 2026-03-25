package org.dromara.authcenter.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dromara.authcenter.api.enums.DataScopeType;

/**
 * 标准数据范围描述
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
@NoArgsConstructor
public class DataScopeDescriptor implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String capabilityCode;

    private DataScopeType scopeType;

    private List<String> orgIds = new ArrayList<>();

    private List<String> positionIds = new ArrayList<>();

    private List<String> subjectIds = new ArrayList<>();

    private Map<String, Object> parameters = new LinkedHashMap<>();
}
