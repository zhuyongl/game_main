package icu.xindongxuanxiang.game.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminLoginInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login", "/admin/login/**");
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // 管理员模块 路径前缀
        configurer.addPathPrefix("/admin", c -> c.getPackage().getName().startsWith("icu.xindongxuanxiang.game.controller.admin"));
        // api模块 路径前缀
        configurer.addPathPrefix("/api", c -> c.getPackage().getName().startsWith("icu.xindongxuanxiang.game.controller.api"));
    }
}
