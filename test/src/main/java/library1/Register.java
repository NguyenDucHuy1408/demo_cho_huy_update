package library1;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Register {

    private AccountManager accountManager;

    // Constructor nhận AccountManager
    public Register(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public void show(Stage primaryStage) {
        // Tạo các trường nhập liệu và nút cho giao diện đăng ký
        Label title = new Label("Library Registration");

        // Các trường nhập liệu
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter Username");

        TextField userIdField = new TextField();
        userIdField.setPromptText("Enter User ID");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");

        Button registerButton = new Button("Register");

        // Xử lý sự kiện khi nhấn nút đăng ký
        registerButton.setOnAction(event -> {
            String username = usernameField.getText();
            String userId = userIdField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            // Kiểm tra xem mật khẩu và xác nhận mật khẩu có khớp không
            if (!password.equals(confirmPassword)) {
                System.out.println("Passwords do not match.");
                return;
            }

            // Đăng ký người dùng
            boolean registered = accountManager.register(userId, username, password, "Member");

            if (registered) {
                System.out.println("Registration successful! You can now login.");
                primaryStage.close(); // Đóng cửa sổ đăng ký sau khi đăng ký thành công
                // Có thể mở lại cửa sổ đăng nhập nếu cần
            } else {
                System.out.println("Registration failed. Please try again.");
            }
        });

        // Layout cho giao diện đăng ký
        VBox layout = new VBox(10);
        layout.getChildren().addAll(title, usernameField, userIdField, passwordField, confirmPasswordField, registerButton);
        layout.setAlignment(Pos.CENTER);

        // Hiển thị giao diện
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setTitle("Library Registration");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
