package com.example.chat.MyUtils.MyExceptiosn;

public class MessagesDontExist extends Exception {
    public MessagesDontExist() {
        super("message is null");
    }
    public MessagesDontExist(String message) {
        super(message);
    }
}
