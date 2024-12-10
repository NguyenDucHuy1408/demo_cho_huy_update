package javaFx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import library1.BookQuery;
import library1.TransactionQuery;

import java.awt.print.Book;
import java.time.LocalDate;
import java.util.Date;

import static javaFx.LoginScreenJavaFx.getLoggedInUserID;

public class UserDashboardController {
    @FXML
    private Button borrowButton;

    @FXML
    private TableView<BookDisplay> libraryTable;

    @FXML
    private TableView<BookDisplay> myBookTable;

    @FXML
    private Button returnButton;

    @FXML
    private TextField searchBar;

    @FXML
    private Button searchButton;

    @FXML
    private Button rateButton;

    private String userID;

    public void setUserID(String userID) {
        this.userID = userID;
    }

    private ObservableList<Book> myBooks;

    private void rLibrary() {
        ObservableList<BookDisplay> data = FXCollections.observableArrayList();
        //data = BookQuery.getAllBooks();
        data = BookQuery.getAllBooksWithRating();
        libraryTable.setItems(data);
    }

    private void initLibraryTable() {
        String[] label = {"bookID", "title", "author", "rating", "available"};
        ObservableList<TableColumn<BookDisplay, ?>> columns = libraryTable.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            columns.get(i).setCellValueFactory(new PropertyValueFactory<>(label[i]));
        }
        rLibrary();
    }

    private void rMyBook() {
        ObservableList<BookDisplay> data = FXCollections.observableArrayList();
        data = TransactionQuery.getUserBooks(userID);
        myBookTable.setItems(data);
    }

    private void initMyBookTable() {
        String[] label = {"bookID", "title", "author", "rating", "borrowDate", "dueDate"};
        ObservableList<TableColumn<BookDisplay, ?>> columns = myBookTable.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            columns.get(i).setCellValueFactory(new PropertyValueFactory<>(label[i]));
        }
        rMyBook();
    }

    private void initButton() {
        searchButton.setOnAction(_ -> {
            String title = searchBar.getText();
            ObservableList<BookDisplay> data = FXCollections.observableArrayList();
            data = BookQuery.getAllBookByName(title);
            libraryTable.setItems(data);
        });

        borrowButton.setOnAction(_ -> {
            if (libraryTable.getSelectionModel().getSelectedItem() == null) {
                return;
            }
            int bookID = libraryTable.getSelectionModel().getSelectedItem().getBookID();
            String title = libraryTable.getSelectionModel().getSelectedItem().getTitle();
            String author = libraryTable.getSelectionModel().getSelectedItem().getAuthor();
            int copy = libraryTable.getSelectionModel().getSelectedItem().getAvailable();

            if (copy == 0) {
                Alert noti = new Alert(Alert.AlertType.WARNING);
                noti.setTitle("Warning");
                noti.setHeaderText(null);
                noti.setContentText("The book \"" + title + "\" is currently unavailable for borrowing.");
                noti.showAndWait();
                return; // Không cho phép mượn sách
            }

            if (TransactionQuery.hasBorrowedBook(userID, bookID)) {
                Alert noti = new Alert(Alert.AlertType.WARNING);
                noti.setTitle("Warning");
                noti.setHeaderText(null);
                noti.setContentText("You have already borrowed the book " + title + ".");
                noti.showAndWait();
                return; // Không cho phép mượn sách thêm lần nữa
            }

            if (TransactionQuery.addBorrow(userID, bookID)) {
                BookQuery.updateBook(bookID, title, author, copy - 1);
                rLibrary();
                rMyBook();
                Alert noti = new Alert(Alert.AlertType.INFORMATION);
                noti.setTitle("Success");
                noti.setHeaderText(null);
                noti.setContentText("You have borrowed the book " + title + ".");
                noti.showAndWait();
            } else {
                Alert noti = new Alert(Alert.AlertType.ERROR);
                noti.setTitle("Error");
                noti.setHeaderText(null);
                noti.setContentText("You have not borrowed the book " + title + ".");
                noti.showAndWait();
            }
        });

        returnButton.setOnAction(_ -> {
            if (myBookTable.getSelectionModel().getSelectedItem() == null) {
                return; // Không làm gì nếu không có sách nào được chọn
            }
            int bookID = myBookTable.getSelectionModel().getSelectedItem().getBookID();
            String title = myBookTable.getSelectionModel().getSelectedItem().getTitle();
            if (TransactionQuery.returnBorrow(userID, bookID)) {
                Alert noti = new Alert(Alert.AlertType.INFORMATION);
                rLibrary();
                rMyBook();
                noti.setTitle("Success");
                noti.setHeaderText(null);
                noti.setContentText("You have returned the book " + title + ".");
                noti.showAndWait();
            } else {
                Alert noti = new Alert(Alert.AlertType.ERROR);
                noti.setTitle("Error");
                noti.setHeaderText(null);
                noti.setContentText("Failed to return the book " + title + ".");
                noti.showAndWait();
            }
        });
    }

    private void checkOverdueBooks() {
        // Lấy danh sách các sách đã mượn của người dùng hiện tại
        ObservableList<BookDisplay> myBooks = TransactionQuery.getUserBooks(userID);

        // Lặp qua từng sách để kiểm tra xem sách có quá hạn không
        for (BookDisplay book : myBooks) {
            // Lấy ngày mượn và ngày hết hạn của sách
            Date borrowDate = book.getBorrowDate();
            Date dueDate = book.getDueDate();

            // Kiểm tra nếu ngày hiện tại sau ngày hết hạn
            LocalDate currentDate = LocalDate.now();  // Ngày hiện tại
            LocalDate dueLocalDate = ((java.sql.Date) dueDate).toLocalDate();  // Chuyển đổi Date sang LocalDate để so sánh

            if (currentDate.isAfter(dueLocalDate)) {  // Nếu ngày hiện tại sau ngày hết hạn
                showOverdueAlert(book.getTitle());  // Hiển thị thông báo nếu quá hạn
            }
        }
    }

    // Hiển thị thông báo nếu sách quá hạn
    private void showOverdueAlert(String bookTitle) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Overdue Book");
        alert.setHeaderText("You have an overdue book!");
        alert.setContentText("The book \"" + bookTitle + "\" is overdue. Please return it as soon as possible.");
        alert.showAndWait();
    }

    // Phương thức khởi tạo, gọi sau khi giao diện được tải
    @FXML
    public void initialize() {
        setUserID(getLoggedInUserID());
        initLibraryTable();
        initMyBookTable();
        initButton();
        checkOverdueBooks();
    }

    // Sự kiện bấm nút "Search"
    @FXML
    private void handleSearchButtonClick() {
        String query = searchBar.getText();
        System.out.println("Searching for: " + query);

        // TODO: Thực hiện logic tìm kiếm trong thư viện
        // Ví dụ: Gọi API hoặc truy vấn database để cập nhật `searchTable`
    }

    // Sự kiện bấm nút "Borrow" (Mượn sách)
    @FXML
    private void handleBorrowButtonClick() {
        System.out.println("Borrow button clicked!");

        // TODO: Thực hiện logic mượn sách
        // Ví dụ: Lấy sách đã chọn trong `libraryTable` và thêm vào cơ sở dữ liệu "sách đã mượn"
    }

    // Sự kiện bấm nút "Return" (Trả sách)
    @FXML
    private void handleReturnButtonClick() {
        System.out.println("Return button clicked!");

        // TODO: Thực hiện logic trả sách
        // Ví dụ: Lấy sách đã chọn trong `myBookTable` và cập nhật trạng thái trả sách trong cơ sở dữ liệu
    }

    // Sự kiện bấm nút "Borrow" trong tab "Search"
    @FXML
    private void handleSBorrowButtonClick() {
        System.out.println("Search Borrow button clicked!");

        // TODO: Thực hiện logic mượn sách từ tab "Search"
        // Ví dụ: Lấy sách đã chọn trong `searchTable` và thêm vào danh sách "sách đã mượn"
    }
}
