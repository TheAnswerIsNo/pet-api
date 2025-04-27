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
 * Time: 2025/4/24 0:09
 */
@Data
@AutoTable(comment = "宠物表")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pet {

    @ColumnId(mode = IdType.ASSIGN_UUID,comment = "id",length = 45)
    private String id;

    @AutoColumn(comment = "昵称",notNull = true)
    private String nickname;

    @AutoColumn(comment = "年龄",notNull = true)
    private Integer age;

    @AutoColumn(comment = "类别",notNull = true)
    private String type;

    @AutoColumn(comment = "性别",notNull = true)
    private String sex;

    @AutoColumn(comment = "疫苗",notNull = true)
    private String vaccine;

    @AutoColumn(comment = "绝育",notNull = true)
    private String sterilization;

    @AutoColumn(comment = "驱虫",notNull = true)
    private String deworming;

    @AutoColumn(comment = "来源",notNull = true)
    private String source;

    @AutoColumn(comment = "体型",notNull = true)
    private String bodyType;

    @AutoColumn(comment = "毛发",notNull = true)
    private String hair;

    @AutoColumn(comment = "特点",notNull = true)
    private String characteristics;

    @AutoColumn(comment = "用户id",notNull = true,length = 45)
    private String userId;

    @InsertFillTime
    @AutoColumn(comment = "创建时间",notNull = true)
    private LocalDateTime createTime;

    @InsertUpdateFillTime
    @AutoColumn(comment = "更新时间",notNull = true)
    private LocalDateTime updateTime;

}
