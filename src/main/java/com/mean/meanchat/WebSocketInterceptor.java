package com.mean.meanchat;

import com.mean.meanchat.controller.MessageController;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @program: MeanChat
 * @description:
 * @author: MeanFan
 * @create: 2018-11-29 16:05
 **/
@Component
public class WebSocketInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
                                   WebSocketHandler webSocketHandler, Map<String, Object> attributes) throws Exception {
        System.out.println("beforeHandshake");
        if (serverHttpRequest instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest request = (ServletServerHttpRequest) serverHttpRequest;
            HttpSession session = request.getServletRequest().getSession();
            if (session != null) {
                System.out.println(session.getId());
                Integer uid = (Integer) session.getAttribute(MessageController.SESSION_UID);
                //TODO uid获取有问题，一直为null
                if(uid != null) {
                    attributes.put(MessageController.SESSION_UID, uid); //将当前用户uid与WebSock绑定
                    System.out.println("websocket bind success:" + uid);
                }
            }

        }
        return true;
    }


    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

    }
}
