package icu.xindongxuanxiang.game.model.dto;

import lombok.Data;

@Data
public class WechatAuthResponse {

    private String openid;

    private String session_key;

    private String unionid;

    private Integer errcode;

    private String errmsg;
}