package library1;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LibraryHistory {
    public List<Transaction> transactions;

    public LibraryHistory() {
        this.transactions = new ArrayList<>();
    }

    // Hiển thị lịch sử giao dịch của người dùng
    public void displayUserHistory(Connection conn, String userId) throws SQLException {
        boolean found = false;
        String query = "SELECT * FROM Transactions WHERE userId = ? ORDER BY borrowDate DESC";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, userId);

        ResultSet rs = ((java.sql.PreparedStatement) stmt).executeQuery();

        System.out.println("Transaction History for User ID: " + userId);
        while (rs.next()) {
            String documentTitle = rs.getString("documentTitle");
            Date borrowDate = rs.getDate("borrowDate");
            Date dueDate = rs.getDate("dueDate");
            boolean isReturned = rs.getBoolean("isReturned");
            System.out.printf("Document: %s, Borrow Date: %s, Due Date: %s, Status: %s%n",
                    documentTitle, borrowDate, dueDate, (isReturned ? "Returned" : "Not Returned"));
            found = true;
        }

        if (!found) {
            System.out.println("No transactions found for this user.");
        }
    }

    // Hiển thị tất cả lịch sử giao dịch
    public void displayAllHistory(Connection conn) throws SQLException {
        String query = """
        SELECT t.userId, d.title, t.borrowDate, t.dueDate, t.isReturned
        FROM Transactions t
        JOIN Documents d ON t.documentId = d.DocumentId
        ORDER BY t.borrowDate DESC
        """;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (!rs.next()) {
                System.out.println("No transactions available.");
                return;
            }

            System.out.println("All Transaction History:");
            do {
                String userId = rs.getString("userId");
                String title = rs.getString("title"); // Lấy tiêu đề từ bảng Documents
                Date borrowDate = rs.getDate("borrowDate");
                Date dueDate = rs.getDate("dueDate");
                boolean isReturned = rs.getBoolean("isReturned");
                System.out.printf("User: %s, Document: %s, Borrow Date: %s, Due Date: %s, Status: %s%n",
                        userId, title, borrowDate, dueDate, (isReturned ? "Returned" : "Not Returned"));
            } while (rs.next());
        }
    }

}
