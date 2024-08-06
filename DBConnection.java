import javax.swing.*;
import java.sql.*;
public class DBConnection {
    String url = "jdbc:mysql://localhost:3306/";
    String username = "root";
    String password = "root";
    public Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection(url,username,password);
        return con;
    }

    public static void main(String[] args) {
        DBConnection t = new DBConnection();
        try {
            t.getConnection();
            System.out.println("con made succesfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}