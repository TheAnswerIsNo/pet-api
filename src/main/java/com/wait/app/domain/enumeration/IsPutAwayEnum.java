package com.wait.app.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 天
 */

@Getter
@AllArgsConstructor
public enum IsPutAwayEnum {


    PUT_AWAY("上架",1),
    DIS_PUT_AWAY("下架",0);

    private final String name;

    private final Integer value;
}
