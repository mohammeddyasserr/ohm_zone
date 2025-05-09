package db_edit_functions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class User {
    private String username;
    private String password;
    private String phone;
    private String address;

    //constructor
    public User(String username, String password, String phone, String address) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.address = address;
    }

    // === Add User ===
    public static void addToDatabase(Connection conn, String username, String password, String phone, String address) throws SQLException {
        String sql = "INSERT INTO users (user_name, password, phone, address) VALUES (?, ?, ?, ?)";
        try (PreparedStatement s = conn.prepareStatement(sql)) {
            s.setString(1, username);
            s.setString(2, password);
            s.setString(3, phone);
            s.setString(4, address);
            s.executeUpdate();
        }
    }

    // === Delete User ===
    public static void deleteFromDatabase(Connection conn, String username, String password, String phone, String address) throws SQLException {
        String sql = "DELETE FROM users WHERE user_name = ?";
        try (PreparedStatement s = conn.prepareStatement(sql)) {
            s.setString(1, username);
            s.executeUpdate();
        }
    }

    // === Update User ===
    public static void updateInDatabase(Connection conn, String username, String password, String phone, String address) throws SQLException {
        String sql = "UPDATE users SET password = ?, phone = ?, address = ? WHERE user_name = ?";
        try (PreparedStatement s = conn.prepareStatement(sql)) {
            s.setString(1, password);
            s.setString(2, phone);
            s.setString(3, address);
            s.setString(4, username);
            s.executeUpdate();
        }
    }
}
