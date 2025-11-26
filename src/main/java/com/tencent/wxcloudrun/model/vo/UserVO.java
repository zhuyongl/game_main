package com.tencent.wxcloudrun.model.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserVO {

  private Integer id;

  private String username;

  private String email;

  private String nickname;

  private String avatar;

  private String userType;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}