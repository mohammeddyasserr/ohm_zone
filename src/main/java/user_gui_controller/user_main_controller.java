package user_gui_controller;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class user_main_controller {

    @FXML
    private Pane left_menu;

    @FXML
    private ScrollPane main_scroll_pane;

    private boolean isMenuVisible = true;

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