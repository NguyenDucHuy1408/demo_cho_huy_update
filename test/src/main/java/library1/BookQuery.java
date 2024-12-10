package library1;

import javaFx.BookDisplay;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class BookQuery {
    public static ObservableList<BookDisplay> getAllBooks() {
        ObservableList<BookDisplay> result = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Documents";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(new BookDisplay(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        5,
                        rs.getInt(4)
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error while getting books info: " + e.getMessage());
        }
        return result;
    }

    public static ObservableList<BookDisplay> getAllBookByName(String name) {

        ObservableList<BookDisplay> result = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Documents WHERE title LIKE ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(sql)) {
            ResultSet rs = null;
            ps.setString(1, "%" + name + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                result.add(new BookDisplay(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        5,
                        rs.getInt(5)
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error while getting books info: " + e.getMessage());
        }
        return result;
    }

    public static BookDisplay getBook(int id) {
        String sql = "SELECT * FROM Documents WHERE DocumentId = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new BookDisplay(
                            rs.getInt("DocumentId"),
                            rs.getString("title"),
                            rs.getString("author"),
                            5,
                            rs.getInt("quantity")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while fetching book: " + e.getMessage());
        }
        return null;
    }

    public static boolean removeBook(int id) {
        String sql = "DELETE FROM Documents WHERE DocumentId = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error while removing book: " + e.getMessage());
        }
        return false;
    }

    public static boolean addBook(String title, String author, int copy) {
        String sql = "INSERT INTO Documents (title, author, quantity, borrowCount) VALUES (?, ?, ?, 0)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setInt(3, copy);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static boolean updateBook(int id, String title, String author, int quantity) {
        String sql = "UPDATE Documents SET title = ?, author = ?, quantity = ? WHERE DocumentId = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setInt(3, quantity);
            stmt.setInt(4, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static ObservableList<BookDisplay> getAllBooksWithRating() {
        ObservableList<BookDisplay> books = FXCollections.observableArrayList();
        String query = """
        SELECT 
            d.DocumentId AS bookID,
            d.title,
            d.author,
            d.quantity AS available,
            ROUND(IFNULL(AVG(r.rating), 0),1) AS averageRating
        FROM Documents d
        LEFT JOIN Reviews r ON d.DocumentId = r.documentId
        GROUP BY d.DocumentId, d.title, d.author, d.quantity
        ORDER BY d.DocumentId;
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int bookID = rs.getInt("bookID");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int available = rs.getInt("available");
                double averageRating = rs.getDouble("averageRating");

                books.add(new BookDisplay(bookID, title, author, averageRating, available));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

}
