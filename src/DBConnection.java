import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DBConnection {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/aakhri_pasta";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static Connection connection;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error connecting to the database. Please check your database settings.",
                    "Database Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
