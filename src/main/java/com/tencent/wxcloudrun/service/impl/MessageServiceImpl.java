package com.tencent.wxcloudrun.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tencent.wxcloudrun.exception.MessageNotFoundException;
import com.tencent.wxcloudrun.exception.UserNotFoundException;
import com.tencent.wxcloudrun.mapper.MessageMapper;
import com.tencent.wxcloudrun.mapper.UserMapper;
import com.tencent.wxcloudrun.model.dto.MessageRequest;
import com.tencent.wxcloudrun.model.entity.Message;
import com.tencent.wxcloudrun.model.entity.User;
import com.tencent.wxcloudrun.model.vo.MessageVO;
import com.tencent.wxcloudrun.model.vo.UserVO;
import com.tencent.wxcloudrun.service.MessageService;
import com.tencent.wxcloudrun.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

  final MessageMapper messageMapper;
  final UserMapper userMapper;

  public MessageServiceImpl(@Autowired MessageMapper messageMapper, @Autowired UserMapper userMapper) {
    this.messageMapper = messageMapper;
    this.userMapper = userMapper;
  }

  @Override
  public Optional<Message> getMessage(Integer id) {
    return Optional.ofNullable(messageMapper.getMessage(id));
  }

  @Override
  public List<Message> getAllMessages() {
    return messageMapper.getAllMessages();
  }

  @Override
  public PageInfo<Message> getMessagesWithPagination(Integer page, Integer size) {
    PageHelper.startPage(page, size);
    List<Message> messages = messageMapper.getAllMessages();
    return new PageInfo<>(messages);
  }
  
  @Override
  public PageInfo<MessageVO> getMessagesVOWithPagination(Integer page, Integer size) {
    PageHelper.startPage(page, size);
    List<Message> messages = messageMapper.getAllMessages();
    List<MessageVO> messageVOS = messages.stream()
        .map(message -> {
          Optional<User> user = Optional.ofNullable(userMapper.getUser(message.getUserId()));
          return convertToVO(message, user.orElse(null));
        })
        .collect(Collectors.toList());
    PageInfo<Message> messagePageInfo = new PageInfo<>(messages);
    PageInfo<MessageVO> voPageInfo = new PageInfo<>((List)messages);
    voPageInfo.setList(messageVOS);
    voPageInfo.setTotal(messagePageInfo.getTotal());
    voPageInfo.setPageNum(messagePageInfo.getPageNum());
    voPageInfo.setPageSize(messagePageInfo.getPageSize());
    voPageInfo.setPages(messagePageInfo.getPages());
    voPageInfo.setHasNextPage(messagePageInfo.isHasNextPage());
    voPageInfo.setHasPreviousPage(messagePageInfo.isHasPreviousPage());
    return voPageInfo;
  }

  @Override
  public List<Message> getMessagesByUserId(Integer userId) {
    return messageMapper.getMessagesByUserId(userId);
  }

  @Override
  public PageInfo<Message> getMessagesByUserIdWithPagination(Integer userId, Integer page, Integer size) {
    PageHelper.startPage(page, size);
    List<Message> messages = messageMapper.getMessagesByUserId(userId);
    return new PageInfo<>(messages);
  }
  
  @Override
  public PageInfo<MessageVO> getMessagesByUserIdVOWithPagination(Integer userId, Integer page, Integer size) {
    // 检查用户是否存在
    User user = userMapper.getUser(userId);
    if (user == null) {
      throw new UserNotFoundException("用户ID: " + userId + " 不存在");
    }
    
    PageHelper.startPage(page, size);
    List<Message> messages = messageMapper.getMessagesByUserId(userId);
    List<MessageVO> messageVOS = messages.stream()
        .map(message -> convertToVO(message, user))
        .collect(Collectors.toList());
    PageInfo<Message> messagePageInfo = new PageInfo<>(messages);
    PageInfo<MessageVO> voPageInfo = new PageInfo<>((List)messages);
    voPageInfo.setList(messageVOS);
    voPageInfo.setTotal(messagePageInfo.getTotal());
    voPageInfo.setPageNum(messagePageInfo.getPageNum());
    voPageInfo.setPageSize(messagePageInfo.getPageSize());
    voPageInfo.setPages(messagePageInfo.getPages());
    voPageInfo.setHasNextPage(messagePageInfo.isHasNextPage());
    voPageInfo.setHasPreviousPage(messagePageInfo.isHasPreviousPage());
    return voPageInfo;
  }
  
  @Override
  public Optional<MessageVO> getMessageVOById(Integer id) {
    return getMessage(id).map(message -> {
      Optional<User> user = Optional.ofNullable(userMapper.getUser(message.getUserId()));
      return convertToVO(message, user.orElse(null));
    });
  }
  
  @Override
  public MessageVO createMessageAndReturnVO(MessageRequest request) {
    // 检查用户是否存在
    User user = userMapper.getUser(request.getUserId());
    if (user == null) {
      throw new UserNotFoundException("用户ID: " + request.getUserId() + " 不存在");
    }
    
    Message message = new Message();
    message.setUserId(request.getUserId());
    message.setContent(request.getContent());
    
    Message createdMessage = createMessage(message);
    return convertToVO(createdMessage, user);
  }
  
  @Override
  public Optional<MessageVO> updateMessageAndReturnVO(Integer id, MessageRequest request) {
    // 检查留言是否存在
    Message existingMessage = messageMapper.getMessage(id);
    if (existingMessage == null) {
      return Optional.empty();
    }
    
    // 检查用户是否存在
    User user = userMapper.getUser(request.getUserId());
    if (user == null) {
      throw new UserNotFoundException("用户ID: " + request.getUserId() + " 不存在");
    }
    
    existingMessage.setUserId(request.getUserId());
    existingMessage.setContent(request.getContent());
    
    Message updatedMessage = updateMessage(existingMessage);
    return Optional.of(convertToVO(updatedMessage, user));
  }

  @Override
  public Message createMessage(Message message) {
    // 检查用户是否存在
    User user = userMapper.getUser(message.getUserId());
    if (user == null) {
      throw new UserNotFoundException("用户ID: " + message.getUserId() + " 不存在");
    }
    
    messageMapper.insertMessage(message);
    return message;
  }

  @Override
  public Message updateMessage(Message message) {
    // 检查留言是否存在
    Message existingMessage = messageMapper.getMessage(message.getId());
    if (existingMessage == null) {
      throw new MessageNotFoundException("留言ID: " + message.getId() + " 不存在");
    }
    
    // 检查用户是否存在
    User user = userMapper.getUser(message.getUserId());
    if (user == null) {
      throw new UserNotFoundException("用户ID: " + message.getUserId() + " 不存在");
    }
    
    messageMapper.updateMessage(message);
    return message;
  }

  @Override
  public void deleteMessage(Integer id) {
    Message message = messageMapper.getMessage(id);
    if (message == null) {
      throw new MessageNotFoundException("留言ID: " + id + " 不存在");
    }
    messageMapper.deleteMessage(id);
  }

  @Override
  public void deleteMessagesByUserId(Integer userId) {
    messageMapper.deleteMessagesByUserId(userId);
  }
  
  @Override
  public MessageVO convertToVO(Message message) {
    MessageVO messageVO = new MessageVO();
    BeanUtils.copyProperties(message, messageVO);
    return messageVO;
  }
  
  @Override
  public MessageVO convertToVO(Message message, User user) {
    MessageVO messageVO = new MessageVO();
    BeanUtils.copyProperties(message, messageVO);
    
    if (user != null) {
      UserVO userVO = new UserVO();
      BeanUtils.copyProperties(user, userVO);
      messageVO.setUser(userVO);
    }
    
    return messageVO;
  }
}