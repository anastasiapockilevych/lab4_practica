import java.sql.*;
import java.util.Vector;

public class DbConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/alcohol_db?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "root";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static Vector<Vector<Object>> getAllProducts() {
        Vector<Vector<Object>> data = new Vector<>();
        String query = "SELECT * FROM products";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("type"));
                row.add(rs.getString("brand"));
                row.add(rs.getString("manufacturer"));
                row.add(rs.getString("supplier"));
                row.add(rs.getDate("expiry_date"));
                row.add(rs.getBigDecimal("price"));
                data.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static boolean addProduct(String type, String brand, String manuf, String supp, String date, double price) {
        String query = "INSERT INTO products (type, brand, manufacturer, supplier, expiry_date, price) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, type);
            pstmt.setString(2, brand);
            pstmt.setString(3, manuf);
            pstmt.setString(4, supp);
            pstmt.setString(5, date);
            pstmt.setDouble(6, price);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Vector<Vector<Object>> searchProducts(String keyword) {
        Vector<Vector<Object>> data = new Vector<>();
        String query = "SELECT * FROM products WHERE brand LIKE ? OR type LIKE ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            String key = "%" + keyword + "%";
            pstmt.setString(1, key);
            pstmt.setString(2, key);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("type"));
                row.add(rs.getString("brand"));
                row.add(rs.getString("manufacturer"));
                row.add(rs.getString("supplier"));
                row.add(rs.getDate("expiry_date"));
                row.add(rs.getBigDecimal("price"));
                data.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
