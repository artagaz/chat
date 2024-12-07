package com.example.chat;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Login extends Application {
    public ToggleGroup LogRegSelect;

    public AnchorPane RegisterPage;
    public AnchorPane LoginPage;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login page");
        stage.setScene(scene);
        stage.show();
    }

    public void LogRegSelect(ActionEvent actionEvent) {
        RadioButton radioButton = (RadioButton) actionEvent.getSource();
        if (radioButton.getText().equals("Login")) {
            LoginPage.setVisible(true);
            RegisterPage.setVisible(false);
        } else {
            RegisterPage.setVisible(true);
            LoginPage.setVisible(false);
        }
    }
}
