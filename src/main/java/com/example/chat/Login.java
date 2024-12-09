package com.example.chat;

import com.google.gson.GsonBuilder;
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
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.List;
import java.util.Objects;

@Slf4j
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
        //все поля заполнены
        if (RegisterLogin.getText().equals("") || RegisterPassword.getText().equals("") || RegisterPasswordAgain.getText().equals("")) {
            Errorslabel.setText("Please fill in all fields");
            return;
        }

        //пароли совпадают
        if (!RegisterPassword.getText().equals(RegisterPasswordAgain.getText())) {
            Errorslabel.setText("Password don't math");
            return;
        }

        //проверка на уникальность логина
        Gson gsonRead = new Gson();
        Users[] users;
        try (FileReader reader = new FileReader("src/main/resources/users.json")) {
            users = gsonRead.fromJson(reader, Users[].class);
            System.out.println(users[1].getUsername());
            System.out.println(users[1].getPassword());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < users.length; i++) {
            if (Objects.equals(users[i].getUsername(), RegisterLogin.getText())) {
                Errorslabel.setText("Please fill in all fields");
                return;
            } else {
                Users tempUser = new Users(RegisterLogin.getText(), RegisterPassword.getText());
                Gson gson = new Gson();
                try (FileReader reader = new FileReader("src/main/resources/users.json")) {
                    Users[] userAdded = new Users[users.length+1];
                    userAdded.clone();
                    try (FileWriter writer = new FileWriter("src/main/resources/users.json")) {
                        gson.toJson(users, writer);
                    }
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
        }

    }
}

@Data
class Users {
    private String username;
    private String password;

    public Users(String login, String pwd) {
        username = login;
        password = pwd;
    }
}

@Data
class DataUsers {
    List<Users> usersList;
}

