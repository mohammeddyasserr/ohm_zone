package db_edit_functions;

import java.sql.*;

public class product {
    private final int id;
    private String name;
    private double price;
    private int quantity;

    public product(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
    // === Add Product ===
    public static void addToDatabase(Connection conn,int id,String name,double price,int quantity) throws SQLException {
        String sql = "INSERT INTO product (id, name, price, quantity) VALUES (?, ?, ?, ?)";
        try (PreparedStatement s = conn.prepareStatement(sql)) {
            s.setInt(1, id);
            s.setString(2, name);
            s.setDouble(3, price);
            s.setInt(4, quantity);
            s.executeUpdate();
        }
    }

    // === Delete Product ===
    public static void deleteFromDatabase(Connection conn,int id,String name,double price,int quantity) throws SQLException {
        String sql = "DELETE FROM product WHERE id = ?";
        try (PreparedStatement s = conn.prepareStatement(sql)) {
            s.setInt(1, id);
            s.executeUpdate();
        }
    }

    // === Update Product ===
    public static void updateInDatabase(Connection conn,int id,String name,double price,int quantity) throws SQLException {
        String sql = "UPDATE product SET name = ?, price = ?, quantity = ? WHERE id = ?";
        try (PreparedStatement s = conn.prepareStatement(sql)) {
            s.setString(1, name);
            s.setDouble(2, price);
            s.setInt(3, quantity);
            s.setInt(4, id);
            s.executeUpdate();
        }
    }

}


