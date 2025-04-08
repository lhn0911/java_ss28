package ss28;

import java.sql.*;

public class Bai6 {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/Banking_DB02";
        String user = "root";
        String password = "Lhn09112005@";

        Connection conn = null;
        PreparedStatement deptStmt = null;
        PreparedStatement empStmt = null;

        try {
            conn = DriverManager.getConnection(url, user, password);
            conn.setAutoCommit(false);

            String insertDeptSQL = "INSERT INTO departments(name) VALUES (?)";
            deptStmt = conn.prepareStatement(insertDeptSQL, Statement.RETURN_GENERATED_KEYS);
            deptStmt.setString(1, "Phòng Nhân sự");
            deptStmt.executeUpdate();

            ResultSet rs = deptStmt.getGeneratedKeys();
            int deptId = 0;
            if (rs.next()) deptId = rs.getInt(1);

            String insertEmpSQL = "INSERT INTO employees(name, department_id) VALUES (?, ?)";
            empStmt = conn.prepareStatement(insertEmpSQL);

            empStmt.setString(1, "Nguyễn Thị C");
            empStmt.setInt(2, deptId);
            empStmt.executeUpdate();

            empStmt.setString(1, "Trần Văn D");
            empStmt.setInt(2, deptId);
            empStmt.executeUpdate();

//            empStmt.setString(1, null);
//            empStmt.setInt(2, deptId);
//            empStmt.executeUpdate();

            conn.commit();
            System.out.println("Đã thêm phòng ban và nhân viên.");
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
                if (deptStmt != null) deptStmt.close();
                if (empStmt != null) empStmt.close();
                if (conn != null) conn.close();
                System.out.println("Đã đóng kết nối.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
