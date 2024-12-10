package com.example.chat.MyUtils;

import lombok.Data;

@Data
public class Users {
    private String username;
    private String password;
    byte[] salt;

    public Users(String transmittedUsername, String transmittedPassword, byte[] transmittedSalt) {
        username = transmittedUsername;
        password = transmittedPassword;
        salt = transmittedSalt;
    }
}
