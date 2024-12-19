module com.example.chat {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires com.google.gson;
    requires static lombok;
    requires java.desktop;


    opens com.example.chat to javafx.fxml, com.google.gson;
    exports com.example.chat;
    exports com.example.chat.MyUtils;
    opens com.example.chat.MyUtils to com.google.gson, javafx.fxml;
    exports com.example.chat.MyUtils.MyExceptiosn;
    opens com.example.chat.MyUtils.MyExceptiosn to com.google.gson, javafx.fxml;
}