import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {
     static final String DB_URL = "jdbc:mysql://localhost:3306/mydb";
     static final String DB_USER = "root";
     static final String DB_PASSWORD = "Cait1trick*";
     static final Logger LOGGER = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    public void run() {
        DatabaseHelper dbHelper = new DatabaseHelper();

        try (Connection con = dbHelper.createConnection()) {
            dbHelper.insertData(con, "Fawzy");
            dbHelper.updateData(con, 1, "Daniel");
            dbHelper.deleteData(con, 1);
            dbHelper.selectData(con);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
        }

    }
    
}

class DatabaseHelper {
    private static final Logger LOGGER = Logger.getLogger(DatabaseHelper.class.getName());
    public Connection createConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(App.DB_URL, App.DB_USER, App.DB_PASSWORD);
        } catch (ClassNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "Database driver not found", ex);
            throw new SQLException("Database driver not found", ex);
        }
    }

    public void selectData(Connection con) {
        String query = "SELECT * FROM USERS";
        try (Statement stmt = con.createStatement(); ResultSet result = stmt.executeQuery(query)) {
            while (result.next()) {
                String name = result.getString("name");
                System.out.println(name);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error executing SELECT query", ex);
        }
    }

    public void insertData(Connection con, String name) {
        String query = "INSERT INTO USERS (name) VALUES (?)";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
        pstmt.setString(1, name);
        int rowsAffected = pstmt.executeUpdate();
        System.out.println("Rows inserted: " + rowsAffected);
        } catch (SQLException ex) {
        LOGGER.log(Level.SEVERE, "Error executing INSERT query", ex);
        }
    }


    public void updateData(Connection con, int userId, String newName) {
        String query = "UPDATE USERS SET name = ? WHERE id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, userId);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows updated: " + rowsAffected);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error executing UPDATE query", ex);
        }
    }

    public void deleteData(Connection con, int userId) {
        String query = "DELETE FROM USERS WHERE id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows deleted: " + rowsAffected);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error executing DELETE query", ex);
        }
    }
}
