package com.example.chat;

import com.example.chat.MyUtils.ChatController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Chat extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Chat.class.getResource("Chat.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        if (stage.getTitle() == null) stage.setTitle("AnonChat");
        stage.setScene(scene);
        stage.show();
        ChatController.setUsername(stage.getTitle());
    }


}
