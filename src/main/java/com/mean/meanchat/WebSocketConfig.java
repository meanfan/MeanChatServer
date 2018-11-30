package com.mean.meanchat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

/**
 * @program: MeanChat
 * @description:
 * @author: MeanFan
 * @create: 2018-11-29 13:51
 **/
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private WebSocketHandler webSocketHandler;
    private WebSocketInterceptor webSocketInterceptor;

    @Autowired
    public void setWebSocketHandler(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Autowired
    public void setWebSocketInterceptor(WebSocketInterceptor webSocketInterceptor) {
        this.webSocketInterceptor = webSocketInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(webSocketHandler, "/websocket")
                .addInterceptors(webSocketInterceptor)
                .setAllowedOrigins("*"); //必须设置，否则是有限制的
    }
}
