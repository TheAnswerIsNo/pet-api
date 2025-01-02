package com.wait.app.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 天
 */

@Getter
@AllArgsConstructor
public enum AttachmentEnum {

    SHOP("商铺","shop"),
    GOODS("商品","goods");

    private final String name;

    private final String value;
}
