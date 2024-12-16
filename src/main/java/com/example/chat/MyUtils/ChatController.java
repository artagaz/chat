package com.example.chat.MyUtils;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import lombok.Setter;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    private static final ConfigReader reader = ConfigReader.getInstance();
    private BufferedWriter bufferedWriter;
    private ObjectOutputStream out;
    private ObjectInputStream input;

    @Setter
    static String username;
    MessageBuilder message;

    @FXML
    public Button EnterButton;
    @FXML
    private Button SendButton;
    @FXML
    private TextField InputField;
    @FXML
    private VBox chatBox;
    @FXML
    private ScrollPane scrollPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Socket socket = new Socket(reader.getHost(), reader.getPort());
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            out = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            Gson gson = new Gson();
            try {
                while ((gson = (Gson) input.readObject()) != null) {
                    message = gson.fromJson(String.valueOf(gson), MessageBuilder.class);
                    addMessage(message, chatBox);
                }
            } catch (IOException ex) {
                System.out.println("Server connection closed: " + ex.getMessage());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).start();

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

        chatBox.heightProperty().addListener((observable, oldValue, newValue) -> {
            chatBox.setPrefHeight(newValue.doubleValue());
        });
    }

    public static void addMessage(MessageBuilder message, VBox vbox) {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(message.getMessage());
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: rgb(233,233,235)" + " -fx-background-radius: 20px");
        textFlow.setPadding(new Insets(5, 5, 5, 10));
        hbox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vbox.getChildren().add(hbox);
            }
        });
    }
}
