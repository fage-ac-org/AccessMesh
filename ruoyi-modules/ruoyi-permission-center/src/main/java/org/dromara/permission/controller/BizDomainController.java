package org.dromara.permission.controller;

import lombok.RequiredArgsConstructor;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.core.domain.R;
import org.dromara.permission.domain.dto.DomainPageReq;
import org.dromara.permission.domain.dto.DomainSaveReq;
import org.dromara.permission.domain.dto.IdsReq;
import org.dromara.permission.domain.vo.BizDomainVo;
import org.dromara.permission.service.BizDomainService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 业务域 biz_domain 接口
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/perm/domains")
public class BizDomainController {

    private final BizDomainService bizDomainService;

    @PostMapping("/page")
    public TableDataInfo<BizDomainVo> page(@RequestBody DomainPageReq req) {
        return bizDomainService.page(req);
    }

    @PostMapping("/save")
    public R<Void> save(@Validated @RequestBody DomainSaveReq req) {
        bizDomainService.save(req);
        return R.ok();
    }

    @PostMapping("/remove")
    public R<Void> remove(@Validated @RequestBody IdsReq req) {
        bizDomainService.remove(req);
        return R.ok();
    }
}
