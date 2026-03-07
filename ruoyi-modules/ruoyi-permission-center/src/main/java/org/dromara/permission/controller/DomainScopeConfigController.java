package org.dromara.permission.controller;

import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.permission.domain.dto.DomainScopeListReq;
import org.dromara.permission.domain.dto.DomainScopeSaveReq;
import org.dromara.permission.domain.dto.IdsReq;
import org.dromara.permission.domain.vo.DomainScopeConfigVo;
import org.dromara.permission.service.DomainScopeConfigService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 域范围配置 domain_scope_config 接口
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/perm/domain-scope")
public class DomainScopeConfigController {

    private final DomainScopeConfigService domainScopeConfigService;

    @PostMapping("/list")
    public R<List<DomainScopeConfigVo>> list(@RequestBody DomainScopeListReq req) {
        return R.ok(domainScopeConfigService.list(req));
    }

    @PostMapping("/save")
    public R<Void> save(@RequestBody DomainScopeSaveReq req) {
        domainScopeConfigService.save(req);
        return R.ok();
    }

    @PostMapping("/remove")
    public R<Void> remove(@RequestBody IdsReq req) {
        domainScopeConfigService.remove(req);
        return R.ok();
    }
}
