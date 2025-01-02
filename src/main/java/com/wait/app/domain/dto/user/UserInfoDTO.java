package com.wait.app.domain.dto.user;

import com.wait.app.domain.dto.role.RoleDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *
 * @author 天
 * Time: 2024/9/6 23:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDTO {

    @ApiModelProperty(value = "用户id")
    private String id;

    @ApiModelProperty(value = "角色")
    private List<RoleDTO> roles;

}
