package com.example.chat.MyUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataBaseActions {

    public static ArrayList<Users> GetUsers() {
        Gson gsonRead = new Gson();
        ArrayList<Users> users;

        try (FileReader reader = new FileReader("src/main/resources/users.json")) {
            Type ArrayListType = new TypeToken<List<Users>>() {
            }.getType();
            users = gsonRead.fromJson(reader, ArrayListType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return users;
    }

    public static String AddUser(ArrayList<Users> users, String login, String password, String passwordAgain) {
        //проверяем введенные данные
        if (login.isEmpty() || password.isEmpty() || passwordAgain.isEmpty()) return "Please fill in all fields";
        if (!passwordAgain.equals(password)) return "Password don't math";
        for (Users user : users) if (Objects.equals(user.getUsername(), login)) return "User already exist";

        //добавляем нового пользователя
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        users.add(new Users(login, password));
        try (FileWriter writer = new FileWriter("src/main/resources/users.json")) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "User added, you can login now";

    }

}



