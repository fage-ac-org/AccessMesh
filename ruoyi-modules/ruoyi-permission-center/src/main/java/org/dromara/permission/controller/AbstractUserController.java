package org.dromara.permission.controller;

import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.permission.domain.dto.IdsReq;
import org.dromara.permission.domain.dto.UserPageReq;
import org.dromara.permission.domain.dto.UserSaveReq;
import org.dromara.permission.domain.vo.AbstractUserVo;
import org.dromara.permission.service.AbstractUserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 抽象用户 abstract_user 接口
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/perm/users")
public class AbstractUserController {

    private final AbstractUserService abstractUserService;

    @PostMapping("/page")
    public TableDataInfo<AbstractUserVo> page(@RequestBody UserPageReq req) {
        return abstractUserService.page(req);
    }

    @PostMapping("/save")
    public R<Void> save(@RequestBody UserSaveReq req) {
        abstractUserService.save(req);
        return R.ok();
    }

    @PostMapping("/remove")
    public R<Void> remove(@RequestBody IdsReq req) {
        abstractUserService.remove(req);
        return R.ok();
    }
}
