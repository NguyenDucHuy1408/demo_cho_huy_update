package library1;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Khởi tạo AccountManager để xử lý đăng nhập và đăng ký
        AccountManager accountManager = new AccountManager();

        // Khởi tạo và hiển thị màn hình đăng nhập
        LoginScreen loginScreen = new LoginScreen(accountManager);
        loginScreen.show(primaryStage); // Hiển thị màn hình đăng nhập
    }

    public static void main(String[] args) {
        launch(args); // Khởi chạy ứng dụng JavaFX
    }
}
