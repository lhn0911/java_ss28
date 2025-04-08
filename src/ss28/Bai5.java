package ss28;

import java.sql.*;
import java.time.LocalDate;

public class Bai5 {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/Banking_DB02";
        String user = "root";
        String password = "Lhn09112005@";

        Connection conn = null;
        PreparedStatement insertOrderStmt = null;
        PreparedStatement insertDetailStmt = null;

        try {
            conn = DriverManager.getConnection(url, user, password);
            conn.setAutoCommit(false);

            String insertOrderSQL = "INSERT INTO orders (customer_name, order_date) VALUES (?, ?)";
            insertOrderStmt = conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS);
            insertOrderStmt.setString(1, "Nguyễn Văn B");
            insertOrderStmt.setDate(2, Date.valueOf(LocalDate.now()));
            insertOrderStmt.executeUpdate();

            ResultSet rs = insertOrderStmt.getGeneratedKeys();
            int orderId = 0;
            if (rs.next()) orderId = rs.getInt(1);

            String insertDetailSQL = "INSERT INTO order_details (order_id, product_name, quantity) VALUES (?, ?, ?)";
            insertDetailStmt = conn.prepareStatement(insertDetailSQL);

            insertDetailStmt.setInt(1, orderId);
            insertDetailStmt.setString(2, "Sản phẩm A");
            insertDetailStmt.setInt(3, 2);
            insertDetailStmt.executeUpdate();
//
//            insertDetailStmt.setInt(1, orderId);
//            insertDetailStmt.setString(2, "Sản phẩm B");
//            insertDetailStmt.setInt(3, -1);
//            insertDetailStmt.executeUpdate();

            conn.commit();
            System.out.println("Đơn hàng và chi tiết đã được lưu.");
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    System.out.println("Lỗi xảy ra. Rollback...");
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (insertOrderStmt != null) insertOrderStmt.close();
                if (insertDetailStmt != null) insertDetailStmt.close();
                if (conn != null) conn.close();
                System.out.println("Đã đóng kết nối.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
