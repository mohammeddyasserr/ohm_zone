package user_gui_controller;

import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.media.Media;
import user_gui_controller.SharedCart;



import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.event.ActionEvent;
import main_package.user_session;

import javafx.scene.image.ImageView;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;



public class receipt_controller implements Initializable {



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
    private MediaView checkmark;

    @FXML
    private Label date;

    @FXML
    private Button back;



    @FXML
    private Label id;

    @FXML
    private ImageView logoImage;

    @FXML
    private ImageView logoImage1;

    @FXML private TableView<HashMap<String, Object>> table;
    @FXML private TableColumn<HashMap<String, Object>, String> name;
    @FXML private TableColumn<HashMap<String, Object>, Double> price;
    @FXML private TableColumn<HashMap<String, Object>, Double> total;
    @FXML private TableColumn<HashMap<String, Object>, Integer> quantity;


    @FXML
    private AnchorPane topBar;

    @FXML
    private Label username;

    @FXML
    private Label total_price;

    @FXML
    void back_btn(ActionEvent event) throws IOException {
        Parent newRoot = FXMLLoader.load(getClass().getResource("/user_gui/user_main.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(newRoot);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        checkmark();
        updateCartCount(); // initial load

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = currentDate.format(formatter);

        date.setText(formattedDate);
        username.setText(user_session.get_user());

        try {
            Connection con = DriverManager.getConnection("jdbc:sqlite:store.db");

            PreparedStatement ps = con.prepareStatement("SELECT bill_id FROM orders ORDER BY bill_id DESC LIMIT 1");
            ResultSet r = ps.executeQuery();

            if (r.next()) {
                id.setText(Integer.toString(r.getInt("bill_id")));

            }
        } catch(SQLException ee){
            System.out.print(ee.getMessage());
        }

        try {
            Connection con = DriverManager.getConnection("jdbc:sqlite:store.db");

            PreparedStatement ps = con.prepareStatement("SELECT total_price FROM orders ORDER BY bill_id DESC LIMIT 1");
            ResultSet r = ps.executeQuery();

            if (r.next()) {
                total_price.setText(Integer.toString(r.getInt("total_price")));

            }
        } catch(SQLException ee){
            System.out.print(ee.getMessage());
        }

        name.setCellValueFactory(data ->
                new SimpleStringProperty((String) data.getValue().get("name")));

        price.setCellValueFactory(data ->
                new SimpleDoubleProperty((Double) data.getValue().get("price")).asObject());

        quantity.setCellValueFactory(data ->
                new SimpleIntegerProperty((Integer) data.getValue().get("quantity")).asObject());

        total.setCellValueFactory(data ->
                new SimpleDoubleProperty((Double) data.getValue().get("total")).asObject());


        SharedCart.cartItems.addListener((ListChangeListener<? super HashMap<String, Object>>) change -> {
            updateCartCount(); // auto update on add/remove
        });
        table.setItems(SharedCart.cartItems);
    }

    private void updateCartCount() {
        if (cartCounter != null) {
            int count = SharedCart.getTotalItemCount();
            cartCounter.setText("Your Cart (" + count + ")");
        }
    }

    public void checkmark() {
        String path = getClass().getResource("/videos/checkmark.mp4").toExternalForm();
        Media media = new Media(path);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        checkmark.setMediaPlayer(mediaPlayer);

        mediaPlayer.play();
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.pause();
            mediaPlayer.seek(mediaPlayer.getTotalDuration());
        });
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
}