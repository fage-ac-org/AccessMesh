package org.dromara.auth.form;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 取消第三方授权入参（JSON 对象）
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
public class UnlockSocialBody {

    /**
     * 第三方授权 socialId
     */
    @NotNull(message = "socialId 不能为空")
    private Long socialId;
}
