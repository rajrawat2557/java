import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class TransactionService {

    public static boolean transfer(int fromWallet, int toWallet, double amount) {
        try {
            Connection con = DBConnection.getConnection();
            con.setAutoCommit(false);

            System.out.println("Checking sender account...");
            PreparedStatement checkFrom = con.prepareStatement(
                    "SELECT balance FROM wallets WHERE id=?");
            checkFrom.setInt(1, fromWallet);
            ResultSet rsFrom = checkFrom.executeQuery();

            if (!rsFrom.next()) {
                System.out.println(" Sender account does not exist. Transaction cancelled.");
                return false;
            }

            double senderBalance = rsFrom.getDouble(1);

            System.out.println("Checking receiver account...");
            PreparedStatement checkTo = con.prepareStatement(
                    "SELECT * FROM wallets WHERE id=?");
            checkTo.setInt(1, toWallet);
            ResultSet rsTo = checkTo.executeQuery();

            if (!rsTo.next()) {
                System.out.println(" Receiver account does not exist. Transaction cancelled.");
                return false;
            }

            if (fromWallet == toWallet) {
                System.out.println(" Cannot transfer to the same account.");
                return false;
            }

            if (senderBalance < amount) {
                System.out.println(" Insufficient balance. Transaction cancelled.");
                return false;
            }

            System.out.println("Debiting sender account...");
            PreparedStatement debit = con.prepareStatement(
                    "UPDATE wallets SET balance = balance - ? WHERE id=?");
            debit.setDouble(1, amount);
            debit.setInt(2, fromWallet);
            debit.executeUpdate();
            System.out.println("Debited successfully.");

            System.out.println("Crediting receiver account...");
            PreparedStatement credit = con.prepareStatement(
                    "UPDATE wallets SET balance = balance + ? WHERE id=?");
            credit.setDouble(1, amount);
            credit.setInt(2, toWallet);
            credit.executeUpdate();
            System.out.println(" Credited successfully.");

            con.commit();

            log(fromWallet, toWallet, amount);

            return true;

        } catch (Exception e) {
            System.out.println(" Transaction failed due to an error.");
            e.printStackTrace();
            return false;
        }
    }

    public static void log(int from, int to, double amount) {
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO transactions(from_wallet, to_wallet, amount) VALUES (?, ?, ?)");
            ps.setInt(1, from);
            ps.setInt(2, to);
            ps.setDouble(3, amount);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showHistory(int walletId) {
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM transactions WHERE from_wallet=? OR to_wallet=? ORDER BY id ASC");
            ps.setInt(1, walletId);
            ps.setInt(2, walletId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n===== Your Transaction History =====");
            while (rs.next()) {
                System.out.println("From: " + rs.getInt("from_wallet") +
                        " | To: " + rs.getInt("to_wallet") +
                        " | Amount: " + rs.getDouble("amount") +
                        " | Time: " + rs.getTimestamp("timestamp"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object[][] getHistoryData(int walletId) {
        List<Object[]> list = new ArrayList<>();
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM transactions WHERE from_wallet=? OR to_wallet=? ORDER BY id DESC");
            ps.setInt(1, walletId);
            ps.setInt(2, walletId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Object[]{
                        rs.getInt("id"),
                        rs.getInt("from_wallet"),
                        rs.getInt("to_wallet"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("timestamp")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list.toArray(new Object[0][0]);
    }
}