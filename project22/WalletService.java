import java.sql.*;

public class WalletService {

    public static int getWalletId(int userId) {
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT id FROM wallets WHERE user_id=?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }

    public static double getBalance(int walletId) {
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT balance FROM wallets WHERE id=?");
            ps.setInt(1, walletId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public static boolean deposit(int walletId, double amount) {
        try {
            Connection con = DBConnection.getConnection();
            System.out.println("Depositing " + amount + " to your account...");
            PreparedStatement ps = con.prepareStatement("UPDATE wallets SET balance = balance + ? WHERE id=?");
            ps.setDouble(1, amount);
            ps.setInt(2, walletId);
            int updated = ps.executeUpdate();
            if (updated == 1) {
                System.out.println(" Deposit successful! Current Balance: " + getBalance(walletId));
                return true;
            } else {
                System.out.println(" Deposit failed. Invalid wallet id.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean withdraw(int walletId, double amount) {
        try {
            Connection con = DBConnection.getConnection();
            double current = getBalance(walletId);
            if (current < amount) {
                System.out.println("Insufficient balance. Cannot withdraw.");
                return false;
            }
            System.out.println("Withdrawing " + amount + " from your account...");
            PreparedStatement ps = con.prepareStatement("UPDATE wallets SET balance = balance - ? WHERE id=?");
            ps.setDouble(1, amount);
            ps.setInt(2, walletId);
            int updated = ps.executeUpdate();
            if (updated == 1) {
                System.out.println("Withdrawal successful! Current Balance: " + getBalance(walletId));
                return true;
            } else {
                System.out.println(" Withdrawal failed. Invalid wallet id.");
                return false;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}