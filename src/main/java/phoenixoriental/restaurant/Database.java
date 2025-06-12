package phoenixoriental.restaurant;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {

    public static Connection connectDB() {
        String url = "jdbc:mysql://localhost:3307/restaurant";
        String username = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
