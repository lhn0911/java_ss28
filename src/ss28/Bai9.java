package ss28;

import java.sql.*;
import java.util.Scanner;

public class Bai9 {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/banking_db02";
        String user = "root";
        String password = "Lhn09112005@";

        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập user_id: ");
        int userId = Integer.parseInt(sc.nextLine());
        System.out.print("Nhập auction_id: ");
        int auctionId = Integer.parseInt(sc.nextLine());
        System.out.print("Nhập giá đặt: ");
        double bidAmount = Double.parseDouble(sc.nextLine());

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            try (
                    PreparedStatement getUser = conn.prepareStatement("SELECT balance FROM users WHERE user_id = ?");
                    PreparedStatement getAuction = conn.prepareStatement("SELECT highest_bid FROM auctions WHERE auction_id = ?");
                    PreparedStatement updateAuction = conn.prepareStatement("UPDATE auctions SET highest_bid = ? WHERE auction_id = ?");
                    PreparedStatement insertBid = conn.prepareStatement("INSERT INTO bids(auction_id, user_id, bid_amount) VALUES (?, ?, ?)");
                    PreparedStatement insertFail = conn.prepareStatement("INSERT INTO failed_bids(user_id, auction_id, reason) VALUES (?, ?, ?)");
            ) {
                getUser.setInt(1, userId);
                ResultSet rsUser = getUser.executeQuery();
                if (!rsUser.next()) {
                    insertFail.setInt(1, userId);
                    insertFail.setInt(2, auctionId);
                    insertFail.setString(3, "Người dùng không tồn tại");
                    insertFail.executeUpdate();
                    conn.rollback();
                    return;
                }
                double balance = rsUser.getDouble("balance");

                getAuction.setInt(1, auctionId);
                ResultSet rsAuction = getAuction.executeQuery();
                if (!rsAuction.next()) {
                    insertFail.setInt(1, userId);
                    insertFail.setInt(2, auctionId);
                    insertFail.setString(3, "Phiên đấu giá không tồn tại");
                    insertFail.executeUpdate();
                    conn.rollback();
                    return;
                }
                double highestBid = rsAuction.getDouble("highest_bid");

                if (bidAmount <= highestBid) {
                    insertFail.setInt(1, userId);
                    insertFail.setInt(2, auctionId);
                    insertFail.setString(3, "Giá đặt phải cao hơn giá hiện tại");
                    insertFail.executeUpdate();
                    conn.rollback();
                    return;
                }

                if (balance < bidAmount) {
                    insertFail.setInt(1, userId);
                    insertFail.setInt(2, auctionId);
                    insertFail.setString(3, "Số dư không đủ");
                    insertFail.executeUpdate();
                    conn.rollback();
                    return;
                }

                updateAuction.setDouble(1, bidAmount);
                updateAuction.setInt(2, auctionId);
                updateAuction.executeUpdate();

                insertBid.setInt(1, auctionId);
                insertBid.setInt(2, userId);
                insertBid.setDouble(3, bidAmount);
                insertBid.executeUpdate();

                conn.commit();
                System.out.println("Đặt giá thành công!");
            } catch (Exception e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
