package org.dromara.permission.controller;

import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.permission.domain.dto.ConditionListReq;
import org.dromara.permission.domain.dto.ConditionSaveReq;
import org.dromara.permission.domain.dto.IdsReq;
import org.dromara.permission.domain.vo.PermissionConditionVo;
import org.dromara.permission.service.PermissionConditionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 生效条件 permission_condition 接口
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/perm/conditions")
public class PermissionConditionController {

    private final PermissionConditionService permissionConditionService;

    @PostMapping("/list")
    public R<List<PermissionConditionVo>> list(@RequestBody ConditionListReq req) {
        return R.ok(permissionConditionService.list(req));
    }

    @PostMapping("/save")
    public R<Void> save(@RequestBody ConditionSaveReq req) {
        permissionConditionService.save(req);
        return R.ok();
    }

    @PostMapping("/remove")
    public R<Void> remove(@RequestBody IdsReq req) {
        permissionConditionService.remove(req);
        return R.ok();
    }
}
