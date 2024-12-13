package com.example.chat.MyUtils;

import lombok.Data;

@Data
public class MessageBuilder {
    private String username;
    private String message;

    public MessageBuilder(String username, String message) {
        this.username = username;
        this.message = message;
    }
}
