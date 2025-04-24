package com.wait.app.domain.param.dynamic;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author 天
 * Time: 2025/4/24 20:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DynamicAddParam {

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "图片")
    private MultipartFile photo;
}
