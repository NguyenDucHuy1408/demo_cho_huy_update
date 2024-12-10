package library1;
import java.time.LocalDate;

public class Transaction {
    private final String userId;
    private final String documentTitle;
    private final LocalDate borrowDate;
    private final LocalDate dueDate;
    private boolean isReturned;

    public Transaction(String userId, String documentTitle, LocalDate borrowDate, LocalDate dueDate) {
        if (dueDate.isBefore(borrowDate)) {
            throw new IllegalArgumentException("Due date cannot be before borrow date.");
        }
        this.userId = userId;
        this.documentTitle = documentTitle;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.isReturned = false;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public void setReturned(boolean returned) {
        isReturned = returned;
    }

    /*
    public void printTransaction() {
        System.out.println("User ID: " + userId +
                ", Document: " + documentTitle +
                ", Borrow Date: " + borrowDate +
                ", Due Date: " + dueDate +
                ", Status: " + (isReturned ? "Returned" : "Not Returned"));
    }
    */

    public void printTransaction() {
        String status = isReturned ? "Returned" : "Not Returned";
        System.out.printf("User ID: %s, Document: %s, Borrow Date: %s, Due Date: %s, Status: %s%n",
                userId, documentTitle, borrowDate, dueDate, status);
    }
}
