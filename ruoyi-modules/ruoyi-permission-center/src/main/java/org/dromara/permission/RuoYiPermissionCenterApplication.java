package org.dromara.permission;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * 通用权限中心：认证、RBAC、数据权限、多租户/多应用
 *
 * @author RuoYi-Cloud-Plus
 */
@EnableDubbo
@SpringBootApplication
public class RuoYiPermissionCenterApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(RuoYiPermissionCenterApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
    }
}
