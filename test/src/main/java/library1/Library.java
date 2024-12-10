package library1;
import java.util.ArrayList;
import java.util.List;
/**
// Lớp trừu tượng Document
abstract class Document {
    protected int documentId; // documentId thay vì title
    protected String title;
    protected String author;
    protected int quantity;
    private final List<Review> reviews;
    private int borrowCount = 0;

    public Document(String title, String author, int quantity) {
        this.title = title;
        this.author = author;
        this.quantity = quantity;
        setQuantity(quantity);
        this.reviews = new ArrayList<>();
        this.borrowCount = 0;

    }

    public int getDocumentId() {
        return documentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        this.quantity = quantity;
    }

    public double getAverageRating() {
        if (reviews.isEmpty()) {
            System.out.println("No reviews available.");
            return 0.0; // Nếu chưa có đánh giá
        }
        int totalRating = 0;
        for (Review review : reviews) {
            totalRating += review.getRating();
        }
        return (double) totalRating / reviews.size();
    }

    public void setAverageRating(double averageRating) {
        // Implement nếu cần lưu điểm trung bình trong đối tượng
    }

    public abstract void printInfo();

    @Override
    public String toString() {
        return "Title: " + title + ", Author: " + author + ", Quantity: " + quantity + ", Borrow Count: " + borrowCount;
    }

}
*/
// Các lớp con của Document (ví dụ: Book, Magazine)
class Book extends Document {
    public Book(String title, String author, int quantity) {
        super(title, author, quantity);
    }

    @Override
    public String toString() {
        return "Book: " + title + " by " + author + ", Quantity: " + quantity;
    }

    @Override
    public void printInfo() {
        System.out.println("Book: " + title + " by " + author + ", Quantity: " + quantity);
    }
}

class Magazine extends Document {
    public Magazine(String title, String author, int quantity) {
        super(title, author, quantity);
    }

    @Override
    public void printInfo() {

        System.out.println("Magazine: " + title + " by " + author + ", Quantity: " + quantity);
    }

    @Override
    public String toString() {
        return "Magazine: " + title + " by " + author + ", Quantity: " + quantity;
    }
}

// Lớp User
/**
class User {
    private final String name;
    private final String userId;
    private final String password;
    private final String role;
    private final List<Document> borrowedDocuments;

    public User(String userId, String name, String password, String role) {
        this.password = password;
        this.name = name;
        this.userId = userId;
        this.role = role;
        this.borrowedDocuments = new ArrayList<>();
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }

    public List<Document> getBorrowedDocuments() {
        return borrowedDocuments;
    }

    public boolean canBorrowDocument(Document document) {
        return document.getQuantity() > 0;
    }

    public void borrowDocument(Document document) {
        if (canBorrowDocument(document)) {
            borrowedDocuments.add(document);
            document.setQuantity(document.getQuantity());  // Giảm số lượng tài liệu
            System.out.println("Document borrowed successfully.");
        } else {
            System.out.println("Document is not available for borrowing.");
        }
    }

    public void returnDocument(Document document) {
        borrowedDocuments.remove(document);
        document.setQuantity(document.getQuantity());  // Tăng lại số lượng tài liệu khi trả
    }

    public void printUserInfo() {
        System.out.println("User: " + name + " (ID: " + userId + ")");
        System.out.println("Borrowed Documents: ");
        for (Document doc : borrowedDocuments) {
            doc.printInfo();
        }
    }
}
 */
