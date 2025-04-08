package ss28;

import java.sql.*;

public class Bai2 {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/Banking_DB02";
        String user = "root";
        String password = "Lhn09112005@";
        Connection conn = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;

        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Kết nối thành công!");
            System.out.println("Auto-commit ban đầu: " + conn.getAutoCommit());

            conn.setAutoCommit(false);
            System.out.println("Auto-commit sau khi tắt: " + conn.getAutoCommit());

            String sql1 = "INSERT INTO users(id, name, email) VALUES (?, ?, ?)";
            pstmt1 = conn.prepareStatement(sql1);
            pstmt1.setInt(1, 101);
            pstmt1.setString(2, "Nguyễn Văn B");
            pstmt1.setString(3, "nvb@example.com");
            pstmt1.executeUpdate();
            System.out.println("Insert 1 thành công.");

            String sql2 = "INSERT INTO users(id, name, email) VALUES (?, ?, ?)";
            pstmt2 = conn.prepareStatement(sql2);
            pstmt2.setInt(1, 101); // Cố tình trùng ID để gây lỗi
            pstmt2.setString(2, "Lỗi ID trùng");
            pstmt2.setString(3, "loi@example.com");
            pstmt2.executeUpdate();
            System.out.println("Insert 2 thành công.");

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
                if (pstmt1 != null) pstmt1.close();
                if (pstmt2 != null) pstmt2.close();
                if (conn != null) conn.close();
                System.out.println("Đã đóng kết nối.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
