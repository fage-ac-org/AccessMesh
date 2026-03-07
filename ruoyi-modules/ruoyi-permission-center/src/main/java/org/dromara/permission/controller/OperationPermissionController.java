package org.dromara.permission.controller;

import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.permission.domain.dto.IdsReq;
import org.dromara.permission.domain.dto.OperationListReq;
import org.dromara.permission.domain.dto.OperationSaveReq;
import org.dromara.permission.domain.vo.OperationPermissionVo;
import org.dromara.permission.service.OperationPermissionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 操作权限 operation_permission 接口
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/perm/operations")
public class OperationPermissionController {

    private final OperationPermissionService operationPermissionService;

    @PostMapping("/list")
    public R<List<OperationPermissionVo>> list(@RequestBody OperationListReq req) {
        return R.ok(operationPermissionService.list(req));
    }

    @PostMapping("/save")
    public R<Void> save(@Validated @RequestBody OperationSaveReq req) {
        operationPermissionService.save(req);
        return R.ok();
    }

    @PostMapping("/remove")
    public R<Void> remove(@Validated @RequestBody IdsReq req) {
        operationPermissionService.remove(req);
        return R.ok();
    }
}
