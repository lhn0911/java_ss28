package ss28;

import java.sql.*;

public class Bai7 {
    static String url = "jdbc:mysql://localhost:3306/Banking_DB02";
    static String user = "root";
    static String password = "Lhn09112005@";

    public static void main(String[] args) throws InterruptedException {
        Thread writer = new Thread(() -> {
            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                conn.setAutoCommit(false);
                String sql = "INSERT INTO orders(customer_name, status) VALUES (?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, "Nguyễn Văn B");
                pstmt.setString(2, "PENDING");
                pstmt.executeUpdate();
                System.out.println("[Writer] Chèn xong, chưa commit");
                Thread.sleep(5000);
                conn.commit();
                System.out.println("[Writer] Commit xong");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Thread reader = new Thread(() -> {
            try {
                Thread.sleep(1000);
                Connection conn = DriverManager.getConnection(url, user, password);
                conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
                conn.setAutoCommit(false);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM orders");
                System.out.println("[Reader] Kết quả đọc:");
                while (rs.next()) {
                    System.out.println(rs.getInt("order_id") + " - " + rs.getString("customer_name") + " - " + rs.getString("status"));
                }
                conn.commit();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();
    }
}
