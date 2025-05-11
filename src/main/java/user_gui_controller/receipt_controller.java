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
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.media.Media;

import javafx.event.ActionEvent;
import main_package.user_session;
import javafx.scene.control.Label;

import javafx.scene.image.ImageView;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
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
    private Button account_btn;

    @FXML
    private Label cartCounter;

    @FXML
    private MediaView checkmark;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        checkmark();
        if (account_btn != null) {
            account_btn.setText(user_session.get_user());
        }
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