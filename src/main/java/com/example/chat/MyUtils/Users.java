package com.example.chat.MyUtils;

import lombok.Data;

@Data
public class Users {
    private String username;
    private String password;

    public Users(String login, String pwd) {
        username = login;
        password = pwd;
    }
}
