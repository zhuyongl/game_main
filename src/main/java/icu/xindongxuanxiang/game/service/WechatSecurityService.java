package icu.xindongxuanxiang.game.service;

import icu.xindongxuanxiang.game.model.dto.WechatMsgSecCheckResponse;

public interface WechatSecurityService {

    String getAccessToken();

    WechatMsgSecCheckResponse checkMessageContent(String openid, String content, Integer scene, String nickname);

    boolean isContentSafe(String openid, String content, Integer scene, String nickname);
}
