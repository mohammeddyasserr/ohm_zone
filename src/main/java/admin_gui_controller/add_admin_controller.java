package admin_gui_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main_package.user_session;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class add_admin_controller implements Initializable {

    @FXML
    private Pane side_panel;
    @FXML
    private Button menu_btn;

    // Add Admin Fields
    @FXML
    TextField user_name;
    @FXML
    PasswordField password;
    @FXML
    PasswordField password_confirm;
    @FXML
    Label username_error;
    @FXML
    Label password_error;
    @FXML
    Label confirm_error;
    @FXML
    Label result_label;
    @FXML
    Button addbtn;
    @FXML
    private Button account_btn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        account_btn.setText(user_session.get_user());
    }


    // Navigation Methods
    @FXML
    void acount_page(ActionEvent event) {

    }

    @FXML
    void adda_page(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/admin_gui/add_admin.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        System.out.println("Admin page button clicked");

    }


    @FXML
    void addp_page(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/admin_gui/add_product.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void editp_page(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/admin_gui/edit_product.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void home_page(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/admin_gui/admin_main.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void pills_page(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/admin_gui/pills_admin.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void removep_page(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/admin_gui/remove_product.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void login_page(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/main_package/login.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void toggle_menu(ActionEvent event) {
        boolean isVisible=side_panel.isVisible();
        side_panel.setVisible(!isVisible);
        //side_panel.setManaged(!isVisible);
    }

    // Add Admin Logic
    @FXML
    void add_admin(ActionEvent event) {
        // Reset error messages
        username_error.setText("");
        password_error.setText("");
        confirm_error.setText("");
        result_label.setText("");

        String username = user_name.getText().trim();
        String pass = password.getText();
        String confirmPass = password_confirm.getText();

        boolean valid = true;

        // Validation
        if (username.isEmpty()) {
            username_error.setText("Username is required.");
            valid = false;
        }

        if (pass.isEmpty()) {
            password_error.setText("Password is required.");
            valid = false;
        }

        if (confirmPass.isEmpty()) {
            confirm_error.setText("Confirm password is required.");
            valid = false;
        } else if (!pass.equals(confirmPass)) {
            confirm_error.setText("Passwords do not match.");
            valid = false;
        }

        if (valid) {
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:store.db")) {
                // Check if username already exists
                String checkSql = "SELECT * FROM admins WHERE user_name = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                checkStmt.setString(1, username);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    result_label.setText("Admin already exists.");
                } else {
                    String insertSql = "INSERT INTO admins (user_name, password) VALUES (?, ?)";
                    PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                    insertStmt.setString(1, username);
                    insertStmt.setString(2, pass); // Consider hashing in real apps
                    insertStmt.executeUpdate();

                    result_label.setText("Admin added successfully.");
                    user_name.clear();
                    password.clear();
                    password_confirm.clear();
                }
            } catch (SQLException e) {
                result_label.setText("Database error: " + e.getMessage());
                e.printStackTrace();
            }
        }

    }
}
