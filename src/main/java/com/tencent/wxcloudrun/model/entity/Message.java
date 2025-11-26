package com.tencent.wxcloudrun.model.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Message implements Serializable {

  private Integer id;

  private Integer userId;

  private String content;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}