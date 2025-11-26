package com.tencent.wxcloudrun.model.dto;

import lombok.Data;

@Data
public class UserRequest {

  private String username;

  private String email;

  private String password;

  private String nickname;

  private String avatar;

  private String userType;

  private String wechatOpenId;
}