package com.tencent.wxcloudrun.controller;

import com.github.pagehelper.PageInfo;
import com.tencent.wxcloudrun.common.ApiResponse;
import com.tencent.wxcloudrun.exception.MessageNotFoundException;
import com.tencent.wxcloudrun.model.dto.MessageRequest;
import com.tencent.wxcloudrun.model.dto.PageRequest;
import com.tencent.wxcloudrun.model.vo.MessageVO;
import com.tencent.wxcloudrun.service.MessageService;
import com.tencent.wxcloudrun.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 留言控制器
 */
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    final MessageService messageService;
    final UserService userService;
    final Logger logger;

    public MessageController(@Autowired MessageService messageService, @Autowired UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
        this.logger = LoggerFactory.getLogger(MessageController.class);
    }

    /**
     * 分页获取所有留言
     * @param pageRequest 分页请求参数
     * @return API response json
     */
    @GetMapping
    ApiResponse getAllMessages(PageRequest pageRequest) {
        logger.info("/api/messages get request, page: {}, size: {}", pageRequest.getPage(), pageRequest.getSize());

        PageInfo<MessageVO> pageInfo = messageService.getMessagesVOWithPagination(pageRequest.getPage(), pageRequest.getSize());
        return ApiResponse.ok(pageInfo);
    }

    /**
     * 根据ID获取留言
     * @param id 留言ID
     * @return API response json
     */
    @GetMapping("/{id}")
    ApiResponse getMessageById(@PathVariable Integer id) {
        logger.info("/api/messages/{} get request", id);
        MessageVO messageVO = messageService.getMessageVOById(id)
                .orElseThrow(() -> new MessageNotFoundException("留言ID: " + id + " 不存在"));
        return ApiResponse.ok(messageVO);
    }

    /**
     * 根据用户ID分页获取留言
     * @param userId 用户ID
     * @param pageRequest 分页请求参数
     * @return API response json
     */
    @GetMapping("/user/{userId}")
    ApiResponse getMessagesByUserId(@PathVariable Integer userId, PageRequest pageRequest) {
        logger.info("/api/messages/user/{} get request, page: {}, size: {}", userId, pageRequest.getPage(), pageRequest.getSize());

        PageInfo<MessageVO> pageInfo = messageService.getMessagesByUserIdVOWithPagination(userId, pageRequest.getPage(), pageRequest.getSize());
        return ApiResponse.ok(pageInfo);
    }

    /**
     * 创建留言
     * @param request {@link MessageRequest}
     * @return API response json
     */
    @PostMapping
    ApiResponse createMessage(@RequestBody MessageRequest request) {
        logger.info("/api/messages post request");

        MessageVO messageVO = messageService.createMessageAndReturnVO(request);
        return ApiResponse.ok(messageVO);
    }

    /**
     * 更新留言
     * @param id 留言ID
     * @param request {@link MessageRequest}
     * @return API response json
     */
    @PutMapping("/{id}")
    ApiResponse updateMessage(@PathVariable Integer id, @RequestBody MessageRequest request) {
        logger.info("/api/messages/{} put request", id);

        MessageVO messageVO = messageService.updateMessageAndReturnVO(id, request)
                .orElseThrow(() -> new MessageNotFoundException("留言ID: " + id + " 不存在"));
        return ApiResponse.ok(messageVO);
    }

    /**
     * 删除留言
     * @param id 留言ID
     * @return API response json
     */
    @DeleteMapping("/{id}")
    ApiResponse deleteMessage(@PathVariable Integer id) {
        logger.info("/api/messages/{} delete request", id);

        messageService.deleteMessage(id);
        return ApiResponse.ok();
    }
}