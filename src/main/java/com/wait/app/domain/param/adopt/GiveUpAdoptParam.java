package com.wait.app.domain.param.adopt;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 天
 * Time: 2025/4/23 23:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiveUpAdoptParam {
    
    @ApiModelProperty( value = "昵称" )
    private String nickname;

    @ApiModelProperty( value = "年龄" )
    private Integer age;

    @ApiModelProperty( value = "类别" )
    private String type;

    @ApiModelProperty( value = "性别" )
    private String sex;

    @ApiModelProperty( value = "疫苗" )
    private String vaccine;

    @ApiModelProperty( value = "绝育" )
    private String sterilization;

    @ApiModelProperty( value = "驱虫" )
    private String deworming;

    @ApiModelProperty( value = "来源" )
    private String source;

    @ApiModelProperty( value = "体型" )
    private String bodyType;

    @ApiModelProperty( value = "毛发" )
    private String hair;

    @ApiModelProperty( value = "特点" )
    private List<String> characteristics;

    @ApiModelProperty( value = "描述" )
    private String description;
    
    @ApiModelProperty( value = "照片" )
    private MultipartFile photo;
}
