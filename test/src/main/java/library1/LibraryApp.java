package library1;
import java.util.Scanner;
import java.util.List;

public class LibraryApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AccountManager accountManager = new AccountManager();
        Library library = new Library();
        User currentUser  = null;




        while (true) {
            showMainMenu();
            int choice = getValidChoice(scanner);

            switch (choice) {
                case 1: // Đăng ký
                    register(scanner, accountManager, library);
                    break;
                case 2: // Đăng nhập
                    currentUser = login(scanner, accountManager);
                    if (currentUser != null) {
                        runLibrarySystem(scanner, library, currentUser);
                    }
                    break;
                case 3: // Thoát
                    System.out.println("Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void showMainMenu() {
        System.out.println("Welcome to Library Application!");
        System.out.println("[1] Register");
        System.out.println("[2] Login");
        System.out.println("[3] Exit");
        System.out.print("Choose an option: ");
    }

    private static int getValidChoice(Scanner scanner) {
        int choice = -1;
        while (choice < 0 || choice > 3) {
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid option.");
            }
        }
        return choice;
    }

    private static int getValidChoice_case(Scanner scanner) {
        int choice = -1;
        while (choice < 0 || choice > 19) {
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid option.");
            }
        }
        return choice;
    }

    private static void register(Scanner scanner, AccountManager accountManager, Library library) {
        System.out.print("Enter username: ");
        String regUsername = scanner.nextLine();
        System.out.print("Enter password: ");
        String regPassword = scanner.nextLine();
        System.out.print("Enter userId: ");
        String regUserId = scanner.nextLine();
        System.out.print("Enter role (Admin/Member): ");
        String regRole = scanner.nextLine();
        // Đăng ký tài khoản trong AccountManager
        boolean registered = accountManager.register(regUserId, regUsername, regPassword, regRole);

        if (registered) {
            // Nếu đăng ký thành công, thêm người dùng vào Library
            User newUser = new User(regUserId, regUsername, regPassword, regRole);
            library.addUser(newUser);  // Gọi addUser từ Library
            System.out.println("User added to library system successfully.");
        }
        //accountManager.register(regUsername, regUserId, regPassword, regRole);
    }

    private static User login(Scanner scanner, AccountManager accountManager) {
        System.out.print("Enter username: ");
        String loginUsername = scanner.nextLine();
        System.out.print("Enter password: ");
        String loginPassword = scanner.nextLine();
        return accountManager.login(loginUsername, loginPassword);
    }

    public static void runLibrarySystem(Scanner scanner, Library library, User currentUser) {
        int choice = -1;
        while (choice != 0) {
            displayUserMenu(currentUser);
            choice = getValidChoice_case(scanner);

            switch (choice) {
                // Admin Actions
                case 1: // Admin - Add Document
                    if (isAdmin(currentUser)) {
                        addDocument(scanner, library);
                    } else {
                        System.out.println("Access Denied: Admin only.");
                    }
                    break;
                case 2: // Admin - Remove Document
                    if (isAdmin(currentUser)) {
                        removeDocument(scanner, library);
                    } else {
                        System.out.println("Access Denied: Admin only.");
                    }
                    break;
                case 3: // Admin - Update Document
                    if (isAdmin(currentUser)) {
                        updateDocument(scanner, library);
                    } else {
                        System.out.println("Access Denied: Admin only.");
                    }
                    break;
                case 4: // Find Document
                    findDocument(scanner, library);
                    break;
                case 5: // Display Documents
                    library.displayDocuments();
                    break;
                case 8: // Admin - Display User Info
                    if (isAdmin(currentUser)) {
                        displayUserInfo(scanner, library);
                    } else {
                        System.out.println("Access Denied: Admin only.");
                    }
                    break;
                case 10: // Admin - View All History
                    if (isAdmin(currentUser)) {
                        library.displayAllHistory();
                    } else {
                        System.out.println("Access Denied: Admin only.");
                    }
                    break;

                // Member Actions
                case 6: // Member - Borrow Document
                    if (isMember(currentUser)) {
                        borrowDocument(scanner, library, currentUser);
                    } else {
                        System.out.println("Access Denied: Member only.");
                    }
                    break;
                case 7: // Member - Return Document
                    if (isMember(currentUser)) {
                        returnDocument(scanner, library, currentUser);
                    } else {
                        System.out.println("Access Denied: Member only.");
                    }
                    break;
                case 9: // Member - View User History
                    if (isMember(currentUser)) {
                        library.displayUserHistory(currentUser.getUserId());
                    } else {
                        System.out.println("Access Denied: Member only.");
                    }
                    break;
                case 11: // Member - Add Review
                    if (isMember(currentUser)) {
                        addReview(scanner, library, currentUser);
                    } else {
                        System.out.println("Access Denied: Member only.");
                    }
                    break;
                case 12: // Member - View Reviews
                    if (isMember(currentUser)) {
                        viewReviews(scanner, library);
                    } else {
                        System.out.println("Access Denied: Member only.");
                    }
                    break;

                // Additional actions
                case 17: // Get Popular Document Suggestions
                    getPopularDocuments(library);
                    break;
                case 18: // Get Top Rated Document Suggestions
                    getTopRatedDocuments(library);
                    break;
                case 19: // Suggest Documents Based on History
                    suggestDocumentsBasedOnHistory(scanner, library, currentUser);
                    break;

                case 0: // Exit / Logout
                    System.out.println("Logged out.");
                    return;

                default:
                    System.out.println("Action is not supported.");
                    break;
            }
        }
    }

    private static void displayUserMenu(User currentUser) {
        System.out.println("\nWelcome to My Application!");
        System.out.println("[0] Exit");

        // Admin Actions
        if ("Admin".equals(currentUser.getRole())) {
            System.out.println("[1] Add Document");
            System.out.println("[2] Remove Document");
            System.out.println("[3] Update Document");
            System.out.println("[4] Find Document");
            System.out.println("[5] Display Documents");
            System.out.println("[8] Display User Info");
            System.out.println("[10] View All History");
        }

        // Member Actions
        if ("Member".equals(currentUser.getRole())) {
            System.out.println("[6] Borrow Document");
            System.out.println("[7] Return Document");
            System.out.println("[5] Display Documents");
            System.out.println("[9] View User History");
            System.out.println("[11] Add Review");
            System.out.println("[12] View Reviews");
            System.out.println("[17] Get Popular Document Suggestions");
            System.out.println("[18] Get Top Rated Document Suggestions");
            System.out.println("[19] Get Top Rated User Sug     gestions");
        }
    }
    private static boolean isAdmin(User user) {
        return "Admin".equals(user.getRole());
    }

    private static boolean isMember(User user) {
        return "Member".equals(user.getRole());
    }

    // Additional methods for borrowing, returning, etc.
    private static void addDocument(Scanner scanner, Library library) {
        System.out.println("Enter Document Type (Book/Magazine): ");
        String type = scanner.nextLine();
        System.out.println("Enter Title: ");
        String title = scanner.nextLine();
        System.out.println("Enter Author: ");
        String author = scanner.nextLine();
        System.out.println("Enter Quantity: ");
        int quantity = Integer.parseInt(scanner.nextLine());

        if (type.equalsIgnoreCase("Book")) {
            library.addDocument(new Book(title, author, quantity));
        } else if (type.equalsIgnoreCase("Magazine")) {
            library.addDocument(new Magazine(title, author, quantity));
        } else {
            System.out.println("Invalid document type.");
        }
    }

    private static void removeDocument(Scanner scanner, Library library) {
        System.out.println("Enter Document Title to Remove: ");
        String removeTitle = scanner.nextLine();
        library.removeDocument(removeTitle);
    }

    private static void updateDocument(Scanner scanner, Library library) {
        System.out.println("Enter Document Title to Update: ");
        String updateTitle = scanner.nextLine();
        System.out.println("Enter New Title: ");
        String newTitle = scanner.nextLine();
        System.out.println("Enter New Author: ");
        String newAuthor = scanner.nextLine();
        System.out.println("Enter New Quantity: ");
        int newQuantity = Integer.parseInt(scanner.nextLine());
        library.updateDocument(updateTitle, newTitle, newAuthor, newQuantity);
    }

    private static void findDocument(Scanner scanner, Library library) {
        System.out.println("Enter Document Title to Find: ");
        String findTitle = scanner.nextLine();
        Document foundDoc = library.findDocumentByTitle(findTitle);
        if (foundDoc != null) {
            foundDoc.printInfo();
        } else {
            System.out.println("Document not found.");
        }
    }

    private static void borrowDocument(Scanner scanner, Library library, User currentUser) {
        //System.out.println("Enter User ID: ");
        //String borrowUserId = scanner.nextLine();
        System.out.println("Enter Document Title to Borrow: ");
        System.out.println(currentUser.getUserId() + " " + currentUser.getName() + " " + currentUser.getPassword() + " " + currentUser.getRole());
        String borrowTitle = scanner.nextLine();
        library.borrowDocument(currentUser.getUserId(), borrowTitle);
    }

    private static void returnDocument(Scanner scanner, Library library, User currentUser) {
        //System.out.println("Enter User ID: ");
        //String returnUserId = scanner.nextLine();
        System.out.println("Enter Document Title to Return: ");
        String returnTitle = scanner.nextLine();
        library.returnDocument(currentUser.getUserId(), returnTitle);
    }

    private static void displayUserInfo(Scanner scanner, Library library) {
        System.out.println("Enter User ID to display info:");
        String userId = scanner.nextLine();
        library.displayUserInfo(userId);  // Call the method to display user info
    }

    private static void addReview(Scanner scanner, Library library, User currentUser) {
        System.out.println("Enter Document Title to Review: ");
        String reviewTitle = scanner.nextLine();
        System.out.println("Enter Rating (1-5): ");
        int rating = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter Comment: ");
        String comment = scanner.nextLine();
        library.addReview(currentUser.getUserId(), reviewTitle, rating, comment);
    }

    private static void viewReviews(Scanner scanner, Library library) {
        System.out.println("Enter Document Title to View Reviews: ");
        String viewTitle = scanner.nextLine();
        library.displayReviews(viewTitle);
    }

    private static void getPopularDocuments(Library library) {
        System.out.println("Co chay");
        List<Document> popularDocs = library.getPopularDocuments(5); // Gợi ý 5 tài liệu phổ biến nhất
        popularDocs.forEach(Document::printInfo);
    }

    private static void getTopRatedDocuments(Library library) {
        System.out.println("Co chay");
        List<Document> topRatedDocs = library.getTopRatedDocuments(5); // Gợi ý 5 tài liệu được đánh giá cao nhất
        topRatedDocs.forEach(Document::printInfo);
    }

    private static void suggestDocumentsBasedOnHistory(Scanner scanner, Library library, User currentUser) {
        System.out.println("Co chay");
        System.out.println("Document suggestions based on your borrowing history:");
        List<Document> historyBasedDocs = library.suggestDocumentsBasedOnHistory(currentUser.getUserId());
        historyBasedDocs.forEach(Document::printInfo);
    }
}

