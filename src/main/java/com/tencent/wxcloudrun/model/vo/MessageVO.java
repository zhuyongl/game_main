package com.tencent.wxcloudrun.model.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MessageVO {

  private Integer id;

  private Integer userId;

  private String content;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
  
  private UserVO user;
}