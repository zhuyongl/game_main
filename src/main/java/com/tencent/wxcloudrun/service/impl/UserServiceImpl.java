package com.tencent.wxcloudrun.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tencent.wxcloudrun.exception.UserNotFoundException;
import com.tencent.wxcloudrun.mapper.UserMapper;
import com.tencent.wxcloudrun.model.dto.UserRequest;
import com.tencent.wxcloudrun.model.entity.User;
import com.tencent.wxcloudrun.model.vo.UserVO;
import com.tencent.wxcloudrun.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

  final UserMapper userMapper;

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