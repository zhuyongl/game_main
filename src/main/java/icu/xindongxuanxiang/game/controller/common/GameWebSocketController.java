package icu.xindongxuanxiang.game.controller.common;

import org.springframework.web.bind.annotation.RestController;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@RestController
@ServerEndpoint("/ws/game/{userId}")
public class GameWebSocketController {


    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        // 用户上线
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 处理游戏消息
    }

    @OnClose
    public void onClose(Session session) {
        // 用户下线
    }
}
