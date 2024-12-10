package javaFx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import library1.AccountManager;
import library1.User;

public class RegisterScreen {

    private AccountManager accountManager;

    @FXML
    private TextField idField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registerButton;

    @FXML
    private Button loginButton;

    @FXML
    private CheckBox adminCheckBox;

    @FXML
    private CheckBox memberCheckBox;
    // Constructor không tham số bắt buộc bởi FXML
    public RegisterScreen() {
        // Không cần xử lý tại đây
    }

    // Setter để truyền AccountManager từ bên ngoài
    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    @FXML
    public void initialize() {
        // Ràng buộc hai CheckBox để luôn có trạng thái ngược nhau
        adminCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                memberCheckBox.setSelected(false);
            }
        });

        memberCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                adminCheckBox.setSelected(false);
            }
        });
    }
    /**
     * Phương thức xử lý đăng ký khi nhấn nút Register.
     */
    @FXML
    private void handleRegister(ActionEvent event) {
        String userId = idField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = null;
        if (adminCheckBox.isSelected()) {
            role = "Admin";
            memberCheckBox.setSelected(false);
        } else if (memberCheckBox.isSelected()) {
            role = "Member";
            adminCheckBox.setSelected(false);
        }

        // Kiểm tra trường dữ liệu
        if (userId == null || userId.isEmpty() ||
                username == null || username.isEmpty() ||
                password == null || password.isEmpty() ||
                role == null) {
            showAlert(AlertType.ERROR, "Registration Failed", "All fields must be filled out, and a role must be selected.");
            return;
        }

        if (accountManager == null) {
            System.out.println("AccountManager is not initialized.");
            return;
        }

        boolean registered = accountManager.register(userId, username, password, role);
        if (registered) {
            System.out.println("Registration successful! You can now login.");
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Registration Successful");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Registration successful! You can now login.");
            successAlert.showAndWait();
            // Quay lại màn hình đăng nhập sau khi đăng ký thành công
            goToLoginScreen(event);
        } else {
            // Hiển thị thông báo lỗi khi đăng ký không thành công
            showAlert(AlertType.ERROR, "Registration Failed", "Username or User ID already exists.");
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
     * Phương thức xử lý khi nhấn nút Login (quay lại màn hình đăng nhập).
     */
    @FXML
    private void handleLogin(ActionEvent event) {
        ((Stage) loginButton.getScene().getWindow()).close();
        goToLoginScreen(event);
    }

    /**
     * Quay lại màn hình đăng nhập.
     */
    private void goToLoginScreen(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginScreenJavaFx.fxml"));
            Parent root = loader.load();

            // Lấy controller của màn hình đăng nhập và thiết lập AccountManager
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

    /**
     * Hiển thị màn hình đăng ký.
     */
    public static void show(Stage stage, AccountManager accountManager) {
        try {
            FXMLLoader loader = new FXMLLoader(RegisterScreen.class.getResource("/RegisterScreen.fxml"));
            Parent root = loader.load();

            // Thiết lập controller và truyền AccountManager
            RegisterScreen controller = loader.getController();
            controller.setAccountManager(accountManager);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Register Screen");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
