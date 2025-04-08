package ss28;

import java.sql.*;

public class Bai1 {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/Banking_DB02";
        String user = "root";
        String password = "Lhn09112005@";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Kết nối thành công!");
            System.out.println("Auto-commit ban đầu: " + conn.getAutoCommit());

            conn.setAutoCommit(false);
            System.out.println("Auto-commit sau khi tắt: " + conn.getAutoCommit());

            String sql = "INSERT INTO users(name, email) VALUES (?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "Nguyễn Văn A");
            pstmt.setString(2, "nva@example.com");

            int rows = pstmt.executeUpdate();
            System.out.println("Đã thêm " + rows + " dòng.");

            conn.commit();
            System.out.println("Commit thành công. Dữ liệu đã được lưu!");

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    System.out.println("Có lỗi xảy ra. Rollback...");
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
                System.out.println("Đã đóng kết nối.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
