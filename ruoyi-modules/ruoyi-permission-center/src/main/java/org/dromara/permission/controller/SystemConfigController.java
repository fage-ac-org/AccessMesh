package org.dromara.permission.controller;

import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.permission.domain.dto.SystemConfigListReq;
import org.dromara.permission.domain.dto.SystemConfigSaveReq;
import org.dromara.permission.domain.vo.SystemConfigVo;
import org.dromara.permission.service.SystemConfigService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 类型枚举 system_config 接口
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/perm/system-config")
public class SystemConfigController {

    private final SystemConfigService systemConfigService;

    @PostMapping("/list")
    public R<List<SystemConfigVo>> list(@RequestBody SystemConfigListReq req) {
        return R.ok(systemConfigService.list(req));
    }

    @PostMapping("/save")
    public R<Void> save(@Validated @RequestBody SystemConfigSaveReq req) {
        systemConfigService.save(req);
        return R.ok();
    }
}
