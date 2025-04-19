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

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author 天
 * Time: 2024/9/10 14:36
 */
@Data
@AutoTable(comment = "商铺表")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shop {

    @ColumnId(mode = IdType.ASSIGN_UUID,comment = "id",length = 45)
    private String id;

    @AutoColumn(comment = "名称",notNull = true)
    private String name;

    @AutoColumn(comment = "描述")
    private String description;

    @AutoColumn(comment = "类型id(对应字典表id)",notNull = true,length = 45)
    private String typeId;

    @AutoColumn(comment = "配送费",notNull = true)
    private BigDecimal deliveryCost;

    @AutoColumn(comment = "开业时间",notNull = true)
    private LocalDateTime openTime;

    @AutoColumn(comment = "休息时间",notNull = true)
    private LocalDateTime restTime;

    @AutoColumn(comment = "联系方式",notNull = true,length = 11)
    private String phone;

    @AutoColumn(comment = "店长id",notNull = true,length = 45)
    private String userId;

    @InsertFillTime
    @AutoColumn(comment = "创建时间",notNull = true)
    private LocalDateTime createTime;

    @InsertUpdateFillTime
    @AutoColumn(comment = "更新时间",notNull = true)
    private LocalDateTime updateTime;
}
