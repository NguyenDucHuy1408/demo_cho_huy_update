package library1;

import java.util.ArrayList;
import java.util.List;

public class User {
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
