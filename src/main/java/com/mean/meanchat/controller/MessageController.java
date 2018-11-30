package com.mean.meanchat.controller;

import com.mean.meanchat.WebSocketHandler;
import com.mean.meanchat.bean.User;
import com.mean.meanchat.mapper.UserMapper;
import org.hibernate.annotations.Check;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: MeanChat
 * @description:
 * @author: MeanFan
 * @create: 2018-11-29 14:28
 **/
@RestController
@RequestMapping("/")
public class MessageController {
    public static final String SESSION_UID = "uid";
    private final UserMapper userMapper;
    private final WebSocketHandler webSocketHandler;

    @Autowired
    public MessageController(UserMapper userMapper, WebSocketHandler webSocketHandler) {
        this.userMapper = userMapper;
        this.webSocketHandler = webSocketHandler;
    }

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @PostMapping("/login")
    public Map<String,Object> login(String username, String password, HttpSession session){
        Map<String,Object> rst = new HashMap<>();
        Integer uid = (Integer) session.getAttribute(SESSION_UID);
        if(uid!=null){ //一个会话只能同时登录一个账号
            rst.put("isSuccess", false);
            rst.put("message", "already login");
            return rst;
        }
        User user = userMapper.selectByUsername(username);
        if(user==null){
            rst.put("isSuccess", false);
            rst.put("message", "username not exist");
            return rst;
        }
        if(user.getPassword().compareTo(password)!=0){
            rst.put("isSuccess", false);
            rst.put("message", "username or password wrong");
            return rst;
        }
        session.setAttribute(SESSION_UID, user.getUid());
        rst.put("isSuccess", true);
        rst.put("message", "login success");
        rst.put("session",session.getId());
        System.out.println("userId: "+user.getUid()+" is online");
        return rst;
    }

    @RequestMapping("/getUserMe")
    public Map<String,Object> getUserMe(HttpSession session){
        Map<String,Object> rst = new HashMap<>();
        User user = userMapper.selectByPrimaryKey((Integer)session.getAttribute(SESSION_UID));
        if(user == null){
            rst.put("isSuccess", false);
            rst.put("message", "login expired");
            return rst;
        }
        rst.put("isSuccess", true);
        rst.put("message", "get user success");
        rst.put("user", user);
        return rst;
    }


    @RequestMapping("/logout")
    public Map<String,Object> logout(HttpSession session){
        Map<String,Object> rst = new HashMap<>();
        if(session.getAttribute(SESSION_UID) == null){
            rst.put("isSuccess", false);
            rst.put("message", "not login yet");
            return rst;
        }
        session.removeAttribute(SESSION_UID);
        session.invalidate();
        rst.put("isSuccess", true);
        System.out.println("user offline");
        return rst;
    }

    @RequestMapping("/send/message")
    public  Map<String,Object> sendMessage(HttpSession session, Integer receiver, String message) {
        Map<String,Object> rst = new HashMap<>();
        Integer uid = (Integer) session.getAttribute(SESSION_UID);
        if(uid==null){
            rst.put("isSuccess", false);
            rst.put("message", "not login yet");
            return rst;
        }
        Map<String,Object> webSocketRst = webSocketHandler.sendMessageToUser(uid, receiver,message);
        if(webSocketRst==null){
            rst.put("isSuccess", false);
            rst.put("message", "error");
            return rst;
        }
        return webSocketRst;
    }
}
