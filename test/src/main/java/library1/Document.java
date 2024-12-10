package library1;

import java.util.ArrayList;
import java.util.List;

// Lớp trừu tượng Document
abstract public class Document {
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