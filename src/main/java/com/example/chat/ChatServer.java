package com.example.chat;

import com.example.chat.MyUtils.ConfigReader;
import com.example.chat.MyUtils.MessageBuilder;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final ConfigReader configReader = ConfigReader.getInstance();

    static Set<ClientHandler> clientHandlers = new HashSet<>();

    private static final Logger logger = LoggerFactory.getLogger(ChatServer.class);

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(configReader.getPort())) {
            logger.info("Waiting for clients...");
            while (true) {
                Socket socket = serverSocket.accept();
                logger.info("Client connected");
                ClientHandler clientHandler = new ClientHandler(socket);
                clientHandlers.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException ex) {
            logger.error("Server error {}", ex.getMessage());
        }
    }

    static void broadcast(MessageBuilder message) {
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.sendMessage(message);
        }
    }

    static void sendMessageToUser(MessageBuilder message, String username) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.getUsername().equals(username)) {
                clientHandler.sendMessage(message);
                break;
            }
        }
    }
}

class ClientHandler extends Thread {
    private final Socket socket;
    private PrintWriter writer;
    private MessageBuilder jsonMessage;
    private String username;
    private String text;


    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (InputStream input = socket.getInputStream(); BufferedReader reader = new BufferedReader(new InputStreamReader(input)); OutputStream output = socket.getOutputStream(); PrintWriter writer = new PrintWriter(output, true)) {
            this.writer = writer;

            // read username
            this.username = reader.readLine();
            logger.info("Client connected with username {}", username);

            Gson gsReader = new Gson();
            do {
                this.jsonMessage = gsReader.fromJson(reader.readLine(), MessageBuilder.class);
                text = jsonMessage.getMessage();
                if (text.startsWith("@")) {
                    int spaceIndex = text.indexOf(' ');
                    String recipient = text.substring(1, spaceIndex);
                    jsonMessage.setMessage(text.substring(spaceIndex + 1));

                    if (jsonMessage.getMessage().equals("null")) logger.warn("Null message received");

                    logger.info("Message from {} to {}", username, recipient);

                    ChatServer.sendMessageToUser(jsonMessage, recipient);
                } else {
                    logger.info("Broadcast message {} from {}", text, username);

                    if (text.equals("null")) logger.warn("Null message received");

                    ChatServer.broadcast(jsonMessage);
                }
                //запись в файл
                MessageBuilder.AddMessage(MessageBuilder.GetMessages(), jsonMessage);

            } while (!text.equalsIgnoreCase("exit"));

            socket.close();
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
        } finally {
            ChatServer.clientHandlers.remove(this);
            logger.info("Client {} disconnected", username);
        }
    }

    void sendMessage(MessageBuilder message) {
        Gson gsWriter = new Gson();
        if (writer != null) {
            writer.println(gsWriter.toJson(message, MessageBuilder.class));
        }
    }

    String getUsername() {
        return username;
    }
}
