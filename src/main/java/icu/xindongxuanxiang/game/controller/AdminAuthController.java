package icu.xindongxuanxiang.game.controller;

import icu.xindongxuanxiang.game.model.entity.User;
import icu.xindongxuanxiang.game.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminAuthController {

    final UserService userService;

    public AdminAuthController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "admin-login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam("username") String username,
                          @RequestParam("password") String password,
                          Model model,
                          HttpServletRequest request) {
        Optional<User> userOptional = userService.getUserByUsername(username);
        if (!userOptional.isPresent() || !password.equals(userOptional.get().getPassword())) {
            model.addAttribute("error", "用户名或密码错误");
            model.addAttribute("username", username);
            return "admin-login";
        }
        HttpSession session = request.getSession(true);
        session.setAttribute("ADMIN_USER", userOptional.get());
        return "redirect:/admin/messages";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/admin/login";
    }
}

