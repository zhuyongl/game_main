package icu.xindongxuanxiang.game;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"icu.xindongxuanxiang.game.mapper"})
public class GameMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameMainApplication.class, args);
    }
}
