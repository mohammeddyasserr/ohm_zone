package admin_gui_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main_package.user_session;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class account_controller implements Initializable {

    @FXML  TextField user;
    @FXML  PasswordField old_pass;
    @FXML  PasswordField new_pass;
    @FXML  PasswordField confirm_pass;

    @FXML  Label old_error;
    @FXML  Label new_error;
    @FXML  Label confirm_error;
    @FXML  Label result_label;

    @FXML  Button editbtn;
    @FXML  Button account_btn;
    @FXML
<<<<<<< Updated upstream
     Pane side_panel;
=======
    Pane side_panel;
>>>>>>> Stashed changes
    @FXML
    Button menu_btn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        user.setText(user_session.get_user());
        user.setEditable(false);
        account_btn.setText(user_session.get_user());

    }
    @FXML
<<<<<<< Updated upstream
   void edit_password(ActionEvent event) {
=======
    void edit_password(ActionEvent event) {
>>>>>>> Stashed changes
        // Reset errors
        old_error.setText("");
        new_error.setText("");
        confirm_error.setText("");
        result_label.setText("");

        String username = user.getText().trim();
        String currentPassword = old_pass.getText().trim();
        String newPassword = new_pass.getText().trim();
        String confirmPassword = confirm_pass.getText().trim();

        boolean valid = true;

        if (currentPassword.isEmpty()) {
            old_error.setText("Please enter your current password.");
            valid = false;
        }

        if (newPassword.isEmpty()) {
            new_error.setText("Please enter a new password.");
            valid = false;
        }

        if (!newPassword.equals(confirmPassword)) {
            confirm_error.setText("Passwords do not match.");
            valid = false;
        }

        if (!valid) return;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:store.db")) {
            String query = "SELECT password FROM admins WHERE user_name = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                if (!BCrypt.checkpw(currentPassword, hashedPassword)) {
                    old_error.setText("Incorrect old password.");
                    return;
                }
            } else {
                old_error.setText("Admin not found.");
                return;
            }

            String updateSql = "UPDATE admins SET password = ? WHERE user_name = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setString(1, BCrypt.hashpw(newPassword, BCrypt.gensalt()));
            updateStmt.setString(2, username);
            updateStmt.executeUpdate();

            result_label.setText("Password updated successfully.");
            old_pass.clear();
            new_pass.clear();
            confirm_pass.clear();

        } catch (Exception e) {
            e.printStackTrace();
            result_label.setText("An error occurred while updating the password.");
        }
    }

    // Navigation Methods
    @FXML
    void acount_page(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/admin_gui/account.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void adda_page(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/admin_gui/add_admin.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
<<<<<<< Updated upstream
        System.out.println("Admin page button clicked");
=======
>>>>>>> Stashed changes

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

<<<<<<< Updated upstream
}
=======
}
>>>>>>> Stashed changes
