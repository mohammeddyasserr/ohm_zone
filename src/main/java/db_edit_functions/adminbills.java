package db_edit_functions;

import java.sql.*;

public class adminbills {
    private int bill_id;
    private String order_date;
    private double total_price;
    private String username;

    public adminbills(int bill_id, String order_date, double total_price, String username) {
        this.bill_id = bill_id;
        this.order_date = order_date;
        this.total_price = total_price;
        this.username = username;
    }

    private static Connection getConnection() throws SQLException {
        String dbUrl = "jdbc:sqlite:store.db";
        return DriverManager.getConnection(dbUrl);
    }

    public static void addToDatabase(int bill_id, String order_date, double total_price, String username) throws SQLException {
        String sql = "INSERT INTO orders (bill_id, order_date, total_price, username) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement s = conn.prepareStatement(sql)) {
            s.setInt(1, bill_id);
            s.setString(2, order_date);
            s.setDouble(3, total_price);
            s.setString(4, username);
            s.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error adding bill: " + e.getMessage());
        }
    }


    public int getBill_id() { return bill_id; }
    public String getDate() { return order_date; }
    public double getTotal_price() { return total_price; }
    public String getUsername() { return username; }



}