package org.dromara.permission.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 类型/枚举 KV 配置表 system_config
 * config_key 如 user_type、role_type、resource_type，type_value 为枚举整型
 * 逻辑删除：基类 deleteFlag，0=未删除，删除时=id
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("system_config")
public class PcSystemConfig extends PermissionBaseEntity {

    /** 主键 */
    @TableId("id")
    private Long id;

    /** 业务域ID，NULL 表示全局类型 */
    private Long bizDomainId;

    /** 类型键，如 user_type、role_type、resource_type */
    private String configKey;

    /** 枚举值 */
    private Integer typeValue;

    /** 显示名称 */
    private String name;

    /** 描述 */
    private String description;

    /** 排序 */
    private Integer sortOrder;
}
