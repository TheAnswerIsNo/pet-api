package com.wait.app.domain.param.order;

import com.wait.app.utils.page.RequestDTOWithPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 天
 * Time: 2025/4/22 19:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderListParam extends RequestDTOWithPage {

    @ApiModelProperty(value = "订单状态(0:未付款 1:送货中 2:已送达 3:已完成 4:订单取消)")
    private Integer status;
}
