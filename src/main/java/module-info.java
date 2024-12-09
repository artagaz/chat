module com.example.chat {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires com.google.gson;
    requires static lombok;


    opens com.example.chat to javafx.fxml, com.google.gson;
    exports com.example.chat;
}