package user_gui_controller;

import javafx.animation.TranslateTransition;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import javafx.scene.control.Label;
import java.util.HashMap;
import java.util.ResourceBundle;

public class user_account_controller implements Initializable {


    @FXML
    private ImageView logo;

    @FXML
    private Label cartCounter;

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
    private PasswordField confirmPass;

    @FXML
    private Label new_pass_error;

    @FXML
    private Label confirmation_error;

    @FXML
    private Label num_updated;

    @FXML
    private Label number_empty;

    @FXML
    private PasswordField enterPass;

    @FXML
    private ImageView logoImage;

    @FXML
    private Button menu_btn;

    @FXML
    private PasswordField newPass;

    @FXML
    private Label address_empty;

    @FXML
    private Label address_updated;

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
        updateCartCount(); // initial load

        SharedCart.cartItems.addListener((ListChangeListener<? super HashMap<String, Object>>) change -> {
            updateCartCount(); // auto update on add/remove
        });
    }

    private void updateCartCount() {
        if (cartCounter != null) {
            int count = SharedCart.getTotalItemCount();
            cartCounter.setText("Your Cart (" + count + ")");
        }
    }

    @FXML
    void edit_address_action(ActionEvent event) {
        Label[] Labels = {
                btn2_error, number_error, number_empty, num_updated,
                btn3_error, address_empty, address_updated,
                btn1_error, pass_error, confirmation_error,
                pass_updated, new_pass_error
        };

        for (Label label : Labels) {
            label.setVisible(false);
        }

        String currentPassword = enterPass.getText();
        String newAddress = changeAddress.getText();
        String currentUsername = user_session.get_user();

        if (currentPassword.isEmpty()) {
            btn3_error.setVisible(true);
            return;
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:store.db");
            String query = "SELECT password FROM users WHERE user_name = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, currentUsername);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String storedHashedPassword = resultSet.getString("password");
                if (!BCrypt.checkpw(currentPassword, storedHashedPassword)) {
                    pass_error.setVisible(true);
                    conn.close();
                    return;
                }
            } else {
                pass_error.setVisible(true);
                conn.close();
                return;
            }

            if (newAddress.isEmpty()) {
                address_empty.setVisible(true);
                conn.close();
                return;
            }

            String updateQuery = "UPDATE users SET address = ? WHERE user_name = ?";
            PreparedStatement updateStatement = conn.prepareStatement(updateQuery);
            updateStatement.setString(1, newAddress);
            updateStatement.setString(2, currentUsername);
            updateStatement.executeUpdate();

            address_updated.setVisible(true);
            enterPass.clear();
            changeAddress.clear();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void edit_number_action(ActionEvent event) {
        Label[] Labels = {
                btn2_error, number_error, number_empty, num_updated,
                btn3_error, address_empty, address_updated,
                btn1_error, pass_error, confirmation_error,
                pass_updated, new_pass_error
        };

        for (Label label : Labels) {
            label.setVisible(false);
        }

        String currentPassword = enterPass.getText();
        String newPhoneNumber = changeNumber.getText();
        String currentUsername = user_session.get_user();

        if (currentPassword.isEmpty()) {
            btn2_error.setVisible(true);
            return;
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:store.db");
            String query = "SELECT password FROM users WHERE user_name = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, currentUsername);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String storedHashedPassword = resultSet.getString("password");
                if (!BCrypt.checkpw(currentPassword, storedHashedPassword)) {
                    pass_error.setVisible(true);
                    conn.close();
                    return;
                }
            } else {
                pass_error.setVisible(true);
                conn.close();
                return;
            }

            if (newPhoneNumber.isEmpty()) {
                number_empty.setVisible(true);
                conn.close();
                return;
            }

            if (newPhoneNumber.length() < 11) {
                number_error.setVisible(true);
                conn.close();
                return;
            }

            String updateQuery = "UPDATE users SET phone = ? WHERE user_name = ?";
            PreparedStatement updateStatement = conn.prepareStatement(updateQuery);
            updateStatement.setString(1, newPhoneNumber);
            updateStatement.setString(2, currentUsername);
            updateStatement.executeUpdate();

            num_updated.setVisible(true);
            enterPass.clear();
            changeNumber.clear();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void edit_password_action(ActionEvent event) {
        Label[] Labels = {
                btn2_error, number_error, number_empty, num_updated,
                btn3_error, address_empty, address_updated,
                btn1_error, pass_error, confirmation_error,
                pass_updated, new_pass_error
        };

        for (Label label : Labels) {
            label.setVisible(false);
        }

        String currentPass = enterPass.getText().trim();
        String usernameText = username.getText().trim();
        String newPassword = newPass.getText().trim();
        String confirmPassword = confirmPass.getText().trim();

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
                            conn.close();
                            return;
                        }
                    } else {
                        pass_error.setVisible(true);
                        conn.close();
                        return;
                    }
                }
            }
            if (newPassword.isEmpty()) {
                new_pass_error.setVisible(true);
                conn.close();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                confirmation_error.setVisible(true);
                conn.close();
                return;
            }

            String updateSql = "UPDATE users SET password = ? WHERE user_name = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setString(1, BCrypt.hashpw(newPassword, BCrypt.gensalt()));
                updateStmt.setString(2, usernameText);
                updateStmt.executeUpdate();
            }

            pass_updated.setVisible(true);
            enterPass.clear();
            newPass.clear();
            confirmPass.clear();
            conn.close();
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
        Parent newRoot = FXMLLoader.load(getClass().getResource("/user_gui/calc.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(newRoot);
    }

    @FXML
    void acount_page(ActionEvent event) throws IOException {
        Parent newRoot = FXMLLoader.load(getClass().getResource("/user_gui/account_page.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(newRoot);
    }

    @FXML
    void cart_page(ActionEvent event) throws IOException {
        Parent newRoot = FXMLLoader.load(getClass().getResource("/user_gui/cart.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(newRoot);
    }

    @FXML
    void home_page_user(ActionEvent event) throws IOException {
        Parent newRoot = FXMLLoader.load(getClass().getResource("/user_gui/user_main.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(newRoot);
    }

    @FXML
    void login_page(ActionEvent event) throws IOException {
        Parent newRoot = FXMLLoader.load(getClass().getResource("/main_package/login.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(newRoot);
    }

    @FXML
    void pills_page_user(ActionEvent event) throws IOException {
        Parent newRoot = FXMLLoader.load(getClass().getResource("/user_gui/pills.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(newRoot);
    }

    @FXML
    void shop_page(ActionEvent event) throws IOException {
        Parent newRoot = FXMLLoader.load(getClass().getResource("/user_gui/shop.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(newRoot);
    }
}