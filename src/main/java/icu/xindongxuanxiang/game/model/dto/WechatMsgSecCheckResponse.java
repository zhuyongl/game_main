package icu.xindongxuanxiang.game.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class WechatMsgSecCheckResponse {

    private Integer errcode;

    private String errmsg;

    @JsonProperty("trace_id")
    private String traceId;

    private Result result;

    private List<Detail> detail;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        private String suggest;
        private Integer label;
        @JsonProperty("replaced_content")
        private String replacedContent;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Detail {
        private String strategy;
        private Integer errcode;
        private String suggest;
        private Integer label;
        private String keyword;
        private Integer prob;
    }
}
