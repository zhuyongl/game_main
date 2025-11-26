package com.tencent.wxcloudrun.model.dto;

import lombok.Data;

@Data
public class UploadFileResponse {

    private Integer errcode;
    
    private String errmsg;
    
    private String url;
    
    private String token;
    
    private String authorization;
    
    private String fileId;
    
    private String cosFileId;
    
    private String key;
}