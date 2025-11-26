package com.tencent.wxcloudrun.model.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class User implements Serializable {

  private Integer id;

  private String username;

  private String email;

  private String password;

  private String nickname;

  private String avatar;

  private String userType;

  private String wechatOpenId;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}