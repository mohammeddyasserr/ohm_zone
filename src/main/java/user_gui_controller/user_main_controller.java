package user_gui_controller;

import javafx.animation.TranslateTransition;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.util.Duration;
import javafx.scene.input.MouseEvent;

import javafx.event.ActionEvent;
import main_package.user_session;

import javax.swing.text.html.ImageView;
import java.awt.*;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.ResourceBundle;


public class user_main_controller implements Initializable {


    @FXML
    private ImageView logo;

    @FXML
    private AnchorPane left_menu;

    @FXML
    private ScrollPane main_scroll_pane;

    private boolean isMenuVisible = true;

    @FXML
    private Label cartCounter;


    @FXML
    private Button account_btn;

    @FXML
    private Label orders_value;

    @FXML
    private Label cart_value;


    private void loadUserOrderCount() {
        String username = account_btn.getText();

        String query = "SELECT COUNT(*) AS count FROM orders WHERE username = ?";

        try (Connection con = DriverManager.getConnection("jdbc:sqlite:store.db");
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    orders_value.setText(Integer.toString(count));
                } else {
                    orders_value.setText("0");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            orders_value.setText("Error");
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        account_btn.setText(user_session.get_user());
        loadUserOrderCount();
        updateCartCount(); // initial load
        cart_value.setText(Integer.toString(SharedCart.getTotalItemCount()));

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
    void to_cart(MouseEvent event) throws IOException {
        Parent newRoot = FXMLLoader.load(getClass().getResource("/user_gui/cart.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(newRoot);
    }

    @FXML
    void to_orders(MouseEvent event) throws IOException {
        Parent newRoot = FXMLLoader.load(getClass().getResource("/user_gui/pills.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(newRoot);
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