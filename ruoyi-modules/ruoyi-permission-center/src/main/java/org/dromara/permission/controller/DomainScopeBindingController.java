package org.dromara.permission.controller;

import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.permission.domain.dto.DomainBindingListReq;
import org.dromara.permission.domain.dto.DomainBindingSaveReq;
import org.dromara.permission.domain.dto.IdsReq;
import org.dromara.permission.domain.vo.DomainScopeBindingVo;
import org.dromara.permission.service.DomainScopeBindingService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 域引用绑定 domain_scope_binding 接口
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/perm/domain-binding")
public class DomainScopeBindingController {

    private final DomainScopeBindingService domainScopeBindingService;

    @PostMapping("/list")
    public R<List<DomainScopeBindingVo>> list(@RequestBody DomainBindingListReq req) {
        return R.ok(domainScopeBindingService.list(req));
    }

    @PostMapping("/save")
    public R<Void> save(@RequestBody DomainBindingSaveReq req) {
        domainScopeBindingService.save(req);
        return R.ok();
    }

    @PostMapping("/remove")
    public R<Void> remove(@RequestBody IdsReq req) {
        domainScopeBindingService.remove(req);
        return R.ok();
    }
}
