package com.wait.app.domain.dto.dict;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 天
 * Time: 2024/9/10 17:18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictListDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "父级id")
    private String parentId;

    @ApiModelProperty(value = "标签")
    private String label;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "子级字典")
    private List<DictListDTO> children = new ArrayList<>();
}
