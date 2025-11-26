package com.tencent.wxcloudrun.model.dto;

import lombok.Data;

@Data
public class FileUploadRequest {

    private String path;
    
    private String fileName;
}