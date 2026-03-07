package org.dromara.permission.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.dromara.common.mybatis.helper.DataBaseHelper;
import org.dromara.common.core.utils.StreamUtils;
import org.dromara.permission.domain.entity.PcDept;

import java.util.List;

/**
 * 权限中心-部门 Mapper
 */
public interface PcDeptMapper extends BaseMapper<PcDept> {

    default List<PcDept> selectListByParentId(Long parentId) {
        return selectList(new LambdaQueryWrapper<PcDept>()
            .select(PcDept::getDeptId)
            .apply(DataBaseHelper.findInSet(parentId, "ancestors")));
    }

    default List<Long> selectDeptAndChildById(Long parentId) {
        List<PcDept> list = selectListByParentId(parentId);
        List<Long> deptIds = StreamUtils.toList(list, PcDept::getDeptId);
        deptIds.add(parentId);
        return deptIds;
    }
}
