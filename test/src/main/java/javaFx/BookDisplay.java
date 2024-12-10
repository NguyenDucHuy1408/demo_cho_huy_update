package javaFx;

import java.sql.Date;

public class BookDisplay {
    private int bookID;
    private String title;
    private String author;
    private double rating;
    private int available;
    private Date borrowDate;  // Ngày mượn
    private Date dueDate;     // Ngày trả

    // Constructor cho BookDisplay (khi không có ngày mượn và ngày trả)
    public BookDisplay(int bookID, String title, String author, double rating, int available) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.rating = rating;
        this.available = available;
    }

    // Constructor cho BookDisplay (khi có ngày mượn và ngày trả)
    public BookDisplay(int bookID, String title, String author, double rating, Date borrowDate, Date dueDate) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.rating = rating;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }

    // Getters và Setters
    public int getBookID() {
        return bookID;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getAvailable() {
        return available;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
