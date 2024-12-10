package library1;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;

class Library {
    private final List<Document> documents;
    private final List<User> users;
    private final LibraryHistory history;
    private Map<String, Document> documentMap = new HashMap<>();
    private Map<String, User> userMap = new HashMap<>();

    public Library() {
        this.documents = new ArrayList<>();
        this.users = new ArrayList<>();
        this.history = new LibraryHistory();
        this.documentMap = new HashMap<>();
        this.userMap = new HashMap<>();
    }

    // Quản lý tài liệu

    public void addDocument(Document doc) {
        String query = "INSERT INTO Documents (title, author, quantity) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, doc.getTitle());
            stmt.setString(2, doc.getAuthor());
            stmt.setInt(3, doc.getQuantity());
            stmt.executeUpdate();
            System.out.println("Document added to the database successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeDocument(String title) {
        String query = "DELETE FROM Documents WHERE title = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, title);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Document removed from the database successfully.");
            } else {
                System.out.println("Document not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDocument(String title, String newTitle, String newAuthor, int newQuantity) {
        String query = "UPDATE Documents SET title = ?, author = ?, quantity = ? WHERE title = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newTitle);
            stmt.setString(2, newAuthor);
            stmt.setInt(3, newQuantity);
            stmt.setString(4, title);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Document updated successfully.");
            } else {
                System.out.println("Document not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Document findDocumentByTitle(String title) {
        String query = "SELECT * FROM Documents WHERE title = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String documentTitle = rs.getString("title");
                String author = rs.getString("author");
                int quantity = rs.getInt("quantity");
                return new Book(documentTitle, author, quantity); // Assuming it's a book; modify for other types
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void displayDocuments() {
        String query = "SELECT * FROM Documents"; // Câu lệnh truy vấn tất cả tài liệu từ cơ sở dữ liệu

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("Library Documents:");
            while (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                int quantity = rs.getInt("quantity");
                System.out.println("Title: " + title + ", Author: " + author + ", Quantity: " + quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    // Quản lý người dùng

    public void addUser(User user) {
        users.add(user);
        userMap.put(user.getUserId(), user);
        System.out.println("User added successfully.");
    }

    public User findUserById(String userId) {
        String query = "SELECT * FROM Users WHERE userId = ?";  // Truy vấn theo userId
        try (Connection conn = DBConnection.getConnection();  // Kết nối đến CSDL
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Thiết lập giá trị cho tham số trong câu lệnh SQL
            stmt.setString(1, userId);

            // Thực thi câu lệnh SQL
            ResultSet rs = stmt.executeQuery();

            // Kiểm tra nếu có kết quả
            if (rs.next()) {
                // Lấy thông tin từ ResultSet
                String username = rs.getString("username");
                String password = rs.getString("password");
                String role = rs.getString("role");

                // Tạo và trả về đối tượng User
                return new User(userId, username, password, role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error finding user by ID: " + e.getMessage());
        }

        // Nếu không tìm thấy user, trả về null
        return null;
    }

    public void borrowDocument(String userId, String documentTitle) {
     // Tìm DocumentId từ documentTitle trong cơ sở dữ liệu
     int documentId = -1;
     String getDocumentQuery = "SELECT DocumentId FROM Documents WHERE title = ?";

     try (Connection conn = DBConnection.getConnection();
          PreparedStatement stmt = conn.prepareStatement(getDocumentQuery)) {
         stmt.setString(1, documentTitle);

         try (ResultSet rs = stmt.executeQuery()) {
             if (rs.next()) {
                 documentId = rs.getInt("DocumentId");
             } else {
                 System.out.println("Document not found with the given title.");
                 return; // Kết thúc nếu không tìm thấy tài liệu
             }
         }
     } catch (SQLException e) {
         e.printStackTrace();
         return;
     }

     // Kiểm tra User
     User user = findUserById(userId);
     if (user == null) {
         System.out.println("User not found with the given ID.");
         return;
     }

     // Kiểm tra số lượng tài liệu
     String checkQuantityQuery = "SELECT quantity FROM Documents WHERE DocumentId = ?";
     int quantity = 0;

     try (Connection conn = DBConnection.getConnection();
          PreparedStatement stmt = conn.prepareStatement(checkQuantityQuery)) {
          stmt.setInt(1, documentId);

         try (ResultSet rs = stmt.executeQuery()) {
             if (rs.next()) {
                 quantity = rs.getInt("quantity");
                 if (quantity <= 0) {
                     System.out.println("Document is not available for borrowing.");
                     return;
                 }
             }
         }
     } catch (SQLException e) {
         e.printStackTrace();
         return;
     }
     // Kiểm tra xem người dùng đã mượn tài liệu này chưa (kiểm tra trong bảng Transactions)
     String checkBorrowQuery = "SELECT COUNT(*) FROM Transactions WHERE userId = ? AND documentId = ? AND isReturned = false";
     int count = 0;
     try (Connection conn = DBConnection.getConnection();
          PreparedStatement stmt = conn.prepareStatement(checkBorrowQuery)) {
         stmt.setString(1, userId);
         stmt.setInt(2, documentId);

         try (ResultSet rs = stmt.executeQuery()) {
             if (rs.next()) {
                 count = rs.getInt(1);  // Lấy số lượng giao dịch chưa trả lại
             }
         }
     } catch (SQLException e) {
         e.printStackTrace();
         return;
     }

     if (count > 0) {
         System.out.println("You have already borrowed this document.");
         return;
     }
     // Cập nhật giao dịch và số lượng
     String borrowQuery = "INSERT INTO Transactions (userId, documentId, borrowDate, dueDate, isReturned) VALUES (?, ?, ?, ?, ?)";
     String updateQuantityQuery = "UPDATE Documents SET quantity = quantity - 1, borrowCount = borrowCount + 1 WHERE DocumentId = ?";

     LocalDate borrowDate = LocalDate.now();
     LocalDate dueDate = borrowDate.plusDays(14); // Thời hạn 14 ngày

     try (Connection conn = DBConnection.getConnection();
          PreparedStatement borrowStmt = conn.prepareStatement(borrowQuery);
          PreparedStatement updateStmt = conn.prepareStatement(updateQuantityQuery)) {

         // Thêm giao dịch
         borrowStmt.setString(1, userId);
         borrowStmt.setInt(2, documentId);
         borrowStmt.setDate(3, java.sql.Date.valueOf(borrowDate));
         borrowStmt.setDate(4, java.sql.Date.valueOf(dueDate));
         borrowStmt.setBoolean(5, false);
         borrowStmt.executeUpdate();

         // Cập nhật số lượng tài liệu
         updateStmt.setInt(1, documentId);
         updateStmt.executeUpdate();

         System.out.println("Document borrowed successfully.");
     } catch (SQLException e) {
         e.printStackTrace();
     }
 }

    public void returnDocument(String userId, String documentTitle) {
        // Tìm DocumentId từ documentTitle
        int documentId = -1;
        String getDocumentQuery = "SELECT DocumentId FROM Documents WHERE title = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(getDocumentQuery)) {
            stmt.setString(1, documentTitle);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    documentId = rs.getInt("DocumentId");
                } else {
                    System.out.println("Document not found with the given title.");
                    return;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // Kiểm tra người dùng
        User user = findUserById(userId);
        if (user == null) {
            System.out.println("User not found with the given ID.");
            return;
        }

        // Kiểm tra giao dịch
        String checkTransactionQuery = "SELECT * FROM Transactions WHERE userId = ? AND documentId = ? AND isReturned = false";
        boolean transactionExists = false;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(checkTransactionQuery)) {
            stmt.setString(1, userId);
            stmt.setInt(2, documentId);

            try (ResultSet rs = stmt.executeQuery()) {
                transactionExists = rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        if (!transactionExists) {
            System.out.println("No outstanding transaction found for this document.");
            return;
        }

        // Cập nhật giao dịch và số lượng tài liệu
        String updateTransactionQuery = "UPDATE Transactions SET isReturned = true WHERE userId = ? AND documentId = ? AND isReturned = false";
        String updateQuantityQuery = "UPDATE Documents SET quantity = quantity + 1 WHERE DocumentId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement updateTransactionStmt = conn.prepareStatement(updateTransactionQuery);
             PreparedStatement updateQuantityStmt = conn.prepareStatement(updateQuantityQuery)) {

            // Cập nhật giao dịch
            updateTransactionStmt.setString(1, userId);
            updateTransactionStmt.setInt(2, documentId);
            int rowsAffected = updateTransactionStmt.executeUpdate();

            if (rowsAffected > 0) {
                // Cập nhật số lượng tài liệu
                updateQuantityStmt.setInt(1, documentId);
                updateQuantityStmt.executeUpdate();

                System.out.println("Document returned successfully.");
            } else {
                System.out.println("No outstanding transaction found for this document.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayUserInfo(String userId) {
        User user = findUserById(userId);
        if (user != null) {
            user.printUserInfo();
        } else {
            System.out.println("User not found.");
        }
    }

    public void displayUserHistory(String userId) {
        String query = """
        SELECT t.transactionId, d.title AS documentTitle, t.borrowDate, t.dueDate, t.isReturned
        FROM Transactions t
        JOIN Documents d ON t.documentId = d.DocumentId
        WHERE t.userId = ?
        ORDER BY t.borrowDate DESC
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                boolean found = false;
                System.out.println("Transaction History for User ID: " + userId);

                while (rs.next()) {
                    found = true;
                    int transactionId = rs.getInt("transactionId");
                    String documentTitle = rs.getString("documentTitle");
                    Date borrowDate = rs.getDate("borrowDate");
                    Date dueDate = rs.getDate("dueDate");
                    boolean isReturned = rs.getBoolean("isReturned");

                    System.out.printf("Transaction ID: %d, Document: %s, Borrow Date: %s, Due Date: %s, Status: %s%n",
                            transactionId, documentTitle, borrowDate, dueDate, (isReturned ? "Returned" : "Not Returned"));
                }

                if (!found) {
                    System.out.println("No transactions found for this user.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error displaying user history: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void displayAllHistory() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "00802005")) {
            history.displayAllHistory(conn);  // Truyền kết nối vào phương thức
        } catch (SQLException e) {
            System.out.println("Error displaying all history: " + e.getMessage());
        }
    }

    public void addReview(String userId, String documentTitle, int rating, String comment) {
        String findDocumentQuery = "SELECT DocumentId FROM Documents WHERE title = ?";
        String insertReviewQuery = "INSERT INTO Reviews (userId, documentId, rating, comment) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement findDocStmt = conn.prepareStatement(findDocumentQuery);
             PreparedStatement insertReviewStmt = conn.prepareStatement(insertReviewQuery)) {

            // Tìm documentId từ documentTitle
            findDocStmt.setString(1, documentTitle);
            try (ResultSet rs = findDocStmt.executeQuery()) {
                if (rs.next()) {
                    int documentId = rs.getInt("DocumentId");

                    // Thêm đánh giá vào bảng Reviews
                    insertReviewStmt.setString(1, userId);
                    insertReviewStmt.setInt(2, documentId);
                    insertReviewStmt.setInt(3, rating);
                    insertReviewStmt.setString(4, comment);

                    insertReviewStmt.executeUpdate();
                    System.out.println("Review added successfully.");
                } else {
                    System.out.println("Document not found. Review not added.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error adding review: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void displayReviews(String documentTitle) {
        String findDocumentQuery = "SELECT DocumentId FROM Documents WHERE title = ?";
        String getReviewsQuery = "SELECT r.userId, r.rating, r.comment FROM Reviews r WHERE r.documentId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement findDocStmt = conn.prepareStatement(findDocumentQuery);
             PreparedStatement getReviewsStmt = conn.prepareStatement(getReviewsQuery)) {

            // Tìm documentId từ documentTitle
            findDocStmt.setString(1, documentTitle);
            try (ResultSet docRs = findDocStmt.executeQuery()) {
                if (docRs.next()) {
                    int documentId = docRs.getInt("DocumentId");

                    // Truy vấn đánh giá từ bảng Reviews
                    getReviewsStmt.setInt(1, documentId);
                    try (ResultSet reviewRs = getReviewsStmt.executeQuery()) {
                        if (!reviewRs.next()) {
                            System.out.println("No reviews for this document.");
                            return;
                        }

                        System.out.println("Reviews for " + documentTitle + ":");
                        do {
                            String userId = reviewRs.getString("userId");
                            int rating = reviewRs.getInt("rating");
                            String comment = reviewRs.getString("comment");
                            System.out.printf("User: %s, Rating: %d/5, Comment: %s%n", userId, rating, comment);
                        } while (reviewRs.next());
                    }
                } else {
                    System.out.println("Document not found.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error displaying reviews: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<Document> getPopularDocuments(int limit) {
        String query = "SELECT * FROM Documents ORDER BY borrowCount DESC LIMIT ?";

        List<Document> popularDocs = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                int quantity = rs.getInt("quantity");
                int borrowCount = rs.getInt("borrowCount");
                popularDocs.add(new Book(title, author, quantity));  // Assuming it's a book; you can modify for other document types
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return popularDocs;
    }

    public static List<Document> getTopRatedDocuments(int limit) {
        String query = """
        SELECT d.DocumentId, d.title, d.author, d.quantity, 
               COALESCE(AVG(r.rating), 0) AS averageRating
        FROM Documents d
        LEFT JOIN Reviews r ON d.DocumentId = r.documentId
        GROUP BY d.DocumentId, d.title, d.author, d.quantity
        ORDER BY averageRating DESC
        LIMIT ?""";

        List<Document> topRatedDocs = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int documentId = rs.getInt("DocumentId");
                    String title = rs.getString("title");
                    String author = rs.getString("author");
                    int quantity = rs.getInt("quantity");
                    double averageRating = rs.getDouble("averageRating");

                    // Giả định tài liệu là Book; chỉnh sửa nếu cần để phân biệt loại tài liệu
                    Document document = new Book(title, author, quantity);
                    document.setAverageRating(averageRating); // Đặt điểm trung bình
                    topRatedDocs.add(document);
                }
            }

            if (topRatedDocs.isEmpty()) {
                System.out.println("No top-rated documents found.");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving top-rated documents: " + e.getMessage());
            e.printStackTrace();
        }

        return topRatedDocs;
    }

    public static List<Document> suggestDocumentsBasedOnHistory(String userId) {
        List<Document> suggestedDocs = new ArrayList<>();

        // Cải thiện truy vấn để tìm tài liệu tương tự đã mượn
        String query = "SELECT * FROM Documents WHERE author IN (" +
                "SELECT author FROM Transactions T " +
                "JOIN Documents D ON T.documentId = D.DocumentId " +
                "WHERE T.userId = ? AND T.isReturned = TRUE)";  // Tìm tài liệu mà người dùng đã mượn và trả

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                int quantity = rs.getInt("quantity");
                // Giả sử đây là sách; bạn có thể thay đổi cho các loại tài liệu khác (Magazine, etc.)
                suggestedDocs.add(new Book(title, author, quantity));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return suggestedDocs;
    }

    public static List<Document> getAllDocuments() {
        List<Document> documents = new ArrayList<>();

        // Câu lệnh SQL để lấy tất cả các tài liệu
        String query = "SELECT * FROM Documents";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int documentId = rs.getInt("DocumentId");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int quantity = rs.getInt("quantity");
                int borrowCount = rs.getInt("borrowCount");

                // Tạo đối tượng Book cho mỗi bản ghi trong bảng
                documents.add(new Book(title, author, quantity)); // Book là lớp con của Document
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return documents;
    }
}
