package library1;
import java.util.HashMap;
import java.sql.*;
import java.util.List;

public class AccountManager {
    // Phương thức đăng ký người dùng
    public boolean register(String userId, String username, String password, String role) {
        String query = "INSERT INTO Users (userId, username, password, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userId);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setString(4, role);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Registration successful!");
                return true;
            } else {
                System.out.println("Registration failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error during registration: " + e.getMessage());
        }
        return false;
    }

    public User login(String username, String password) {
        String query = "SELECT * FROM Users WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String userId = rs.getString("userId");
                String role = rs.getString("role");
                System.out.println(userId);
                return new User(userId, username, password, role);
            } else {
                System.out.println("Invalid username or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Document> getPopularDocuments(int i) {
        return Library.getPopularDocuments(i);
    }

    public List<Document> getTopRatedDocuments(int i) {
        return Library.getTopRatedDocuments(i);
    }


    public List<Document> suggestDocumentsBasedOnHistory(String userId) {
        return Library.suggestDocumentsBasedOnHistory(userId);
    }

    public List<Document> getAllDocuments() {
        return Library.getAllDocuments();
    }
}
