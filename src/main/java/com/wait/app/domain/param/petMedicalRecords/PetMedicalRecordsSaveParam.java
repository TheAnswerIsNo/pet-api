package com.wait.app.domain.param.petMedicalRecords;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author 天
 * Time: 2025/4/27 22:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetMedicalRecordsSaveParam {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "地址",required = true)
    private String address;

    @ApiModelProperty(value = "描述",required = true)
    private String description;
}
