import java.sql.*;
import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server started...");

            // Database connection details
            String url = "jdbc:mysql://localhost:3306/Chatapp"; // Change database name if needed
            String user = "root"; // Your MySQL username
            String password = "Maha@101004"; // Your MySQL password

            // Establish a connection to the database
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Database connected.");

            // Enable auto-commit
            conn.setAutoCommit(true);

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("New client connected: " + clientSocket.getInetAddress());
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                    String message;
                    while ((message = in.readLine()) != null) {
                        System.out.println("Message received from client: " + message);

                        // Insert message into the database
                        String sql = "INSERT INTO chat_messages (message) VALUES (?)";
                        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                            pstmt.setString(1, message); // Set the message
                            pstmt.executeUpdate(); // Execute the insert query
                            System.out.println("Message inserted into database.");
                        } catch (SQLException e) {
                            System.out.println("Error inserting message into database.");
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
