package main.java.carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private Connection connection;
    private String url = "jdbc:h2:./src/main/java/carsharing/db/";
    DBConnection(String fileName) {
        this.url += fileName;
    }
    public Connection connectDB() throws ClassNotFoundException, SQLException {
        Class.forName ("org.h2.Driver");
        connection = DriverManager.getConnection(url);
        connection.setAutoCommit(true);
        return connection;
    }

    public Connection getConnection() {
        return connection;
    }
}
