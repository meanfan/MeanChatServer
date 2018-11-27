package com.mean.meanchat.controller;

import com.mean.meanchat.bean.User;
import com.mean.meanchat.mapper.UserMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
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
 * @create: 2018-11-26 21:18
 **/
@RestController
@EnableAutoConfiguration
@RequestMapping("/app")
public class UserController {
    @Autowired
    UserMapper userMapper;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public Map<String,Object> login(String username, String password, HttpSession session){
        Map<String,Object> rst = new HashMap<>();
        Integer uid = (Integer) session.getAttribute("uid");
        if(uid!=null){ //一个会话只能同时登录一个账号
            rst.put("isSuccess", false);
            rst.put("message", "already logged");
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
        session.setAttribute("user", username);
        session.setAttribute("uid", user.getUid());
        rst.put("isSuccess", true);
        rst.put("message", "login success");
        rst.put("session",session.getId());
        return rst;
    }

    @GetMapping("/getUserMe")
    public Map<String,Object> getUserMe(HttpSession session){
        Map<String,Object> rst = new HashMap<>();
        User user = userMapper.selectByPrimaryKey((Integer)session.getAttribute("uid"));
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


    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        session.invalidate();
        return "logout success";
    }
}
