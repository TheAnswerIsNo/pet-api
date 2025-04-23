package com.wait.app.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 天
 */

@Getter
@AllArgsConstructor
public enum AdoptEnum {

    /**
     * 0:待管理员审核 1:待送养人同意 2:已完成 3:已取消 4:被拒绝 5:审核不通过
     */

    DSH("待管理员审核",0),
    DTY("待送养人同意",1),
    YWC("已完成",2),
    YQX("已取消",3),
    BJJ("被拒绝",4),
    SHBTG("审核不通过",5);

    private final String name;
    private final Integer code;
}
