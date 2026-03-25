package org.dromara.gateway.authz;

import org.dromara.authcenter.api.model.InterfacePermissionRule;
import org.dromara.authcenter.api.model.InterfacePermissionSnapshot;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("PermissionRuleMatcher Tests")
@Tag("dev")
class PermissionRuleMatcherTest {

    private final PermissionRuleMatcher matcher = new PermissionRuleMatcher();

    @Test
    @DisplayName("match should find rule with same service method and path")
    void matchShouldFindRuleWithSameServiceMethodAndPath() {
        InterfacePermissionRule rule = new InterfacePermissionRule();
        rule.setServiceCode("order-service");
        rule.setHttpMethod("POST");
        rule.setPathPattern("/api/orders/**");

        InterfacePermissionSnapshot snapshot = new InterfacePermissionSnapshot();
        snapshot.setRules(List.of(rule));

        Optional<InterfacePermissionRule> matched = matcher.match(snapshot, "order-service", "POST", "/api/orders/query");

        assertTrue(matched.isPresent());
    }

    @Test
    @DisplayName("match should ignore rule when http method mismatches")
    void matchShouldIgnoreRuleWhenHttpMethodMismatches() {
        InterfacePermissionRule rule = new InterfacePermissionRule();
        rule.setServiceCode("order-service");
        rule.setHttpMethod("GET");
        rule.setPathPattern("/api/orders/**");

        InterfacePermissionSnapshot snapshot = new InterfacePermissionSnapshot();
        snapshot.setRules(List.of(rule));

        Optional<InterfacePermissionRule> matched = matcher.match(snapshot, "order-service", "POST", "/api/orders/query");

        assertFalse(matched.isPresent());
    }
}
