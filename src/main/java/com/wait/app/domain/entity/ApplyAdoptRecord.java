package com.wait.app.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.tangzc.autotable.annotation.AutoColumn;
import com.tangzc.autotable.annotation.AutoTable;
import com.tangzc.mpe.annotation.InsertFillTime;
import com.tangzc.mpe.annotation.InsertUpdateFillTime;
import com.tangzc.mpe.autotable.annotation.ColumnId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 *
 * @author 天
 * Time: 2025/4/24 0:14
 */
@Data
@AutoTable(comment = "申请领养表")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyAdoptRecord {

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

    @AutoColumn(comment = "用户id",notNull = true,length = 45)
    private String userId;

    @AutoColumn(comment = "送养记录id",notNull = true,length = 45)
    private String giveUpAdoptRecordId;

    @AutoColumn(comment = "宠物id",notNull = true,length = 45)
    private String petId;

    @AutoColumn(comment = "领养状态(0:待管理员审核 1:待送养人同意 2:已完成 3:已取消 4:被拒绝 5:审核不通过 )",notNull = true,defaultValue = "0")
    private Integer status;

    @InsertFillTime
    @AutoColumn(comment = "创建时间",notNull = true)
    private LocalDateTime createTime;

    @InsertUpdateFillTime
    @AutoColumn(comment = "更新时间",notNull = true)
    private LocalDateTime updateTime;
}
