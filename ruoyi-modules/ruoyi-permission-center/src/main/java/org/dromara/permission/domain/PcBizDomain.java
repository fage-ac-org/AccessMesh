package org.dromara.permission.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 业务域表 biz_domain
 * 逻辑删除：基类 deleteFlag，0=未删除，删除时=id
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("biz_domain")
public class PcBizDomain extends PermissionBaseEntity {

    /** 主键 */
    @TableId("id")
    private Long id;

    /** 域编码 */
    private String code;

    /** 域名称 */
    private String name;

    /** 描述 */
    private String description;
}
