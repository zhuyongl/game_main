package com.tencent.wxcloudrun.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tencent.wxcloudrun.exception.UserNotFoundException;
import com.tencent.wxcloudrun.mapper.UserMapper;
import com.tencent.wxcloudrun.model.dto.UserRequest;
import com.tencent.wxcloudrun.model.dto.WechatAuthResponse;
import com.tencent.wxcloudrun.model.dto.WechatLoginRequest;
import com.tencent.wxcloudrun.model.entity.User;
import com.tencent.wxcloudrun.model.vo.UserVO;
import com.tencent.wxcloudrun.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

  final UserMapper userMapper;
  
  @Value("${wechat.appid}")
  private String wechatAppId;
  
  @Value("${wechat.secret}")
  private String wechatSecret;

  public UserServiceImpl(@Autowired UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  @Override
  public Optional<User> getUser(Integer id) {
    return Optional.ofNullable(userMapper.getUser(id));
  }

  @Override
  public Optional<User> getUserByUsername(String username) {
    return Optional.ofNullable(userMapper.getUserByUsername(username));
  }

  @Override
  public Optional<User> getUserByEmail(String email) {
    return Optional.ofNullable(userMapper.getUserByEmail(email));
  }

  @Override
  public Optional<User> getUserByWechatOpenId(String wechatOpenId) {
    return Optional.ofNullable(userMapper.getUserByWechatOpenId(wechatOpenId));
  }

  @Override
  public List<User> getAllUsers() {
    return userMapper.getAllUsers();
  }

  @Override
  public PageInfo<User> getUsersWithPagination(Integer page, Integer size) {
    PageHelper.startPage(page, size);
    List<User> users = userMapper.getAllUsers();
    return new PageInfo<>(users);
  }
  
  @Override
  public PageInfo<UserVO> getUsersVOWithPagination(Integer page, Integer size) {
    PageHelper.startPage(page, size);
    List<User> users = userMapper.getAllUsers();
    List<UserVO> userVOS = users.stream()
        .map(this::convertToVO)
        .collect(Collectors.toList());
    PageInfo<User> userPageInfo = new PageInfo<>(users);
    PageInfo<UserVO> voPageInfo = new PageInfo<>((List)users);
    voPageInfo.setList(userVOS);
    voPageInfo.setTotal(userPageInfo.getTotal());
    voPageInfo.setPageNum(userPageInfo.getPageNum());
    voPageInfo.setPageSize(userPageInfo.getPageSize());
    voPageInfo.setPages(userPageInfo.getPages());
    voPageInfo.setHasNextPage(userPageInfo.isHasNextPage());
    voPageInfo.setHasPreviousPage(userPageInfo.isHasPreviousPage());
    return voPageInfo;
  }
  
  @Override
  public Optional<UserVO> getUserVOById(Integer id) {
    return getUser(id).map(this::convertToVO);
  }
  
  @Override
  public Optional<UserVO> getUserVOByUsername(String username) {
    return getUserByUsername(username).map(this::convertToVO);
  }
  
  @Override
  public Optional<UserVO> getUserVOByEmail(String email) {
    return getUserByEmail(email).map(this::convertToVO);
  }
  
  @Override
  public Optional<UserVO> getUserVOByWechatOpenId(String wechatOpenId) {
    return getUserByWechatOpenId(wechatOpenId).map(this::convertToVO);
  }
  
  @Override
  public UserVO createUserAndReturnVO(UserRequest request) {
    User user = new User();
    user.setUsername(request.getUsername());
    user.setEmail(request.getEmail());
    user.setPassword(request.getPassword());
    user.setNickname(request.getNickname());
    user.setAvatar(request.getAvatar());
    user.setUserType(request.getUserType());
    user.setWechatOpenId(request.getWechatOpenId());
    
    User createdUser = createUser(user);
    return convertToVO(createdUser);
  }
  
  @Override
  public Optional<UserVO> updateUserAndReturnVO(Integer id, UserRequest request) {
    Optional<User> userOptional = getUser(id);
    if (!userOptional.isPresent()) {
      return Optional.empty();
    }
    
    User user = userOptional.get();
    user.setUsername(request.getUsername());
    user.setEmail(request.getEmail());
    user.setPassword(request.getPassword());
    user.setNickname(request.getNickname());
    user.setAvatar(request.getAvatar());
    user.setUserType(request.getUserType());
    user.setWechatOpenId(request.getWechatOpenId());
    
    User updatedUser = updateUser(user);
    return Optional.of(convertToVO(updatedUser));
  }
  
  @Override
  public UserVO wechatLogin(WechatLoginRequest request) {
    // 调用微信接口获取openid
    String wechatAuthUrl = "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={code}&grant_type=authorization_code";
    
    RestTemplate restTemplate = new RestTemplate();
    WechatAuthResponse authResponse = restTemplate.getForObject(
        wechatAuthUrl,
        WechatAuthResponse.class,
        wechatAppId,
        wechatSecret,
        request.getCode()
    );
    
    if (authResponse == null || authResponse.getErrcode() != null) {
      throw new RuntimeException("微信登录失败: " + (authResponse != null ? authResponse.getErrmsg() : "未知错误"));
    }
    
    String openid = authResponse.getOpenid();
    
    // 检查用户是否已存在
    User existingUser = userMapper.getUserByWechatOpenId(openid);
    
    if (existingUser != null) {
      // 用户已存在，更新用户信息
      existingUser.setNickname(request.getNickname());
      existingUser.setAvatar(request.getAvatar());
      userMapper.updateUser(existingUser);
      return convertToVO(existingUser);
    } else {
      // 用户不存在，创建新用户
      User newUser = new User();
      newUser.setWechatOpenId(openid);
      newUser.setNickname(request.getNickname());
      newUser.setAvatar(request.getAvatar());
      newUser.setUserType("wechat");
      userMapper.insertUser(newUser);
      return convertToVO(newUser);
    }
  }
  
  @Override
  public Optional<UserVO> updateWechatUserInfo(String wechatOpenId, UserRequest request) {
    User user = userMapper.getUserByWechatOpenId(wechatOpenId);
    if (user == null) {
      return Optional.empty();
    }
    
    // 只更新昵称和头像
    user.setNickname(request.getNickname());
    user.setAvatar(request.getAvatar());
    
    User updatedUser = updateUser(user);
    return Optional.of(convertToVO(updatedUser));
  }

  @Override
  public User createUser(User user) {
    userMapper.insertUser(user);
    return user;
  }

  @Override
  public User updateUser(User user) {
    userMapper.updateUser(user);
    return user;
  }

  @Override
  public void deleteUser(Integer id) {
    Optional<User> user = getUser(id);
    if (!user.isPresent()) {
      throw new UserNotFoundException("用户ID: " + id + " 不存在");
    }
    userMapper.deleteUser(id);
  }
  
  @Override
  public UserVO convertToVO(User user) {
    UserVO userVO = new UserVO();
    BeanUtils.copyProperties(user, userVO);
    return userVO;
  }
}