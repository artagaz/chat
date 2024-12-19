package com.example.chat.MyUtils.MyExceptiosn;

public class NullMessage extends Exception {
    public NullMessage() {
        super("message is null");
    }
    public NullMessage(String message) {
        super(message);
    }
}
