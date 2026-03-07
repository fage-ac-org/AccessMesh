package org.dromara.permission.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.dromara.permission.constant.PermissionConstants;
import org.dromara.permission.domain.PcSystemConfig;
import org.dromara.permission.domain.dto.SystemConfigListReq;
import org.dromara.permission.domain.dto.SystemConfigSaveReq;
import org.dromara.permission.domain.vo.SystemConfigVo;
import org.dromara.permission.mapper.PcSystemConfigMapper;
import org.dromara.permission.service.SystemConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SystemConfigServiceImpl implements SystemConfigService {

    private final PcSystemConfigMapper mapper;

    @Override
    public List<SystemConfigVo> list(SystemConfigListReq req) {
        if (req == null || req.getTenantId() == null) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<PcSystemConfig> q = new LambdaQueryWrapper<PcSystemConfig>()
            .eq(PcSystemConfig::getTenantId, req.getTenantId())
            .eq(PcSystemConfig::getDeleteFlag, PermissionConstants.NOT_DELETED);
        if (req.getBizDomainId() != null) {
            q.eq(PcSystemConfig::getBizDomainId, req.getBizDomainId());
        }
        if (StrUtil.isNotBlank(req.getConfigKey())) {
            q.eq(PcSystemConfig::getConfigKey, req.getConfigKey());
        }
        q.orderByAsc(PcSystemConfig::getSortOrder);
        List<PcSystemConfig> list = mapper.selectList(q);
        return list.stream().map(this::toVo).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SystemConfigSaveReq req) {
        if (req == null || CollUtil.isEmpty(req.getItems())) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        for (SystemConfigSaveReq.SystemConfigItem item : req.getItems()) {
            if (item.getTenantId() == null) {
                continue;
            }
            PcSystemConfig entity;
            if (item.getId() != null) {
                entity = mapper.selectOne(new LambdaQueryWrapper<PcSystemConfig>()
                    .eq(PcSystemConfig::getId, item.getId())
                    .eq(PcSystemConfig::getDeleteFlag, PermissionConstants.NOT_DELETED));
                if (entity == null) {
                    continue;
                }
                entity.setBizDomainId(item.getBizDomainId());
                entity.setConfigKey(item.getConfigKey());
                entity.setTypeValue(item.getTypeValue());
                entity.setName(item.getName());
                entity.setDescription(item.getDescription());
                entity.setSortOrder(item.getSortOrder() != null ? item.getSortOrder() : 0);
                entity.setUpdatedAt(now);
                mapper.updateById(entity);
            } else {
                entity = new PcSystemConfig();
                entity.setTenantId(item.getTenantId());
                entity.setBizDomainId(item.getBizDomainId());
                entity.setConfigKey(item.getConfigKey());
                entity.setTypeValue(item.getTypeValue());
                entity.setName(item.getName());
                entity.setDescription(item.getDescription());
                entity.setSortOrder(item.getSortOrder() != null ? item.getSortOrder() : 0);
                entity.setDeleteFlag(PermissionConstants.NOT_DELETED);
                entity.setCreatedAt(now);
                entity.setUpdatedAt(now);
                mapper.insert(entity);
            }
        }
    }

    private SystemConfigVo toVo(PcSystemConfig e) {
        SystemConfigVo vo = new SystemConfigVo();
        BeanUtils.copyProperties(e, vo);
        return vo;
    }
}
