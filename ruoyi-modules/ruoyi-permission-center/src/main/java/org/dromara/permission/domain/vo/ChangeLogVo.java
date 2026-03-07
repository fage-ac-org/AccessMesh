package org.dromara.permission.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 变更记录列表/详情 VO
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
public class ChangeLogVo {

    private Long id;
    private Long tenantId;
    private Long bizDomainId;
    private String entityType;
    private Long entityId;
    private String operation;
    private String oldSnapshot;
    private String newSnapshot;
    private String affectedAbstractUserIds;
    private String affectedAbstractRoleIds;
    private String changeReason;
    private String changeSource;
    private String requestId;
    private Long createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdAt;
}
