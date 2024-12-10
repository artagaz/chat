package com.example.chat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Chat extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Chat.class.getResource("Chat.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Chat");
        stage.setScene(scene);
        stage.show();
    }


}
