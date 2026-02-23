package icu.xindongxuanxiang.game.controller;

import icu.xindongxuanxiang.game.common.ApiResponse;
import icu.xindongxuanxiang.game.exception.MessageNotFoundException;
import icu.xindongxuanxiang.game.model.entity.Message;
import icu.xindongxuanxiang.game.model.vo.MessageVO;
import icu.xindongxuanxiang.game.common.ApiResponse;
import icu.xindongxuanxiang.game.model.vo.UserVO;
import icu.xindongxuanxiang.game.service.MessageService;
import icu.xindongxuanxiang.game.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/admin/messages")
public class MessageReviewController {

    final MessageService messageService;
    final UserService userService;

    public MessageReviewController(@Autowired MessageService messageService, @Autowired UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @GetMapping
    public String listAll(@RequestParam(value = "status", required = false) String status,
                          @RequestParam(value = "page", defaultValue = "1") int page,
                          @RequestParam(value = "size", defaultValue = "10") int size,
                          Model model) {
        PageInfo<Message> messagePage;
        if (status == null || status.isEmpty() || "ALL".equalsIgnoreCase(status)) {
            messagePage = messageService.getMessagesWithPagination(page, size);
        } else {
            // 对于有状态的查询，需要先获取所有消息再过滤
            List<Message> allMessages = messageService.getMessagesByReviewStatus(status);
            // 手动分页
            int start = (page - 1) * size;
            int end = Math.min(start + size, allMessages.size());
            List<Message> pageMessages = allMessages.subList(start, end);

            // 创建PageInfo对象
            messagePage = new PageInfo<>(pageMessages);
            messagePage.setTotal(allMessages.size());
            messagePage.setPageNum(page);
            messagePage.setPageSize(size);
            int totalPages = (int) Math.ceil((double) allMessages.size() / size);
            messagePage.setPages(totalPages);
        }

        List<MessageVO> messageVOS = messagePage.getList().stream().map(message -> {
            MessageVO vo = messageService.convertToVO(message);
            Optional<UserVO> userVO = userService.getUserVOById(message.getUserId());
            userVO.ifPresent(vo::setUser);
            return vo;
        }).collect(Collectors.toList());

        model.addAttribute("messages", messageVOS);
        model.addAttribute("status", status == null ? "ALL" : status);
        model.addAttribute("currentPage", messagePage.getPageNum());
        model.addAttribute("totalPages", messagePage.getPages());
        model.addAttribute("totalItems", messagePage.getTotal());
        model.addAttribute("pageTitle", "留言管理");
        model.addAttribute("activeMenu", "messages");
        model.addAttribute("contentTemplate", "message-list");
        return "admin-layout";
    }

    @GetMapping("/review")
    public String reviewList(Model model) {
        List<Message> messages = messageService.getMessagesByReviewStatus("PENDING");
        List<MessageVO> messageVOS = messages.stream().map(message -> {
            MessageVO vo = messageService.convertToVO(message);
            Optional<UserVO> userVO = userService.getUserVOById(message.getUserId());
            userVO.ifPresent(vo::setUser);
            return vo;
        }).collect(Collectors.toList());
        model.addAttribute("messages", messageVOS);
        model.addAttribute("pageTitle", "留言审核");
        model.addAttribute("activeMenu", "messages");
        model.addAttribute("contentTemplate", "message-review");
        return "admin-layout";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Integer id, Model model) {
        Message message = messageService.getMessageRaw(id)
                .orElseThrow(() -> new MessageNotFoundException("留言ID: " + id + " 不存在"));
        MessageVO vo = messageService.convertToVO(message);
        Optional<UserVO> userVO = userService.getUserVOById(message.getUserId());
        userVO.ifPresent(vo::setUser);
        model.addAttribute("message", vo);
        model.addAttribute("pageTitle", "编辑留言");
        model.addAttribute("activeMenu", "messages");
        model.addAttribute("contentTemplate", "message-edit");
        return "admin-layout";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Integer id,
                         @RequestParam("content") String content,
                         @RequestParam("reviewStatus") String reviewStatus) {
        messageService.updateMessageContentAndStatus(id, content, reviewStatus);
        return "redirect:/admin/messages";
    }

    @PostMapping("/{id}/review")
    @ResponseBody
    public ApiResponse review(@PathVariable Integer id,
                              @RequestParam("content") String content,
                              @RequestParam("reviewStatus") String reviewStatus) {
        try {
            messageService.updateMessageContentAndStatus(id, content, reviewStatus);
            return ApiResponse.ok("审核操作成功");
        } catch (Exception e) {
            return ApiResponse.error("审核操作失败：" + e.getMessage());
        }
    }

    @PostMapping("/review/{id}/approve")
    public String approve(@PathVariable Integer id) {
        messageService.updateMessageReviewStatus(id, "APPROVED");
        return "redirect:/admin/messages/review";
    }

    @PostMapping("/review/{id}/reject")
    public String reject(@PathVariable Integer id) {
        messageService.updateMessageReviewStatus(id, "REJECTED");
        return "redirect:/admin/messages/review";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        messageService.deleteMessage(id);
        return "redirect:/admin/messages";
    }
}
