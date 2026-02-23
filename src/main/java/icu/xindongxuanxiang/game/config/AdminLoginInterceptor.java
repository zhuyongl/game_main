package icu.xindongxuanxiang.game.config;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AdminLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("ADMIN_USER") != null) {
            return true;
        }
        String requestUri = request.getRequestURI();
        if (requestUri.startsWith(request.getContextPath() + "/admin/login")) {
            return true;
        }
        response.sendRedirect(request.getContextPath() + "/admin/login");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        if (modelAndView != null && modelAndView.getModel() != null) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                Object adminUser = session.getAttribute("ADMIN_USER");
                modelAndView.getModel().put("adminUser", adminUser);
            }
        }
    }
}
