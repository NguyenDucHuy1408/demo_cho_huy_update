package javaFx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import library1.*;

public class AdminDashboardController {

    @FXML
    private Button addBook;

    @FXML
    private Button addBorrow;

    @FXML
    private Button addUser;

    @FXML
    private TableView<BookDisplay> bookTable;

    @FXML
    private TableView<BorrowDisplay> borrowTable;

    @FXML
    private Button editBook;

    @FXML
    private Button editUser;

    @FXML
    private Button removeBook;

    @FXML
    private Button removeUser;

    @FXML
    private Button borrowRefresh;

    @FXML
    private TableView<UserDisplay> userTable;

    @FXML
    private StackPane addBookPane;

    @FXML
    private TextField addTitle;

    @FXML
    private TextField addAuthor;

    @FXML
    private TextField addCopy;

    @FXML
    private Button submitAdd;

    @FXML
    private StackPane addUserPane;

    @FXML
    private TextField addUsername;

    @FXML
    private TextField addPassword;

    @FXML
    private TextField addRole;

    @FXML
    private TextField addID;

    @FXML
    private Button submitUser;

    private void refreshUserData() {
        ObservableList<UserDisplay> data = FXCollections.observableArrayList();
        data = UserQuery.getAllUserInfo();
        userTable.setItems(data);
    }

    private void initUserTable() {
        refreshBookTable();
        String[] label = {"userID", "username", "password", "role"};
        ObservableList<TableColumn<UserDisplay, ?>> columns = userTable.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            columns.get(i).setCellValueFactory(new PropertyValueFactory<>(label[i]));
        }
        refreshUserData();
    }

    private void refreshBorrowTable() {
        ObservableList<BorrowDisplay> data = FXCollections.observableArrayList();
        data = TransactionQuery.getAllBorrow();
        borrowTable.setItems(data);
    }

    private void initBorrowTable() {
        String[] label = {"id", "userID", "bookID", "borrowDate", "returnDate", "returned"};
        ObservableList<TableColumn<BorrowDisplay, ?>> columns = borrowTable.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            columns.get(i).setCellValueFactory(new PropertyValueFactory<>(label[i]));
        }
        refreshBorrowTable();
    }

    private void refreshBookTable() {
        //ObservableList<BookDisplay> data = FXCollections.observableArrayList();
        ObservableList<BookDisplay> data = BookQuery.getAllBooksWithRating();
        //data = BookQuery.getAllBooks();
        data = BookQuery.getAllBooksWithRating();
        bookTable.setItems(data);
    }

    private void initBookTable() {
        String[] label = {"bookID", "title", "author", "rating", "available"};
        ObservableList<TableColumn<BookDisplay, ?>> columns = bookTable.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            columns.get(i).setCellValueFactory(new PropertyValueFactory<>(label[i]));
        }
        refreshBookTable();
    }

    private void initBookSection() {
        addBook.setOnAction(_ -> {
            addTitle.clear();
            addAuthor.clear();
            addCopy.clear();
            addBookPane.setVisible(true);
            submitAdd.setOnAction(_ -> {
                String title = addTitle.getText();
                String author = addAuthor.getText();
                String available = addCopy.getText();
                if (title == null || author == null || available == null || title == "" || author == "" || available == "" || !available.matches("\\d*")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Please enter all the fields correctly.");
                    alert.showAndWait();
                } else {
                    int copy = Integer.parseInt(available);
                    if (BookQuery.addBook(title, author, copy)) {
                        refreshBookTable();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText(null);
                        alert.setContentText("Book added successfully.");
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Book already exists.");
                        alert.showAndWait();
                    }
                    addBookPane.setVisible(false);
                }
            });
        });
        removeBook.setOnAction(_ -> {
            if (bookTable.getSelectionModel().getSelectedItem() == null) {
                return;
            }
            int bookID = bookTable.getSelectionModel().getSelectedItem().getBookID();
            if (BookQuery.removeBook(bookID)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                refreshBookTable();
                alert.setTitle("Book Removed");
                alert.setHeaderText(null);
                alert.setContentText("Book Removed");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error removing book");
                alert.setHeaderText(null);
                alert.setContentText("Please try again");
                alert.showAndWait();
            }
        });
        editBook.setOnAction(_ -> {
            if (bookTable.getSelectionModel().getSelectedItem() == null) {
                return;
            }
            addTitle.clear();
            addAuthor.clear();
            addCopy.clear();
            addBookPane.setVisible(true);
            int bookID = bookTable.getSelectionModel().getSelectedItem().getBookID();
            submitAdd.setOnAction(_ -> {
                String title = addTitle.getText();
                String author = addAuthor.getText();
                String available = addCopy.getText();
                if (title == null || author == null || available == null || title == "" || author == "" || available == "" || !available.matches("\\d*")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Please enter all the fields correctly.");
                    alert.showAndWait();
                } else {
                    int copy = Integer.parseInt(available);
                    if (BookQuery.updateBook(bookID, title, author, copy)) {
                        refreshBookTable();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText(null);
                        alert.setContentText("Book edited successfully.");
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Can't edit.");
                        alert.showAndWait();
                    }
                    addBookPane.setVisible(false);
                }
            });
        });
    }

    private void initUserSection() {
        addUser.setOnAction(_ -> {
            addUserPane.setVisible(true);
            addID.clear();
            addUsername.clear();
            addPassword.clear();
            addRole.clear();
            submitUser.setOnAction(_ -> {
                String id = addID.getText();
                String username = addUsername.getText();
                String password = addPassword.getText();
                String role = addRole.getText();
                if (!"Admin".equalsIgnoreCase(role) && !"Member".equalsIgnoreCase(role)) {
                    // Hiển thị thông báo lỗi nếu role không hợp lệ
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Role");
                    alert.setHeaderText(null);
                    alert.setContentText("Role must be either 'Admin' or 'Member'. Please try again.");
                    alert.showAndWait();
                } else {
                    if (id == null || username == null || password == null || role == null || id == "" || username == "" || password == "" || role == "") {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Please enter all the fields correctly.");
                        alert.showAndWait();
                    } else {
                        if (UserQuery.addUser(id, username, password, role)) {
                            refreshUserData();
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Success");
                            alert.setHeaderText(null);
                            alert.setContentText("User added successfully.");
                            alert.showAndWait();
                            addUserPane.setVisible(false);
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText(null);
                            alert.setContentText("User already exists.");
                            alert.showAndWait();
                        }
                    }
                }
            });
        });
        editUser.setOnAction(_ -> {
            addUserPane.setVisible(true);
            addID.setVisible(false);
            addUsername.clear();
            addPassword.clear();
            addRole.clear();
            String id = userTable.getSelectionModel().getSelectedItem().getUserID();
            submitUser.setOnAction(_ -> {
//                String id = addID.getText();
                String username = addUsername.getText();
                String password = addPassword.getText();
                String role = addRole.getText();
                if (!"Admin".equalsIgnoreCase(role) && !"Member".equalsIgnoreCase(role)) {
                    // Hiển thị thông báo lỗi nếu role không hợp lệ
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Role");
                    alert.setHeaderText(null);
                    alert.setContentText("Role must be either 'Admin' or 'Member'. Please try again.");
                    alert.showAndWait();
                } else {
                    if (id == null || username == null || password == null || role == null || id == "" || username == "" || password == "" || role == "") {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Please enter all the fields correctly.");
                        alert.showAndWait();
                    } else {
                        if (UserQuery.editUser(id, username, password, role)) {
                            refreshUserData();
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Success");
                            alert.setHeaderText(null);
                            alert.setContentText("User edited successfully.");
                            alert.showAndWait();
                            addUserPane.setVisible(false);
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Can't edit.");
                            alert.showAndWait();
                        }
                    }
                }
            });

        });
        removeUser.setOnAction(_ -> {
            if (userTable.getSelectionModel().getSelectedItem() == null) {
                return;
            }
            String userID = userTable.getSelectionModel().getSelectedItem().getUserID();
            if (UserQuery.removeUser(userID)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                refreshUserData();
                alert.setTitle("User Removed");
                alert.setHeaderText(null);
                alert.setContentText("User Removed");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error removing user");
                alert.setHeaderText(null);
                alert.setContentText("Please try again");
                alert.showAndWait();
            }
        });
    }

    private void initBorrowSection() {
        borrowRefresh.setOnAction(_ -> refreshBorrowTable());
    }

    @FXML
    private void initialize() {
        initBookSection();
        initUserSection();
        initUserTable();
        initBorrowTable();
        initBookTable();
        initBorrowSection();
    }

}
