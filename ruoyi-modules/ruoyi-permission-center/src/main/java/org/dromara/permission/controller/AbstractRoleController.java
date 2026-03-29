package org.dromara.permission.controller;

import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.permission.domain.dto.IdsReq;
import org.dromara.permission.domain.dto.RoleListReq;
import org.dromara.permission.domain.dto.RoleSaveReq;
import org.dromara.permission.domain.vo.AbstractRoleVo;
import org.dromara.permission.service.AbstractRoleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 抽象角色 abstract_role 接口
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/perm/roles")
public class AbstractRoleController {

    private final AbstractRoleService abstractRoleService;

    @PostMapping("/list")
    public R<List<AbstractRoleVo>> list(@RequestBody RoleListReq req) {
        return R.ok(abstractRoleService.list(req));
    }

    @PostMapping("/save")
    public R<Void> save(@Validated @RequestBody RoleSaveReq req) {
        abstractRoleService.save(req);
        return R.ok();
    }

    @PostMapping("/remove")
    public R<Void> remove(@Validated @RequestBody IdsReq req) {
        abstractRoleService.remove(req);
        return R.ok();
    }
}
