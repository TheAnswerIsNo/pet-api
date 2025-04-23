package com.wait.app.domain.param.adopt;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 天
 * Time: 2025/4/23 23:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplyAdoptParam {
    
    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "养宠经验")
    private String experience;

    @ApiModelProperty(value = "婚姻状况")
    private String marriage;

    @ApiModelProperty(value = "住房情况")
    private String housing;

    @ApiModelProperty(value = "职业")
    private String job;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "给送养人的话")
    private String words;

    @ApiModelProperty(value = "送养记录id")
    private String giveUpAdoptRecordId;

    @ApiModelProperty(value = "宠物id")
    private String petId;
}
