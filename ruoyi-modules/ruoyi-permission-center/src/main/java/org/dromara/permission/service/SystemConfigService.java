package org.dromara.permission.service;

import org.dromara.permission.domain.dto.SystemConfigListReq;
import org.dromara.permission.domain.dto.SystemConfigSaveReq;
import org.dromara.permission.domain.vo.SystemConfigVo;

import java.util.List;

/**
 * 类型枚举 system_config 服务
 */
public interface SystemConfigService {

    List<SystemConfigVo> list(SystemConfigListReq req);

    void save(SystemConfigSaveReq req);
}
