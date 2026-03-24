import java.sql.*;

public class UserService {

    public static int signup(String username, String password) {
        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO users(username, password) VALUES (?, ?)");
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();

            PreparedStatement ps2 = con.prepareStatement(
                    "SELECT user_id FROM users WHERE username=? AND password=?");
            ps2.setString(1, username);
            ps2.setString(2, password);

            ResultSet rs = ps2.executeQuery();

            int userId = -1;
            if (rs.next()) userId = rs.getInt(1);

            PreparedStatement ps3 = con.prepareStatement(
                    "INSERT INTO wallets(user_id, balance) VALUES (?, 0)");
            ps3.setInt(1, userId);
            ps3.executeUpdate();

            System.out.println("Signup successful.");
            return userId;

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int login(String username, String password) {
        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT user_id FROM users WHERE username=? AND password=?");
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Login successful.");
                return rs.getInt(1);
            } else {
                System.out.println("Invalid credentials.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}