package org.dromara.permission.controller;

import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.permission.domain.dto.IdsReq;
import org.dromara.permission.domain.dto.ResourceDependencyListReq;
import org.dromara.permission.domain.dto.ResourceDependencySaveReq;
import org.dromara.permission.domain.vo.ResourceDependencyVo;
import org.dromara.permission.service.ResourceDependencyService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资源依赖 resource_dependency 接口
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/perm/resource-dependencies")
public class ResourceDependencyController {

    private final ResourceDependencyService resourceDependencyService;

    @PostMapping("/list")
    public R<List<ResourceDependencyVo>> list(@RequestBody ResourceDependencyListReq req) {
        return R.ok(resourceDependencyService.list(req));
    }

    @PostMapping("/save")
    public R<Void> save(@RequestBody ResourceDependencySaveReq req) {
        resourceDependencyService.save(req);
        return R.ok();
    }

    @PostMapping("/remove")
    public R<Void> remove(@RequestBody IdsReq req) {
        resourceDependencyService.remove(req);
        return R.ok();
    }
}
