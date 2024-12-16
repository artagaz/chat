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
    Socket socket;
    private PrintWriter writer;

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
            this.socket = new Socket(reader.getHost(), reader.getPort());
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            OutputStream output = socket.getOutputStream();
            this.writer = new PrintWriter(output, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        EnterButton.setOnAction(actionEvent -> {
            writer.println(username);
            EnterButton.setVisible(false);
        });

        SendButton.setOnAction(actionEvent -> {
            Gson message = new Gson();

            if (!InputField.getText().isEmpty()) {
                writer.println(message.toJson(new MessageBuilder(username, InputField.getText()), MessageBuilder.class));
            }
        });
    }

//    public static void addMessage(MessageBuilder message, VBox vbox) {
//        HBox hbox = new HBox();
//        hbox.setAlignment(Pos.CENTER_LEFT);
//        hbox.setPadding(new Insets(5, 5, 5, 10));
//
//        Text text = new Text(message.getMessage());
//        TextFlow textFlow = new TextFlow(text);
//        textFlow.setStyle("-fx-background-color: rgb(233,233,235)" + " -fx-background-radius: 20px");
//        textFlow.setPadding(new Insets(5, 5, 5, 10));
//        hbox.getChildren().add(textFlow);
//
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                vbox.getChildren().add(hbox);
//            }
//        });
//    }
}
