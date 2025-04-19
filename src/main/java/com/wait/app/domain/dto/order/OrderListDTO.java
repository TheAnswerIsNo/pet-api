package com.wait.app.domain.dto.order;

import com.tangzc.autotable.annotation.AutoColumn;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 天
 * Time: 2024/10/7 18:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderListDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "总价")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "总数量")
    private Integer totalNumber;

    @ApiModelProperty(value = "订单状态(0:未付款 1:制作中 2:配送中 3:已送达 4:已完成 5:订单取消)")
    private Integer status;

    @ApiModelProperty(value = "接单时间")
    private LocalDateTime takeOrderTime;

    @ApiModelProperty(value = "订单明细")
    private List<OrderListDetail> orderListDetailList = new ArrayList<>();

    @Data
    public static class OrderListDetail{
        @AutoColumn(comment = "商品名称",notNull = true)
        private String name;

        @AutoColumn(comment = "单价",notNull = true)
        private BigDecimal price;

        @AutoColumn(comment = "数量",notNull = true)
        private Integer number;
    }
}
