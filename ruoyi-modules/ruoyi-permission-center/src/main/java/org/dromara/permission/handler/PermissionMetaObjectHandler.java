package org.dromara.permission.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.permission.domain.PermissionBaseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 权限中心实体自动填充处理器
 * 针对 PermissionBaseEntity 的 createdBy/createdAt/updatedBy/updatedAt 字段
 */
@Component
public class PermissionMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject.getOriginalObject() instanceof PermissionBaseEntity) {
            LocalDateTime now = LocalDateTime.now();
            Long userId = getLoginUserId();
            this.strictInsertFill(metaObject, "createdBy", Long.class, userId);
            this.strictInsertFill(metaObject, "createdAt", LocalDateTime.class, now);
            this.strictInsertFill(metaObject, "updatedBy", Long.class, userId);
            this.strictInsertFill(metaObject, "updatedAt", LocalDateTime.class, now);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.getOriginalObject() instanceof PermissionBaseEntity) {
            LocalDateTime now = LocalDateTime.now();
            Long userId = getLoginUserId();
            this.strictUpdateFill(metaObject, "updatedBy", Long.class, userId);
            this.strictUpdateFill(metaObject, "updatedAt", LocalDateTime.class, now);
        }
    }

    private Long getLoginUserId() {
        try {
            return LoginHelper.getUserId();
        } catch (Exception e) {
            return null;
        }
    }
}
