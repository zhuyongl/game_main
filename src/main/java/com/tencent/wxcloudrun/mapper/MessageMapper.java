package com.tencent.wxcloudrun.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tencent.wxcloudrun.model.entity.Message;

import java.util.List;

@Mapper
public interface MessageMapper {

  Message getMessage(@Param("id") Integer id);

  List<Message> getAllMessages();

  List<Message> getMessagesByUserId(@Param("userId") Integer userId);

  void insertMessage(Message message);

  void updateMessage(Message message);

  void deleteMessage(@Param("id") Integer id);

  void deleteMessagesByUserId(@Param("userId") Integer userId);
}