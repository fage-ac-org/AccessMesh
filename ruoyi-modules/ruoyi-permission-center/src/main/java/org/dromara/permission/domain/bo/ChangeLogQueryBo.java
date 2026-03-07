package org.dromara.permission.domain.bo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 变更记录查询条件
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
public class ChangeLogQueryBo {

    /** 租户ID（必填） */
    private Long tenantId;

    /** 抽象用户ID：仅查影响该用户的变更 */
    private Long abstractUserId;

    /** 抽象角色ID：仅查影响该角色的变更 */
    private Long abstractRoleId;

    /** 业务域ID */
    private Long bizDomainId;

    /** 变更实体类型：user_role、batch_user_role、role_resource_permission 等 */
    private String entityType;

    /** 请求/追踪ID */
    private String requestId;

    /** 变更时间起（created_at >= beginTime） */
    private LocalDateTime beginTime;

    /** 变更时间止（created_at <= endTime） */
    private LocalDateTime endTime;
}
