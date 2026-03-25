package org.dromara.authcenter.api.enums;

/**
 * 主体类型
 *
 * @author RuoYi-Cloud-Plus
 */
public enum SubjectType {

    USER,
    SERVICE,
    DELEGATED;

    public static SubjectType fromCode(String code) {
        if (code == null || code.isBlank()) {
            return USER;
        }
        for (SubjectType value : values()) {
            if (value.name().equalsIgnoreCase(code)) {
                return value;
            }
        }
        return USER;
    }
}
