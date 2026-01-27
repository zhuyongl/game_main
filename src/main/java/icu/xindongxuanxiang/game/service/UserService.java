package icu.xindongxuanxiang.game.service;

import java.util.Optional;

import com.github.pagehelper.PageInfo;
import icu.xindongxuanxiang.game.model.dto.UserRequest;
import icu.xindongxuanxiang.game.model.dto.WechatLoginRequest;
import icu.xindongxuanxiang.game.model.entity.User;
import icu.xindongxuanxiang.game.model.vo.UserVO;

import java.util.List;

public interface UserService {

  Optional<User> getUser(Integer id);

  Optional<User> getUserByUsername(String username);

  Optional<User> getUserByEmail(String email);

  Optional<User> getUserByWechatOpenId(String wechatOpenId);

  List<User> getAllUsers();

  PageInfo<User> getUsersWithPagination(Integer page, Integer size);
  
  PageInfo<UserVO> getUsersVOWithPagination(Integer page, Integer size);
  
  // 新增的VO相关方法
  Optional<UserVO> getUserVOById(Integer id);
  
  Optional<UserVO> getUserVOByUsername(String username);
  
  Optional<UserVO> getUserVOByEmail(String email);
  
  Optional<UserVO> getUserVOByWechatOpenId(String wechatOpenId);
  
  UserVO createUserAndReturnVO(UserRequest request);
  
  Optional<UserVO> updateUserAndReturnVO(Integer id, UserRequest request);
  
  // 微信登录相关方法
  UserVO wechatLogin(WechatLoginRequest request);
  
  Optional<UserVO> updateWechatUserInfo(String wechatOpenId, UserRequest request);

  User createUser(User user);

  User updateUser(User user);

  void deleteUser(Integer id);
  
  UserVO convertToVO(User user);
}