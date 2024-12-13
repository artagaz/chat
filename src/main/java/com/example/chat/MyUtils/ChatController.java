package com.example.chat.MyUtils;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import lombok.Setter;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    private static final ConfigReader reader = ConfigReader.getInstance();
    private BufferedWriter bufferedWriter;
    private ObjectOutputStream out;

    @Setter
    static String username;

    @FXML
    public Button EnterButton;
    @FXML
    private Button SendButton;
    @FXML
    private TextField InputField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Socket socket = new Socket(reader.getHost(), reader.getPort());
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Gson message = new Gson();

        EnterButton.setOnAction(actionEvent -> {
            try {
                out.writeObject(message.toJson(new MessageBuilder(username, "Connected"), MessageBuilder.class));

                EnterButton.setVisible(false);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        SendButton.setOnAction(actionEvent -> {
            if (!InputField.getText().isEmpty()) {
                try {
                    out.writeObject(message.toJson(new MessageBuilder(username, InputField.getText()), MessageBuilder.class));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
