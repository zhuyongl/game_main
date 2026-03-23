package icu.xindongxuanxiang.game.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WechatMsgSecCheckRequest {

    private String openid;

    private Integer version = 2;

    private Integer scene;

    private String content;

    private String nickname;
}
