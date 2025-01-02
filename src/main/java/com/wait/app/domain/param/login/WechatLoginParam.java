package com.wait.app.domain.param.login;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author 天
 * Time: 2024/9/10 13:27
 */
@Data
public class WechatLoginParam {

    @ApiModelProperty(value = "授权code")
    private String code;

    @ApiModelProperty(value = "手机验证code")
    private String phoneCode;
}
