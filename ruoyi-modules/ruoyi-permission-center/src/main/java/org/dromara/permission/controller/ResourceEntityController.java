package org.dromara.permission.controller;

import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.permission.domain.dto.IdsReq;
import org.dromara.permission.domain.dto.ResourceListReq;
import org.dromara.permission.domain.dto.ResourceSaveReq;
import org.dromara.permission.domain.vo.ResourceEntityVo;
import org.dromara.permission.service.ResourceEntityService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资源实体 resource_entity 接口
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/perm/resources")
public class ResourceEntityController {

    private final ResourceEntityService resourceEntityService;

    @PostMapping("/list")
    public R<List<ResourceEntityVo>> list(@RequestBody ResourceListReq req) {
        return R.ok(resourceEntityService.list(req));
    }

    @PostMapping("/save")
    public R<Void> save(@Validated @RequestBody ResourceSaveReq req) {
        resourceEntityService.save(req);
        return R.ok();
    }

    @PostMapping("/remove")
    public R<Void> remove(@Validated @RequestBody IdsReq req) {
        resourceEntityService.remove(req);
        return R.ok();
    }
}
