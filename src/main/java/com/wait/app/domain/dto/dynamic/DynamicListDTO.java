package com.wait.app.domain.dto.dynamic;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 *
 * @author 天
 * Time: 2025/4/24 21:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DynamicListDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "点赞数")
    private Integer likeNum;

    @ApiModelProperty(value = "图片")
    private String photo;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

}
