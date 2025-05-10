package user_gui_controller;

import db_edit_functions.product;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import javafx.event.ActionEvent;
import main_package.user_session;


import javax.swing.*;
import java.awt.*;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class shop_controller implements Initializable {



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
    private GridPane productGrid;

    @FXML
    private AnchorPane detailsPane;

    @FXML private ImageView detailsImage;

    @FXML private Label detailsName;

    @FXML private Label detailsPrice;

    @FXML private Text detailsDescription;

    @FXML
    private ComboBox options;

    @FXML
    private TextField quantity;

    @FXML
    private VBox Resistance, Capacitor, Battery, Diode, ICS, Jumpers, Transistor, Test_Board, Soldering_Iron, Avo, Boards, Switch;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        account_btn.setText(user_session.get_user());
        Resistance.setUserData("Resistance");
        Capacitor.setUserData("Capacitor");
        Battery.setUserData("Battery");
        Diode.setUserData("Diode");
        ICS.setUserData("IC");
        Jumpers.setUserData("Jumpers");
        Transistor.setUserData("Transistor");
        Test_Board.setUserData("Test Board");
        Soldering_Iron.setUserData("Soldering Iron");
        Avo.setUserData("Multimeter");
        Boards.setUserData("Programmable Boards");
        Switch.setUserData("Switch");
    }







    private void showDetailsPane() {
        detailsPane.setVisible(true);

        TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), detailsPane);
        slideIn.setFromX(300);  // Start from right
        slideIn.setToX(0);      // Slide to original position
        slideIn.play();
    }
    @FXML
    private void onProductClick(MouseEvent event) {
        VBox clickedBox = (VBox) event.getSource();
        String productName = (String) clickedBox.getUserData();
        String cat="";
        if (productName != null) {
            detailsName.setText(productName);
            if (productName == "Resistance") {
                cat = "RES";
            } else if (productName == "Capacitor") {
                cat = "CAP";
            } else if (productName == "Battery") {
                cat = "Battery";
            } else if (productName == "Diode") {
                cat = "Diode";
            } else if (productName == "IC") {
                cat = "IC";
            } else if (productName == "Jumpers") {
                cat = "Jumpers";
            } else if (productName == "Transistor") {
                cat = "Transistor";
            } else if (productName == "Test_Board") {
                cat = "TestBoard";
            } else if (productName == "Soldering_Iron") {
                cat = "SolderingIron";
            } else if (productName == "Multimeter") {
                cat = "Multimeter";
            } else if (productName == "Programmable_Board") {
                cat = "Kits";
            } else if (productName == "Switch") {
                cat = "Switch";
            }
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:store.db")) {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM product WHERE name LIKE ?");
                ps.setString(1, cat + "%-");
                ResultSet rs = ps.executeQuery();



            } catch (SQLException e) {
                e.printStackTrace();
            }
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
        Parent root = FXMLLoader.load(getClass().getResource("/user_gui/account_page.fxml"));
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