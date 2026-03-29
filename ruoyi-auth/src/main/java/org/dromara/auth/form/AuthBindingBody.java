package org.dromara.auth.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 第三方登录绑定授权入参（JSON 对象）
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
public class AuthBindingBody {

    /**
     * 登录来源
     */
    @NotBlank(message = "登录来源不能为空")
    private String source;

    /**
     * 租户ID
     */
    @NotBlank(message = "租户ID不能为空")
    private String tenantId;

    /**
     * 域名
     */
    @NotBlank(message = "域名不能为空")
    private String domain;
}
