package icu.xindongxuanxiang.game.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import icu.xindongxuanxiang.game.model.entity.User;

import java.util.List;

@Mapper
public interface UserMapper {

    User getUser(@Param("id") Integer id);

    User getUserByUsername(@Param("username") String username);

    User getUserByEmail(@Param("email") String email);

    User getUserByWechatOpenId(@Param("wechatOpenId") String wechatOpenId);

    List<User> getAllUsers();

    void insertUser(User user);

    void updateUser(User user);

    void deleteUser(@Param("id") Integer id);
}