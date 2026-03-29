package org.dromara.permission.domain.dto;

import lombok.Data;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.permission.domain.bo.ChangeLogQueryBo;

/**
 * 变更记录分页查询请求体（POST JSON）
 *
 * @author RuoYi-Cloud-Plus
 */
@Data
public class ChangeLogPageReq {

    /** 查询条件，可为空 */
    private ChangeLogQueryBo query;

    /** 分页参数，可为空 */
    private PageQuery pageQuery;
}
