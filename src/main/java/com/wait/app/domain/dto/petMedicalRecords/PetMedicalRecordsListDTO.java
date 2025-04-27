package com.wait.app.domain.dto.petMedicalRecords;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author 天
 * Time: 2025/4/27 22:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetMedicalRecordsListDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "创建时间")
    private String createTime;
}
