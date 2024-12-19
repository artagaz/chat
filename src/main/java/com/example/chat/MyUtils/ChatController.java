package com.example.chat.MyUtils;

import com.example.chat.MyUtils.MyExceptiosn.NullMessage;
import com.example.chat.MyUtils.MyExceptiosn.NullUserName;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    private static final ConfigReader reader = ConfigReader.getInstance();
    Socket socket;
    private PrintWriter writer;
    BufferedReader bufferedReader;

    @Setter
    static String username;

    @FXML
    public Button EnterButton;
    @FXML
    private Button SendButton;
    @FXML
    private TextField InputField;
    @FXML
    private Button LoadMessagesButton;
    @FXML
    private ScrollPane SP_Main;
    @FXML
    private VBox chatBox;
    @FXML
    private Label ActiveUsers;

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.socket = new Socket(reader.getHost(), reader.getPort());
            OutputStream output = socket.getOutputStream();
            this.writer = new PrintWriter(output, true);
            InputStream input = socket.getInputStream();
            this.bufferedReader = new BufferedReader(new InputStreamReader(input));
            logger.info("Connected to server");

            new Thread(() -> {
                Gson gsReader = new Gson();
                try {
                    while (true) {
                        MessageBuilder message = gsReader.fromJson(bufferedReader.readLine(), MessageBuilder.class);
                        String activeUsers = gsReader.fromJson(bufferedReader.readLine(), String.class);
                        addMessage(message, activeUsers);
                        logger.info("message: {} created", message);
                    }
                } catch (IOException ex) {
                    System.out.println("Server connection closed: " + ex.getMessage());
                }
            }).start();

        } catch (IOException e) {
            logger.error("Can't connect to server");
            throw new RuntimeException(e);
        }

        EnterButton.setOnAction(actionEvent -> {
            try {
                if (username == null) throw new NullUserName();

                writer.println(username);
                EnterButton.setVisible(false);
            } catch (NullUserName e) {
                logger.error("username is null, set default username");
                username = "default";
            }

            logger.info("user {} logged in", username);
        });

        SendButton.setOnAction(actionEvent -> {
            Gson message = new Gson();

            try {
                if (InputField.getText().isEmpty()) throw new NullMessage();
                writer.println(message.toJson(new MessageBuilder(username, InputField.getText()), MessageBuilder.class));
                logger.info("message: {} sent", InputField.getText());
            } catch (NullMessage e) {
                logger.error("message is null, nothing to send");
            }
        });

        LoadMessagesButton.setOnAction(actionEvent -> {
            ArrayList<MessageBuilder> messagesToLoad = MessageBuilder.GetMessages();
            chatBox.getChildren().remove(0, chatBox.getChildren().size());

            for (MessageBuilder message : messagesToLoad) {
                addMessage(message, "not changed");
            }

            logger.info("messages loaded");
        });

        chatBox.heightProperty().addListener((observableValue, oldValue, newValue) -> SP_Main.setVvalue((Double) newValue));
    }

    private void addMessage(MessageBuilder message, String activeUsers) {
        if (message.getMessage() != null) {
            HBox hBox = new HBox();

            Text userName = new Text(message.getUsername() + ": ");
            TextFlow userNameFlow = new TextFlow(userName);
            Text text = new Text(message.getMessage());
            TextFlow textFlow = new TextFlow(text);

            if (message.getUsername().equals(username)) {
                hBox.setAlignment(Pos.CENTER_RIGHT);
                hBox.setPadding(new Insets(5, 5, 5, 5));

                userNameFlow.setStyle("-fx-color: rgb(239, 242, 255); " + "-fx-background-color: rgb(15, 125, 242); ");
                userNameFlow.setPadding(new Insets(5, 5, 5, 5));
                userName.setFill(Color.color(0.1, 0.1, 0.1));

                textFlow.setStyle("-fx-color: rgb(239, 242, 255); " + "-fx-background-color: rgb(15, 125, 242); ");
                textFlow.setPadding(new Insets(5, 5, 5, 5));
                text.setFill(Color.color(0.934, 0.945, 0.996));
            } else {
                hBox.setAlignment(Pos.CENTER_LEFT);
                hBox.setPadding(new Insets(5, 5, 5, 5));

                userNameFlow.setStyle("-fx-color: rgb(239, 242, 255); " + "-fx-background-color: rgb(204, 229, 255); ");
                userNameFlow.setPadding(new Insets(5, 5, 5, 5));
                userName.setFill(Color.color(0.1, 0.1, 0.1));

                textFlow.setStyle("-fx-color: rgb(239, 242, 255); " + "-fx-background-color: rgb(204, 229, 255); ");
                textFlow.setPadding(new Insets(5, 5, 5, 5));
                text.setFill(Color.color(0.06, 0.49, 0.95));
            }

            hBox.getChildren().add(userNameFlow);
            hBox.getChildren().add(textFlow);

            Platform.runLater(() -> {
                chatBox.getChildren().add(hBox);

                if (!activeUsers.equals("not changed")) {
                    ActiveUsers.setText(activeUsers);
                }

                logger.info("message added, active users: {}", activeUsers);
            });
        }
    }

}
