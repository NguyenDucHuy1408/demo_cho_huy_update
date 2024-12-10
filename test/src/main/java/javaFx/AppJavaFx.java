package javaFx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import library1.AccountManager;


public class AppJavaFx extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Khởi tạo AccountManager để xử lý đăng nhập và đăng ký
        AccountManager accountManager = new AccountManager();

        // Khởi tạo và hiển thị màn hình đăng nhập
        LoginScreenJavaFx loginScreenJavaFx = new LoginScreenJavaFx();
        loginScreenJavaFx.setAccountManager(accountManager); // Đảm bảo set AccountManager
        loginScreenJavaFx.show(primaryStage, accountManager); // Hiển thị màn hình đăng nhập
    }

    public static void main(String[] args) {
        launch(args); // Khởi chạy ứng dụng JavaFX
    }
}
