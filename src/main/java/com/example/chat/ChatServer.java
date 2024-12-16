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

    static void broadcast(MessageBuilder message, ClientHandler excludeUser) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler != excludeUser) {
                clientHandler.sendMessage(message);
            }
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
    private ObjectInputStream input;
    ObjectOutputStream output;

    MessageBuilder userMessage;


    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {

            Gson gson = new Gson();
            this.userMessage = gson.fromJson((String) input.readObject(), MessageBuilder.class);
            logger.info("Client connected with username {}", userMessage.getUsername());

            String text;
            do {
                text = gson.fromJson((String) input.readObject(), MessageBuilder.class).getMessage();
                if (text.startsWith("@")) {
                    int spaceIndex = text.indexOf(' ');
                    String recipient = text.substring(1, spaceIndex);
                    String message = text.substring(spaceIndex + 1);

                    if (message.equals("null"))
                        logger.warn("Null message received");

                    logger.info("Message from {} to {}", userMessage.getUsername(), recipient);

                    ChatServer.sendMessageToUser(userMessage, recipient);
                } else {
                    logger.info("Broadcast message {} from {}", text, userMessage.getUsername());

                    if (text.equals("null"))
                        logger.warn("Null message received");

                    ChatServer.broadcast(userMessage, this);
                }
            } while (!text.equalsIgnoreCase("exit"));

            socket.close();
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            ChatServer.clientHandlers.remove(this);
            logger.info("Client {} disconnected", userMessage.getUsername());
        }
    }

    void sendMessage(MessageBuilder message) {
        if (output != null) {
            try {
                output.writeObject(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    String getUsername() {
        return userMessage.getUsername();
    }
}
