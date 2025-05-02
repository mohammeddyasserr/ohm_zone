package admin_gui_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import java.sql.*;

public class add_admin_controller {


    @FXML
    private Pane side_panel;
    @FXML
    private Button menu_btn;


    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label messageLabel;


    @FXML
    void toggle_menu(ActionEvent event) {
        boolean isVisible = side_panel.isVisible();
        side_panel.setVisible(!isVisible);

    }


    @FXML
    private void handleAddAdmin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please fill in all fields.");
            return;
        }

        try {

            Connection con = DriverManager.getConnection("jdbc:sqlite:store.db");


            PreparedStatement checkStmt = con.prepareStatement("SELECT COUNT(*) FROM admins WHERE username = ?");
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                messageLabel.setText("Username already exists.");
            } else {

                PreparedStatement insertStmt = con.prepareStatement("INSERT INTO admins(username, password) VALUES(?, ?)");
                insertStmt.setString(1, username);
                insertStmt.setString(2, password);
                insertStmt.executeUpdate();
                messageLabel.setText("Admin added successfully.");
                insertStmt.close();
            }

            rs.close();
            checkStmt.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
            messageLabel.setText("Database error occurred.");
        }
    }
}