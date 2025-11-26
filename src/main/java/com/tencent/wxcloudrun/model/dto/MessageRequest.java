package com.tencent.wxcloudrun.model.dto;

import lombok.Data;

@Data
public class MessageRequest {

  private Integer userId;

  private String content;
}