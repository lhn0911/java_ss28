package ss28;

import java.sql.*;
import java.time.LocalDate;

public class Bai8 {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/Banking_DB02";
        String user = "root";
        String password = "Lhn09112005@";

        Connection conn = null;
        PreparedStatement pstmtCheckRoom = null;
        PreparedStatement pstmtUpdateRoom = null;
        PreparedStatement pstmtInsertBooking = null;
        PreparedStatement pstmtLogFail = null;

        int customerId = 1;
        int roomId = 101;
        String status = "CONFIRMED";

        try {
            conn = DriverManager.getConnection(url, user, password);
            conn.setAutoCommit(false);

            String sqlCheckRoom = "SELECT availability FROM rooms WHERE room_id = ?";
            pstmtCheckRoom = conn.prepareStatement(sqlCheckRoom);
            pstmtCheckRoom.setInt(1, roomId);
            ResultSet rs = pstmtCheckRoom.executeQuery();

            if (rs.next() && rs.getBoolean("availability")) {
                String sqlUpdateRoom = "UPDATE rooms SET availability = false WHERE room_id = ?";
                pstmtUpdateRoom = conn.prepareStatement(sqlUpdateRoom);
                pstmtUpdateRoom.setInt(1, roomId);
                pstmtUpdateRoom.executeUpdate();

                String sqlInsertBooking = "INSERT INTO bookings(customer_id, room_id, booking_date, status) VALUES (?, ?, ?, ?)";
                pstmtInsertBooking = conn.prepareStatement(sqlInsertBooking);
                pstmtInsertBooking.setInt(1, customerId);
                pstmtInsertBooking.setInt(2, roomId);
                pstmtInsertBooking.setDate(3, Date.valueOf(LocalDate.now()));
                pstmtInsertBooking.setString(4, status);
                pstmtInsertBooking.executeUpdate();

                conn.commit();
                System.out.println("Đặt phòng thành công.");
            } else {
                String sqlLogFail = "INSERT INTO failed_bookings(customer_id, room_id, reason) VALUES (?, ?, ?)";
                pstmtLogFail = conn.prepareStatement(sqlLogFail);
                pstmtLogFail.setInt(1, customerId);
                pstmtLogFail.setInt(2, roomId);
                pstmtLogFail.setString(3, "Phòng đã được đặt hoặc không tồn tại.");
                pstmtLogFail.executeUpdate();

                conn.commit();
                System.out.println("Phòng không khả dụng. Ghi log thất bại.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                    System.out.println("Có lỗi xảy ra. Rollback...");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (pstmtCheckRoom != null) pstmtCheckRoom.close();
                if (pstmtUpdateRoom != null) pstmtUpdateRoom.close();
                if (pstmtInsertBooking != null) pstmtInsertBooking.close();
                if (pstmtLogFail != null) pstmtLogFail.close();
                if (conn != null) conn.close();
                System.out.println("Đã đóng kết nối.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
