package library1;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class LoginScreen {

    private AccountManager accountManager;

    public LoginScreen(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public void show(Stage primaryStage) {
        // Tạo hình nền
        Image backgroundImage = new Image("file:///D:/Java/Library-management//Library-management//LoginScreen"); // Thay đổi đường dẫn đến hình nền của bạn
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);

        // Tạo layout cho giao diện đăng nhập
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);

        // Tạo các phần tử giao diện
        Label title = new Label("Library Login");
        title.setFont(new Font("Arial", 24));
        title.setTextFill(Color.DARKBLUE);

        // Tạo trường nhập liệu
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter Username");
        usernameField.setStyle("-fx-padding: 10; -fx-background-radius: 10; -fx-border-radius: 10;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setStyle("-fx-padding: 10; -fx-background-radius: 10; -fx-border-radius: 10;");

        // Tạo các nút
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 10;");
        loginButton.setMinWidth(200);

        Button registerButton = new Button("Register");
        registerButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 10;");
        registerButton.setMinWidth(200);

        // Đăng nhập người dùng
        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            User user = accountManager.login(username, password);
            if (user != null) {
                // Đăng nhập thành công, chuyển đến màn hình chính
                System.out.println("Logged in as " + user.getUserId());
            } else {
                // Đăng nhập thất bại
                System.out.println("Invalid username or password");
            }
        });

        // Đăng ký tài khoản
        registerButton.setOnAction(event -> {
            Register register = new Register(accountManager);
            register.show(primaryStage);
            System.out.println("Switch to registration screen");
        });

        // Đặt nền cho layout
        layout.setBackground(new Background(background));

        // Thêm các thành phần vào layout
        layout.getChildren().addAll(title, usernameField, passwordField, loginButton, registerButton);

        // Hiển thị giao diện
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setTitle("Library Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
