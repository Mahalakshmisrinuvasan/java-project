import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public static Connection getConnection() {
        Connection conn = null;
        try {
            String url = "jdbc:mysql://localhost:3306/chat_app";
            String user = "root"; // Replace with your MySQL username
            String password = "Maha@101004";  // Replace with your MySQL password
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Database connected: " + (conn != null ? "Success" : "Failed"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
