module client {
    requires javafx.controls;
    requires javafx.fxml;


    opens server to javafx.base;
    opens server.models to javafx.base;
    opens client to javafx.fxml;
    exports client;
    exports server;
    exports server.models;
}