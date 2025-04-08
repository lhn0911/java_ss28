package ss28;

import java.sql.*;

public class Bai4 {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/Banking_DB02";
        String user = "root";
        String password = "Lhn09112005@";

        Connection conn = null;
        PreparedStatement withdrawStmt = null;
        PreparedStatement depositStmt = null;

        int fromAccountId = 1;
        int toAccountId = 2;
        double amount = 300.0;

        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Kết nối thành công.");
            conn.setAutoCommit(false);

            String withdrawSQL = "UPDATE bank_accounts SET balance = balance - ? " +
                    "WHERE account_id = ? AND balance >= ?";
            withdrawStmt = conn.prepareStatement(withdrawSQL);
            withdrawStmt.setDouble(1, amount);
            withdrawStmt.setInt(2, fromAccountId);
            withdrawStmt.setDouble(3, amount);
            int rows1 = withdrawStmt.executeUpdate();

            if (rows1 == 0) {
                throw new SQLException("Không đủ tiền hoặc tài khoản gửi không tồn tại.");
            }

            System.out.println("Đã trừ " + amount + " từ tài khoản ID " + fromAccountId);

            String depositSQL = "UPDATE bank_accounts SET balance = balance + ? WHERE account_id = ?";
            depositStmt = conn.prepareStatement(depositSQL);
            depositStmt.setDouble(1, amount);
            depositStmt.setInt(2, toAccountId);
            int rows2 = depositStmt.executeUpdate();

            if (rows2 == 0) {
                throw new SQLException("Tài khoản nhận không tồn tại.");
            }

            System.out.println("Đã cộng " + amount + " vào tài khoản ID " + toAccountId);

            conn.commit();
            System.out.println("Chuyển khoản thành công. Giao dịch đã được commit!");

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    System.out.println("Có lỗi. Rollback...");
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (withdrawStmt != null) withdrawStmt.close();
                if (depositStmt != null) depositStmt.close();
                if (conn != null) conn.close();
                System.out.println("Đã đóng kết nối.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
