package org.dromara.permission.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 接口资源映射表 resource_api_mapping
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("resource_api_mapping")
public class PcResourceApiMapping extends PermissionBaseEntity {

    /** 主键 */
    @TableId("id")
    private Long id;

    /** 所属业务域ID，NULL 表示全局映射 */
    private Long bizDomainId;

    /** 关联的资源实体ID */
    private Long resourceEntityId;

    /** 服务编码 */
    private String serviceCode;

    /** HTTP 方法 */
    private String httpMethod;

    /** 路径模式 */
    private String pathPattern;

    /** 匹配优先级 */
    private Integer matchOrder;

    /** 是否启用 */
    private Boolean enabled;

    /** 扩展属性(JSON) */
    private String extra;
}
