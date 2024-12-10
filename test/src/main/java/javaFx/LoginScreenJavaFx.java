package javaFx;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import library1.AccountManager;
import library1.User;

import java.io.IOException;

public class LoginScreenJavaFx {

    private AccountManager accountManager;
    private static User loggedInUser;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    // Constructor không tham số bắt buộc bởi FXML
    public LoginScreenJavaFx() {
        // Không cần xử lý tại đây
    }

    // Setter để truyền AccountManager từ bên ngoài
    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    /**
     * Phương thức xử lý đăng nhập khi nhấn nút Login.
     */
    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (accountManager == null) {
            System.out.println("AccountManager is not initialized.");
            return;
        }

        User user = accountManager.login(username, password);
        if (user != null) {
            loggedInUser = user;
            if("Member".equals(user.getRole())) {
                try {
                    UserDashboard.start(new Stage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // Chuyển sang giao diện member
                ((Stage) loginButton.getScene().getWindow()).close();
                System.out.println("Login successful! Welcome " + user.getName());
                //return;
            } else {
                System.out.println("Login successful! Welcome " + user.getName());
                try {
                    AdminDashboard.start(new Stage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                ((Stage) loginButton.getScene().getWindow()).close();
            }
            // Ví dụ: Chuyển đến màn hình LibrarySystem
            // LibrarySystem.show(new Stage(), user, accountManager);
        } else {
            // Hiển thị thông báo lỗi khi đăng nhập không thành công
            showAlert(AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }

    /**
     * Phương thức hiển thị thông báo lỗi (Alert).
     */
    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Phương thức xử lý khi nhấn nút Register.
     */

    @FXML
    private void handleRegister(ActionEvent event) {
        ((Stage) loginButton.getScene().getWindow()).close();
        RegisterScreen.show(new Stage(), accountManager);
    }

    public static String getLoggedInUserID() {
        if (loggedInUser != null) {
            return loggedInUser.getUserId(); // Trả về userID từ đối tượng User
        } else {
            return null; // Không có user nào đăng nhập
        }
    }

    /**
     * Hiển thị màn hình đăng nhập.
     */
    public static void show(Stage stage, AccountManager accountManager) {
        try {
            FXMLLoader loader = new FXMLLoader(LoginScreenJavaFx.class.getResource("/LoginScreenJavaFx.fxml"));
            Parent root = loader.load();

            // Thiết lập controller và truyền AccountManager
            LoginScreenJavaFx controller = loader.getController();
            controller.setAccountManager(accountManager);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Login Screen");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
