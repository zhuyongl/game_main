package icu.xindongxuanxiang.game.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import icu.xindongxuanxiang.game.model.dto.PageRequest;
import icu.xindongxuanxiang.game.model.vo.UserVO;
import icu.xindongxuanxiang.game.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    final UserService userService;

    public AdminUserController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listUsers(@RequestParam(value = "page", defaultValue = "1") int page,
                           @RequestParam(value = "size", defaultValue = "10") int size,
                           Model model) {
        PageInfo<UserVO> pageInfo = userService.getUsersVOWithPagination(page, size);
        List<UserVO> users = pageInfo.getList();
        model.addAttribute("users", users);
        model.addAttribute("currentPage", pageInfo.getPageNum());
        model.addAttribute("totalPages", pageInfo.getPages());
        model.addAttribute("totalItems", pageInfo.getTotal());
        model.addAttribute("pageTitle", "用户管理");
        model.addAttribute("activeMenu", "users");
        model.addAttribute("contentTemplate", "user-list");
        return "admin-layout";
    }
}

