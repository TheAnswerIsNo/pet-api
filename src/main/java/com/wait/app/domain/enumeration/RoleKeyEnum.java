package com.wait.app.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author 天
 * Time: 2024/9/15 11:00
 */
@Getter
@AllArgsConstructor
public enum RoleKeyEnum {

    USER("普通用户",0),

    RIDER("骑手",1),

    SHOPER("商家",2),

    ADMIN("管理员",3);

    private final String name;

    private final Integer roleKey;
}
