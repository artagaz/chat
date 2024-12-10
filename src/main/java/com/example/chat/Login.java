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
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;

//мои
import com.example.chat.MyUtils.Users;
import com.example.chat.MyUtils.DataBaseActions;

@Slf4j
public class Login extends Application {
    public ToggleGroup LogRegSelect;

    public AnchorPane RegisterPage;
    public AnchorPane LoginPage;

    public TextField RegisterLogin;
    public TextField RegisterPassword;
    public TextField RegisterPasswordAgain;
    public Label Errorslabel;

    public TextField LoginPassword;
    public TextField LoginLogin;

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
        Errorslabel.setText("");
        ArrayList<Users> users = DataBaseActions.GetUsers();

        Errorslabel.setText(DataBaseActions.AddUser(users, RegisterLogin.getText(), RegisterPassword.getText(), RegisterPasswordAgain.getText()));
    }

    public void LoginButton(ActionEvent actionEvent) {
        //все поля заполнены
        if (LoginLogin.getText().isEmpty() || LoginPassword.getText().isEmpty()) {
            Errorslabel.setText("Please fill in all fields");
            return;
        }

        //проверка логина
        ArrayList<Users> users = DataBaseActions.GetUsers();
        for (Users user : users) {

            if (user.getUsername().equals(LoginLogin.getText())) {
                String transmittedPassword = DataBaseActions.HashString(LoginPassword.getText(), user.getSalt());

                if (!user.getPassword().equals(transmittedPassword)) Errorslabel.setText("Wrong password");
                else {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(Chat.class.getResource("Chat.fxml"));
                        Scene scene = new Scene(fxmlLoader.load());
                        Stage stage = new Stage();
                        stage.setTitle(user.getUsername());
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;

            } else Errorslabel.setText("Wrong username");
        }

    }
}



