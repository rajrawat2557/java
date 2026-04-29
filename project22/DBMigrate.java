import java.sql.*;

public class DBMigrate {
    public static void main(String[] args) {
        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();
            
            System.out.println("Deleting transactions...");
            stmt.executeUpdate("DELETE FROM transactions");
            
            System.out.println("Deleting wallets...");
            stmt.executeUpdate("DELETE FROM wallets");
            
            System.out.println("Deleting users...");
            stmt.executeUpdate("DELETE FROM users");
            
            System.out.println("Checking if pin column exists...");
            try {
                stmt.executeQuery("SELECT pin FROM users LIMIT 1");
                System.out.println("Column 'pin' already exists.");
            } catch (SQLException e) {
                System.out.println("Adding 'pin' column to users table...");
                stmt.executeUpdate("ALTER TABLE users ADD COLUMN pin VARCHAR(10)");
                System.out.println("Column added.");
            }
            
            System.out.println("Database reset and migration complete.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
