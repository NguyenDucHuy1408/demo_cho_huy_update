package library1;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/library"; // Địa chỉ và tên cơ sở dữ liệu
    private static final String USER = "root"; // Tên người dùng MySQL
    private static final String PASSWORD = "00802005"; // Mật khẩu MySQL của bạn

    // Lấy kết nối tới cơ sở dữ liệu
    public static Connection getConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Error while connecting to MySQL: " + e.getMessage());
        }
        return null;
    }
}
