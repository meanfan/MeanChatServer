package com.mean.meanchat;

import com.alibaba.druid.support.json.JSONUtils;
import com.mean.meanchat.controller.MessageController;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: MeanChat
 * @description:
 * @author: MeanFan
 * @create: 2018-11-29 15:58
 **/
@Component
public class WebSocketHandler extends TextWebSocketHandler {
    //在线用户列表
    private static final Map<Integer, WebSocketSession> userWebSocketSessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Integer uid = getWebSocketSessionUid(session);
        if (uid != null) {
            userWebSocketSessions.put(uid, session);
            System.out.println("WebSocket: connect authorize success:"+uid);
            session.sendMessage(new TextMessage("connect success"));
        }else {
            System.out.println("WebSocket: connect authorize failure");
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        System.out.println("WebSocket: connect error");
        userWebSocketSessions.remove(getWebSocketSessionUid(session));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("WebSocket: disconnected");
        userWebSocketSessions.remove(getWebSocketSessionUid(session));
    }

    private Integer getWebSocketSessionUid(WebSocketSession session) {
        try {
            return (Integer) session.getAttributes().get(MessageController.SESSION_UID);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);
    }

    public Map<String,Object> sendMessageToUser(Integer sender, Integer receiver, String message){
        Map<String,Object> rst = new HashMap<>();
        System.out.println("sendMessageToUser:"+sender+" to "+ receiver+" message: "+message);
        if (userWebSocketSessions.get(sender) == null) { //未登录非法操作
            rst.put("isSuccess", false);
            rst.put("message", "websocket: not login yet");
            return rst;
        }
        //TODO 存入数据库
        rst.put("isSuccess", true);
        if(userWebSocketSessions.get(receiver) != null) { //对方在线
            WebSocketSession receiverSession = userWebSocketSessions.get(receiver);
            rst.put("isReceiverOnline", true);
            if (receiverSession.isOpen()) {
                String json = JSONUtils.toJSONString(rst);
                TextMessage testMessage = new TextMessage(json);
                try {
                    receiverSession.sendMessage(testMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                rst.put("isReceiverOnline", false);
            }
        }else {//对方不在线
            return rst;
        }
        return rst;
    }
}
