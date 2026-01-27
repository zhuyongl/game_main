package icu.xindongxuanxiang.game.service;

import java.util.Optional;

import com.github.pagehelper.PageInfo;
import icu.xindongxuanxiang.game.model.dto.MessageRequest;
import icu.xindongxuanxiang.game.model.entity.Message;
import icu.xindongxuanxiang.game.model.entity.User;
import icu.xindongxuanxiang.game.model.vo.MessageVO;

import java.util.List;

public interface MessageService {

  Optional<Message> getMessage(Integer id);

  List<Message> getAllMessages();

  PageInfo<Message> getMessagesWithPagination(Integer page, Integer size);
  
  PageInfo<MessageVO> getMessagesVOWithPagination(Integer page, Integer size);

  List<Message> getMessagesByUserId(Integer userId);

  PageInfo<Message> getMessagesByUserIdWithPagination(Integer userId, Integer page, Integer size);
  
  PageInfo<MessageVO> getMessagesByUserIdVOWithPagination(Integer userId, Integer page, Integer size);

  Message createMessage(Message message);

  Message updateMessage(Message message);

  void deleteMessage(Integer id);

  void deleteMessagesByUserId(Integer userId);
  
  // 新增的VO相关方法
  Optional<MessageVO> getMessageVOById(Integer id);
  
  MessageVO createMessageAndReturnVO(MessageRequest request);
  
  Optional<MessageVO> updateMessageAndReturnVO(Integer id, MessageRequest request);
  
  MessageVO convertToVO(Message message);
  
  MessageVO convertToVO(Message message, User user);
}