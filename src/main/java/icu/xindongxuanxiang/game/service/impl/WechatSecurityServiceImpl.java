package icu.xindongxuanxiang.game.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import icu.xindongxuanxiang.game.exception.BusinessException;
import icu.xindongxuanxiang.game.exception.ErrorCode;
import icu.xindongxuanxiang.game.model.dto.WechatMsgSecCheckRequest;
import icu.xindongxuanxiang.game.model.dto.WechatMsgSecCheckResponse;
import icu.xindongxuanxiang.game.service.WechatSecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Service
public class WechatSecurityServiceImpl implements WechatSecurityService {

    private static final Logger logger = LoggerFactory.getLogger(WechatSecurityServiceImpl.class);

    private static final String MSG_SEC_CHECK_URL = "https://api.weixin.qq.com/wxa/game/content_spam/msg_sec_check?access_token=";

    @Value("${wechat.cloud.access-token-url}")
    private String accessTokenUrl;

    private String cachedAccessToken;
    private long tokenExpireTime = 0;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public WechatSecurityServiceImpl() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String getAccessToken() {
        if (cachedAccessToken != null && System.currentTimeMillis() < tokenExpireTime) {
            return cachedAccessToken;
        }

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(accessTokenUrl, String.class);
            JsonNode jsonNode = objectMapper.readTree(response.getBody());

            if (jsonNode.has("access_token")) {
                cachedAccessToken = jsonNode.get("access_token").asText();
                int expiresIn = jsonNode.get("expires_in").asInt();
                tokenExpireTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(expiresIn - 300);
                return cachedAccessToken;
            } else {
                logger.error("获取access_token失败: {}", response.getBody());
                throw new BusinessException(ErrorCode.WECHAT_ACCESS_TOKEN_ERROR);
            }
        } catch (Exception e) {
            logger.error("获取access_token异常", e);
            throw new BusinessException(ErrorCode.WECHAT_ACCESS_TOKEN_ERROR, e.getMessage());
        }
    }

    @Override
    public WechatMsgSecCheckResponse checkMessageContent(String openid, String content, Integer scene, String nickname) {
        try {
            String accessToken = getAccessToken();
            String url = MSG_SEC_CHECK_URL + accessToken;

            WechatMsgSecCheckRequest request = new WechatMsgSecCheckRequest();
            request.setOpenid(openid);
            request.setVersion(2);
            request.setScene(scene);
            request.setContent(content);
            request.setNickname(nickname);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<WechatMsgSecCheckRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            WechatMsgSecCheckResponse secCheckResponse = objectMapper.readValue(response.getBody(), WechatMsgSecCheckResponse.class);

            if (secCheckResponse.getErrcode() != null && secCheckResponse.getErrcode() != 0) {
                logger.error("微信内容安全检测失败: errcode={}, errmsg={}", secCheckResponse.getErrcode(), secCheckResponse.getErrmsg());
                throw new BusinessException(ErrorCode.WECHAT_SEC_CHECK_FAILED, secCheckResponse.getErrmsg());
            }

            return secCheckResponse;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.error("微信内容安全检测异常", e);
            throw new BusinessException(ErrorCode.WECHAT_SEC_CHECK_FAILED, e.getMessage());
        }
    }

    @Override
    public boolean isContentSafe(String openid, String content, Integer scene, String nickname) {
        WechatMsgSecCheckResponse response = checkMessageContent(openid, content, scene, nickname);

        if (response.getResult() == null) {
            return true;
        }

        String suggest = response.getResult().getSuggest();
        return "pass".equals(suggest);
    }
}
