package com.mean.meanchat.bean;

import java.util.Date;

public class User {
    private Integer uid;

    private String username;

    private String nickname;

    private String password;

    private Date timeRegister;

    private Date timeLastLogin;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Date getTimeRegister() {
        return timeRegister;
    }

    public void setTimeRegister(Date timeRegister) {
        this.timeRegister = timeRegister;
    }

    public Date getTimeLastLogin() {
        return timeLastLogin;
    }

    public void setTimeLastLogin(Date timeLastLogin) {
        this.timeLastLogin = timeLastLogin;
    }
}