package user_gui_controller;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import main_package.user_session;
import org.mindrot.jbcrypt.BCrypt;
//import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class user_account_controller implements Initializable {



    @FXML
    private ImageView logo;

    @FXML
    private AnchorPane left_menu;

    @FXML
    private ScrollPane main_scroll_pane;

    private boolean isMenuVisible = true;

    @FXML
    private Button account_btn;

    @FXML
    private Button address_btn;

    @FXML
    private Label btn1_error;

    @FXML
    private Label btn2_error;

    @FXML
    private Label btn3_error;

    @FXML
    private TextField changeAddress;

    @FXML
    private TextField changeNumber;

    @FXML
    private TextField confirmPass;

    @FXML
    private Label confirmation_error;

    @FXML
    private TextField enterPass;

    @FXML
    private ImageView logoImage;

    @FXML
    private Button menu_btn;

    @FXML
    private TextField newPass;

    @FXML
    private Button number_btn;

    @FXML
    private Label number_error;

    @FXML
    private Button pass_btn;

    @FXML
    private Label pass_error;

    @FXML
    private AnchorPane topBar;

    @FXML
    private TextField username;

    @FXML
    private Label pass_updated;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        account_btn.setText(user_session.get_user());
        username.setText(user_session.get_user());
    }

    @FXML
    void edit_address_action(ActionEvent event) {

    }

    @FXML
    void edit_number_action(ActionEvent event) {

    }

    @FXML
    void edit_password_action(ActionEvent event) {
        String currentPass = enterPass.getText().trim();
        String usernameText = username.getText().trim();
        String newPassword = newPass.getText().trim();
        String confirmPassword = confirmPass.getText().trim();

        btn1_error.setVisible(false);
        pass_error.setVisible(false);
        confirmation_error.setVisible(false);
        pass_updated.setVisible(false);

        if (currentPass.isEmpty()) {
            btn1_error.setVisible(true);
            return;
        }
        String dbUrl = "jdbc:sqlite:store.db";
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            String sql = "SELECT password FROM users WHERE user_name = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, usernameText);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String hashedPassword = rs.getString("password");
                        if (!BCrypt.checkpw(currentPass, hashedPassword)) {
                            pass_error.setVisible(true);
                            return;
                        }
                    } else {
                        pass_error.setVisible(true);
                        return;
                    }
                }
            }

            if (!newPassword.equals(confirmPassword)) {
                confirmation_error.setVisible(true);
                return;
            }

            String newHashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            String updateSql = "UPDATE users SET password = ? WHERE user_name = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setString(1, newHashedPassword);
                updateStmt.setString(2, usernameText);
                updateStmt.executeUpdate();
            }


            pass_updated.setVisible(true);
            enterPass.clear();
            newPass.clear();
            confirmPass.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void toggle_menu() {
        isMenuVisible = !isMenuVisible;

        if (isMenuVisible) {
            // Show menu with animation
            left_menu.setVisible(true);
            left_menu.setManaged(true);

            TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), left_menu);
            slideIn.setFromX(-left_menu.getWidth());
            slideIn.setToX(0);
            slideIn.play();

            AnchorPane.setLeftAnchor(main_scroll_pane, 165.0);
        } else {
            // Hide menu with animation
            TranslateTransition slideOut = new TranslateTransition(Duration.millis(300), left_menu);
            slideOut.setFromX(0);
            slideOut.setToX(-left_menu.getWidth());
            slideOut.setOnFinished(event -> {
                left_menu.setVisible(false);
                left_menu.setManaged(false);
            });
            slideOut.play();

            AnchorPane.setLeftAnchor(main_scroll_pane, 0.0);
        }
    }

    @FXML
    void calculator_page(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/user_gui/calc.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void acount_page(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/user_gui/account_user.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void cart_page(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/user_gui/cart.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void home_page_user(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/user_gui/user_main.fxml"));
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
    void pills_page_user(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/admin_gui/pills.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void shop_page(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/user_gui/shop.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}

