package db_edit_functions;
import java.sql.* ;

public class admin {
    private String userName ;
    private String password ;

    public admin(String userName, String password) {
        this.userName = userName ;
        this.password = password ;
    }

    //Add Admin
    public static void addToDatabase(Connection conn,String userName,String password) throws SQLException {
        String sql = "INSERT INTO admins (user_name , password) VALUES (? , ?)";
        try (PreparedStatement s = conn.prepareStatement(sql)) {
            s.setString(1 , userName);
            s.setString(2 , password);
            s.executeUpdate();
        }
    }

    //Update Admin
    public static void updateInDatabase(Connection conn,String userName,String password) throws SQLException {
        String sql = "UPDATE admins SET password = ? WHERE user_name = ?";
        try (PreparedStatement s = conn.prepareStatement(sql)) {
            s.setString(1 , password);
            s.setString(2 , userName);
            s.executeUpdate();
        }
    }

    //Delete Admin
    public static void deleteFromDatabase(Connection conn,String userName) throws SQLException {
        String sql = "DELETE FROM admins WHERE user_name = ?";
        try (PreparedStatement s = conn.prepareStatement(sql)) {
            s.setString(1 , userName);
            s.executeUpdate();
        }
    }

}
