package com.example.chat.MyUtils.MyExceptiosn;

public class NullUserName extends Exception {
    public NullUserName() {
        super("Username is null");
    }
    public NullUserName(String message) {
        super(message);
    }
}
