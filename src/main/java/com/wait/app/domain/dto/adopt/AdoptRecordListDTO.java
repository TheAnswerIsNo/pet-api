package com.wait.app.domain.dto.adopt;

import com.baomidou.mybatisplus.annotation.IdType;
import com.tangzc.autotable.annotation.AutoColumn;
import com.tangzc.mpe.autotable.annotation.ColumnId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author 天
 * Time: 2025/4/24 23:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdoptRecordListDTO {

    @ColumnId(mode = IdType.ASSIGN_UUID,comment = "id",length = 45)
    private String id;

    @AutoColumn(comment = "性别",notNull = true)
    private String sex;

    @AutoColumn(comment = "年龄")
    private Integer age;

    @AutoColumn(comment = "养宠经验",notNull = true)
    private String experience;

    @AutoColumn(comment = "婚姻状况",notNull = true)
    private String marriage;

    @AutoColumn(comment = "住房情况",notNull = true)
    private String housing;

    @AutoColumn(comment = "职业",notNull = true)
    private String job;

    @AutoColumn(comment = "地址",notNull = true)
    private String address;

    @AutoColumn(comment = "手机号",notNull = true)
    private String phone;

    @AutoColumn(comment = "给送养人的话",notNull = true)
    private String words;

    @ApiModelProperty(value = "领养人")
    private String adoptUser;

    @ApiModelProperty(value = "领养状态(0:待管理员审核 1:待送养人同意 2:已完成 3:已取消 4:被拒绝 5:审核不通过 )")
    private Integer status;
}
