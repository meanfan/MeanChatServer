package com.mean.meanchat.controller;

import com.mean.meanchat.WebSocketHandler;
import com.mean.meanchat.bean.User;
import com.mean.meanchat.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public static final String RST_IS_SUCCESS = "isSuccess";
    public static final String RST_MESSAGE = "message";
    public static final String RST_USER = "user";
    private final UserMapper userMapper;
    private final WebSocketHandler webSocketHandler;

    @Autowired
    public MessageController(UserMapper userMapper, WebSocketHandler webSocketHandler) {
        this.userMapper = userMapper;
        this.webSocketHandler = webSocketHandler;
    }

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("/serverstatus")
    public String status(){return "normal";}

    @PostMapping("/login")
    public Map<String,Object> login(HttpSession session, String username, String password){
        Map<String,Object> rst = new HashMap<>();
        Integer uid = (Integer) session.getAttribute(SESSION_UID);
        if(uid!=null){ //一个会话只能同时登录一个账号
            rst.put(RST_IS_SUCCESS, false);
            rst.put(RST_MESSAGE, "already login");
            return rst;
        }
        User user = userMapper.selectByUsername(username);
        if(user==null){
            rst.put(RST_IS_SUCCESS, false);
            rst.put(RST_MESSAGE, "username not exist");
            return rst;
        }
        if(user.getPassword().compareTo(password)!=0){
            rst.put(RST_IS_SUCCESS, false);
            rst.put(RST_MESSAGE, "username or password wrong");
            return rst;
        }
        session.setAttribute(SESSION_UID, user.getUid());
        rst.put(RST_IS_SUCCESS, true);
        rst.put(RST_MESSAGE, "login success");
        System.out.println("userId: "+user.getUid()+" is online, sessionId:"+session.getId());
        return rst;
    }

    @RequestMapping("/getMyInfo")
    public Map<String,Object> getUserMe(HttpSession session){
        Map<String,Object> rst = new HashMap<>();
        User user = userMapper.selectByPrimaryKey((Integer)session.getAttribute(SESSION_UID));
        if(user == null){
            rst.put(RST_IS_SUCCESS, false);
            rst.put(RST_MESSAGE, "login expired");
            return rst;
        }
        rst.put(RST_IS_SUCCESS, true);
        rst.put(RST_MESSAGE, "get user success");
        rst.put(RST_USER, user);
        return rst;
    }


    @RequestMapping("/logout")
    public Map<String,Object> logout(HttpSession session){
        Map<String,Object> rst = new HashMap<>();
        if(session.getAttribute(SESSION_UID) == null){
            rst.put(RST_IS_SUCCESS, false);
            rst.put(RST_MESSAGE, "not login yet");
            return rst;
        }
        session.removeAttribute(SESSION_UID);
        session.invalidate();
        rst.put(RST_IS_SUCCESS, true);
        System.out.println("user offline");
        return rst;
    }

    @PostMapping("/register")
    public Map<String,Object> register(HttpSession session,String username,String password,String nickname){
        Map<String,Object> rst = new HashMap<>();
        if(session.getAttribute(SESSION_UID)!=null){ //登录状态不允许注册
            rst.put(RST_IS_SUCCESS, false);
            rst.put(RST_MESSAGE, "already login, please logout and retry");
            return rst;
        }
        //TODO 注册信息格式检查
        if(username==null ||username.isEmpty() || password==null|| password.isEmpty()){
            rst.put(RST_IS_SUCCESS, false);
            rst.put(RST_MESSAGE, "username or password can't be empty");
            return rst;
        }

        //用户名冲突检查
        User user = userMapper.selectByUsername(username);
        if(user!=null){
            rst.put(RST_IS_SUCCESS, false);
            rst.put(RST_MESSAGE, "username already exist");
            return rst;
        }
        user = new User();
        user.setUsername(username);
        user.setPassword(password);
        if(nickname==null || nickname.isEmpty()){
            nickname=username;
        }
        user.setNickname(nickname);
        userMapper.insert(user);
        if(true) { //TODO 判断数据库插入是否成功
            rst.put(RST_IS_SUCCESS, true);
            return rst;
        }else {
            rst.put(RST_IS_SUCCESS, false);
            rst.put(RST_MESSAGE, "server error, please retry");
            return rst;
        }
    }

    @RequestMapping("/send/message")
    public  Map<String,Object> sendMessage(HttpSession session, Integer receiver, String message) {
        Map<String,Object> rst = new HashMap<>();
        Integer uid = (Integer) session.getAttribute(SESSION_UID);
        if(uid==null){
            rst.put(RST_IS_SUCCESS, false);
            rst.put(RST_MESSAGE, "not login yet");
            return rst;
        }
        Map<String,Object> webSocketRst = webSocketHandler.sendMessageToUser(uid, receiver,message);
        if(webSocketRst==null){
            rst.put(RST_IS_SUCCESS, false);
            rst.put(RST_MESSAGE, "error");
            return rst;
        }
        return webSocketRst;
    }
}
