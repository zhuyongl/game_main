package icu.xindongxuanxiang.game.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import icu.xindongxuanxiang.game.model.entity.Message;

import java.util.List;

@Mapper
public interface MessageMapper {

    Message getMessage(@Param("id") Integer id);

    Message getMessageRaw(@Param("id") Integer id);

    List<Message> getAllMessages();

    List<Message> getAllMessagesAdmin();

    List<Message> getMessagesByUserId(@Param("userId") Integer userId);

    List<Message> getMessagesByReviewStatus(@Param("reviewStatus") String reviewStatus);

    void insertMessage(Message message);

    void updateMessage(Message message);

    void deleteMessage(@Param("id") Integer id);

    void deleteMessagesByUserId(@Param("userId") Integer userId);

    void updateMessageReviewStatus(@Param("id") Integer id, @Param("reviewStatus") String reviewStatus);
}
