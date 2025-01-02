package com.wait.app.domain.dto.role;

import com.baomidou.mybatisplus.annotation.IdType;
import com.tangzc.autotable.annotation.AutoColumn;
import com.tangzc.mpe.autotable.annotation.ColumnId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author 天
 * Time: 2024/9/15 13:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {

    @ColumnId(mode = IdType.ASSIGN_UUID,comment = "id",length = 45)
    private String id;

    @AutoColumn(comment = "名称",notNull = true)
    private String name;

    @AutoColumn(comment = "描述")
    private String description;

    @AutoColumn(comment = "排序",notNull = true,defaultValue = "0",length = 10)
    private Integer sort;

    @AutoColumn(comment = "启用(0:禁用 1:启用)",notNull = true,defaultValue = "1",length = 10)
    private Integer enabled;

    @AutoColumn(comment = "等级(0:普通用户 1:骑手 2:商家 3:管理员)",notNull = true,defaultValue = "0",length = 10)
    private Integer level;
}
