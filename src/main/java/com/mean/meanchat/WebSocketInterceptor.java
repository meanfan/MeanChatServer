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
//@Component
public class WebSocketInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
                                   WebSocketHandler webSocketHandler, Map<String, Object> attributes) {
        System.out.println("websocket before Handshake:");
        if(serverHttpRequest instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest request = (ServletServerHttpRequest) serverHttpRequest;
            HttpSession session = request.getServletRequest().getSession();
            if (session != null) {
                System.out.println("websocket sessionId:" + session.getId());
                Integer uid = (Integer) session.getAttribute(MessageController.SESSION_UID);
                if (uid != null) {
                    attributes.put(MessageController.SESSION_UID, uid); //将当前用户uid与WebSock绑定
                    System.out.println("websocket bind success:" + uid);
                } else {
                    System.out.println("websocket bind failure");

                }
            }
        }
        return true;
    }


    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {

    }
}
