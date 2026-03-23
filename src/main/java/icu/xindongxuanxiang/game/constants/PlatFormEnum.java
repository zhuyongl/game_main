package icu.xindongxuanxiang.game.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlatFormEnum {
    /**
     * 微信
     */
    WECHAT("wechat", 0),
    /**
     * 抖音
     */
    DOUYIN("douyin", 1),
    /**
     * 快手
     */
    KUAISHOU("kuaishou", 2),
    /**
     * 其他
     */
    OTHER("other", 3);
    
    private final String key;
    private final Integer value;
}
