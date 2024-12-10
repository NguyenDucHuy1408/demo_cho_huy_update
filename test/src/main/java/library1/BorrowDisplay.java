package library1;

public class BorrowDisplay {
    private int id;
    private String userID;
    private int bookID;
    private String borrowDate;
    private String returnDate;
    private int returned = 0;

    public BorrowDisplay(int id, String userID, int bookID, String borrowDate, String returnDate, int returned) {
        this.id = id;
        this.userID = userID;
        this.bookID = bookID;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.returned = returned;
    }

    public int getId() {
        return id;
    }

    public String getUserID() {
        return userID;
    }

    public int getBookID() {
        return bookID;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public String getReturned() {
        return returned == 1 ? "Yes" : "No";
    }
}

