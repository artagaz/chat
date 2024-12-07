package com.example.chat;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import com.google.gson.Gson;
import lombok.Data;

import java.io.*;

public class Login extends Application {
    public ToggleGroup LogRegSelect;

    public AnchorPane RegisterPage;
    public AnchorPane LoginPage;

    public TextField RegisterLogin;
    public TextField RegisterPassword;
    public TextField RegisterPasswordAgain;
    public Label Errorslabel;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Chat.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login page");
        stage.setScene(scene);
        stage.show();
    }

    //register or login selection
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

    public void RegisterButton(ActionEvent actionEvent) {
//        if (RegisterLogin.getText().equals("") || RegisterPassword.getText().equals("") || RegisterPasswordAgain.getText().equals("")) {
//            Errorslabel.setText("Please fill in all fields");
//            return;
//        }

        Gson gson = new Gson();
        try (FileReader reader = new FileReader("src/main/resources/users.json")) {
            Users obj = gson.fromJson(reader, Users.class);
            System.out.println(obj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

@Data
class Users {
   private String username;
   private String password;
}

