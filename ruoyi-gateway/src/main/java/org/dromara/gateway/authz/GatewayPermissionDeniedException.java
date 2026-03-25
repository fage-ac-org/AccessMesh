package org.dromara.gateway.authz;

/**
 * 网关接口权限拒绝异常
 *
 * @author RuoYi-Cloud-Plus
 */
public class GatewayPermissionDeniedException extends RuntimeException {

    public GatewayPermissionDeniedException(String message) {
        super(message);
    }
}
