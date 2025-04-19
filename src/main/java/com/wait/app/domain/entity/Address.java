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
 * Time: 2024/10/16 13:38
 */
@Data
@AutoTable(comment = "地址表")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @ColumnId(mode = IdType.ASSIGN_UUID,comment = "id",length = 45)
    private String id;

    @AutoColumn(comment = "用户id",notNull = true,length = 45)
    private String userId;

    @AutoColumn(comment = "名字",notNull = true)
    private String name;

    @AutoColumn(comment = "手机号",notNull = true,length = 11)
    private String phone;

    @AutoColumn(comment = "详细地址",notNull = true)
    private String info;

    @AutoColumn(comment = "门牌号",notNull = true)
    private String houseNumber;

    @InsertFillTime
    @AutoColumn(comment = "创建时间",notNull = true)
    private LocalDateTime createTime;

    @InsertUpdateFillTime
    @AutoColumn(comment = "更新时间",notNull = true)
    private LocalDateTime updateTime;
}
