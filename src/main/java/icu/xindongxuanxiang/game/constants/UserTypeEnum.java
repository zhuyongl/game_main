package icu.xindongxuanxiang.game.constants;

import lombok.Getter;

@Getter
public enum UserTypeEnum {

    LOCAL("local"),
    WECHAT("wechat");

    private final String type;

    UserTypeEnum(String type) {
        this.type = type;
    }
}
