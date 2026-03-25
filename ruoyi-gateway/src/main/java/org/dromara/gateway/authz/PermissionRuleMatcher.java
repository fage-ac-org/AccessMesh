package org.dromara.gateway.authz;

import org.dromara.authcenter.api.model.InterfacePermissionRule;
import org.dromara.authcenter.api.model.InterfacePermissionSnapshot;
import org.springframework.http.server.PathContainer;
import org.springframework.stereotype.Component;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Optional;

/**
 * 接口权限规则匹配器
 *
 * @author RuoYi-Cloud-Plus
 */
@Component
public class PermissionRuleMatcher {

    private final PathPatternParser pathPatternParser = new PathPatternParser();

    public Optional<InterfacePermissionRule> match(InterfacePermissionSnapshot snapshot, String serviceCode,
                                                   String requestMethod, String requestPath) {
        return snapshot.getRules().stream()
            .filter(rule -> rule.getServiceCode() == null || rule.getServiceCode().isBlank() || rule.getServiceCode().equals(serviceCode))
            .filter(rule -> rule.getHttpMethod() == null || rule.getHttpMethod().isBlank() || rule.getHttpMethod().equalsIgnoreCase(requestMethod))
            .filter(rule -> isPathMatch(rule.getPathPattern(), requestPath))
            .findFirst();
    }

    private boolean isPathMatch(String patternText, String requestPath) {
        if (patternText == null || patternText.isBlank()) {
            return false;
        }
        PathPattern pattern = pathPatternParser.parse(patternText);
        return pattern.matches(PathContainer.parsePath(requestPath));
    }
}
