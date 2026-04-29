import java.sql.*;

public class DBCheck {
    public static void main(String[] args) throws Exception {
        Connection con = DBConnection.getConnection();
        ResultSet rs = con.getMetaData().getColumns(null, null, "transactions", null);
        while (rs.next()) {
            System.out.println(rs.getString("COLUMN_NAME"));
        }
    }
}
