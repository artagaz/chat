package com.example.chat.MyUtils;

import com.example.chat.MyUtils.MyExceptiosn.MessagesDontExist;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Data
public class MessageBuilder {
    private String username;
    private String message;

    private static final Logger logger = LoggerFactory.getLogger(MessageBuilder.class);

    public MessageBuilder(String username, String message) {
        this.username = username;
        this.message = message;
    }

    public static ArrayList<MessageBuilder> GetMessages() {
        Gson gsonRead = new Gson();
        ArrayList<MessageBuilder> messages;

        try (FileReader reader = new FileReader("src/main/resources/messages.json")) {
            Type ArrayListType = new TypeToken<List<MessageBuilder>>() {
            }.getType();
            messages = gsonRead.fromJson(reader, ArrayListType);
            if (messages.isEmpty()) throw new MessagesDontExist();
        } catch (MessagesDontExist e) {
            messages = new ArrayList<>();
            messages.add(new MessageBuilder("System", "Start chatting first"));
            logger.error("Нет сообщений в файле messages.json, заглушка показана");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return messages;
    }

    public static void AddMessage(ArrayList<MessageBuilder> messages, MessageBuilder message) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        messages.add(message);

        //сохраняем данные
        try (FileWriter writer = new FileWriter("src/main/resources/messages.json")) {
            gson.toJson(messages, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
