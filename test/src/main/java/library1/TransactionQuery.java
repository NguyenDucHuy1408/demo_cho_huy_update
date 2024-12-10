package library1;

import javaFx.BookDisplay;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.util.Objects;

public class TransactionQuery {

    // Thêm sách mới mượn vào bảng transactions
    
    public static boolean addBorrow(String userID, int bookID) {
        System.out.println(userID + " " + bookID);
        String sql = "INSERT INTO Transactions (userId, documentId, borrowDate, dueDate, isReturned) VALUES (?, ?, NOW(), DATE_ADD(NOW(), INTERVAL 14 DAY), 0)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(sql)) {
            ps.setString(1, userID);
            ps.setInt(2, bookID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error while adding borrow transaction: " + e.getMessage());
            return false;
        }
    }


    // Lấy danh sách sách đã mượn bởi người dùng

    public static ObservableList<BookDisplay> getUserBooks(String userID) {
        ObservableList<BookDisplay> result = FXCollections.observableArrayList();
        String sql = """
        SELECT t.DocumentId, d.title, d.author, t.borrowDate, t.dueDate
        FROM Transactions t
        JOIN Documents d ON t.documentId = d.documentId
        WHERE t.userId = ? AND t.isReturned = 0
        """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(sql)) {
            ps.setString(1, userID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int bookID = rs.getInt("documentId");
                String title = rs.getString("title");
                String author = rs.getString("author");
                Date borrowDate = rs.getDate("borrowDate");
                Date dueDate = rs.getDate("dueDate");

                // Tính điểm rating trung bình của cuốn sách
                double averageRating = getAverageRating(bookID);

                // Thêm sách vào danh sách kết quả với rating trung bình và ngày mượn, ngày trả
                result.add(new BookDisplay(bookID, title, author, averageRating, borrowDate, dueDate));
            }
        } catch (SQLException e) {
            System.out.println("Error while getting user books: " + e.getMessage());
        }
        return result;
    }






    public static ObservableList<BorrowDisplay> getAllBorrow() {
        ObservableList<BorrowDisplay> result = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Transactions";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                result.add(new BorrowDisplay(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6)
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error while getting all borrow transactions: " + e.getMessage());
        }
        return result;
    }

    public static boolean returnBorrow(String userID, int bookID) {
        String sql = "UPDATE Transactions SET isReturned = 1 WHERE userId = ? AND documentId = ? AND isReturned = 0";
        BookDisplay temp = BookQuery.getBook(bookID);
        if (BookQuery.updateBook(bookID, temp.getTitle(), temp.getAuthor(), temp.getAvailable() + 1)) {
            System.out.println("YES");
        } else {
            System.out.println("NO");
        }
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(sql)) {
            ps.setString(1, userID);
            ps.setInt(2, bookID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error while returning borrow transaction: " + e.getMessage());
            return false;
        }
    }

    public static boolean hasBorrowedBook(String userID, int bookID) {
        String sql = "SELECT COUNT(*) FROM Transactions WHERE userId = ? AND documentId = ? AND isReturned = 0";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(sql)) {
            ps.setString(1, userID);
            ps.setInt(2, bookID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Trả về true nếu tồn tại bản ghi chưa trả
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking borrowed book: " + e.getMessage());
        }
        return false; // Không mượn hoặc có lỗi
    }

    public static double getAverageRating(int bookID) {
        double averageRating = 0;
        String sql = """
            SELECT AVG(rating) AS average_rating
            FROM reviews
            WHERE documentId = ?
            """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(sql)) {
            ps.setInt(1, bookID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                averageRating = rs.getDouble("average_rating");
            }
        } catch (SQLException e) {
            System.out.println("Error while calculating average rating: " + e.getMessage());
        }
        System.out.println(averageRating);
        return averageRating;
    }



}

