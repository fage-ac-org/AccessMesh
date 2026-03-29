package org.dromara.permission.controller;

import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.permission.domain.dto.DomainRelationListReq;
import org.dromara.permission.domain.dto.DomainRelationSaveReq;
import org.dromara.permission.domain.dto.IdsReq;
import org.dromara.permission.domain.vo.DomainRelationConfigVo;
import org.dromara.permission.service.DomainRelationConfigService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 域关系配置 domain_relation_config 接口
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/perm/domain-relation")
public class DomainRelationConfigController {

    private final DomainRelationConfigService domainRelationConfigService;

    @PostMapping("/list")
    public R<List<DomainRelationConfigVo>> list(@RequestBody DomainRelationListReq req) {
        return R.ok(domainRelationConfigService.list(req));
    }

    @PostMapping("/save")
    public R<Void> save(@RequestBody DomainRelationSaveReq req) {
        domainRelationConfigService.save(req);
        return R.ok();
    }

    @PostMapping("/remove")
    public R<Void> remove(@RequestBody IdsReq req) {
        domainRelationConfigService.remove(req);
        return R.ok();
    }
}
