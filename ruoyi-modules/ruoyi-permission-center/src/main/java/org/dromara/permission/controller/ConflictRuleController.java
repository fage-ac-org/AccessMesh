package org.dromara.permission.controller;

import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.permission.domain.dto.ConflictDetectReq;
import org.dromara.permission.domain.dto.ConflictRuleListReq;
import org.dromara.permission.domain.dto.ConflictRuleSaveReq;
import org.dromara.permission.domain.dto.IdsReq;
import org.dromara.permission.domain.vo.ConflictRuleVo;
import org.dromara.permission.domain.vo.ConflictViolationVo;
import org.dromara.permission.service.ConflictRuleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限冲突规则与检测接口
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/perm/conflict-rules")
public class ConflictRuleController {

    private final ConflictRuleService conflictRuleService;

    @PostMapping("/list")
    public R<List<ConflictRuleVo>> list(@RequestBody ConflictRuleListReq req) {
        return R.ok(conflictRuleService.list(req));
    }

    @PostMapping("/save")
    public R<Void> save(@Validated @RequestBody ConflictRuleSaveReq req) {
        conflictRuleService.save(req);
        return R.ok();
    }

    @PostMapping("/remove")
    public R<Void> remove(@Validated @RequestBody IdsReq req) {
        conflictRuleService.remove(req);
        return R.ok();
    }

    @PostMapping("/detect")
    public R<List<ConflictViolationVo>> detect(@Validated @RequestBody ConflictDetectReq req) {
        return R.ok(conflictRuleService.detect(req));
    }
}
