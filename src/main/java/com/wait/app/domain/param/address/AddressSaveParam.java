package com.wait.app.domain.param.address;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author 天
 * Time: 2024/10/16 18:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressSaveParam {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "名字")
    private String name;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "详细地址")
    private String info;

    @ApiModelProperty(value = "门牌号")
    private String houseNumber;
}
