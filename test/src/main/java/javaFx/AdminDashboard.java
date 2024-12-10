package javaFx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.application.Application.launch;

public class AdminDashboard  {

    public static void main(String[] args) {
        launch(args);
    }


    public static void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(AdminDashboard.class.getResource("/AdminSystem.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Admin Dashboard");
        primaryStage.show();
    }
}
