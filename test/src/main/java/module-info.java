module org.example.test2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires java.desktop;
    requires mysql.connector.java;
    requires jdk.compiler;

    opens org.example.test2 to javafx.fxml;
    exports org.example.test2;
    opens library1 to javafx.fxml;
    exports library1;
    exports javaFx;
    opens javaFx to javafx.fxml;
    requires java.base;
    requires javafx.base;
}
