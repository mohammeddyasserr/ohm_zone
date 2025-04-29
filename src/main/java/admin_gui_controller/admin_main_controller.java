package admin_gui_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class admin_main_controller {

    @FXML
    private Pane side_panel;

    @FXML
    private Button menu_btn;

    @FXML
    void toggle_menu(ActionEvent event) {
        boolean isVisible=side_panel.isVisible();
        side_panel.setVisible(!isVisible);
        //side_panel.setManaged(!isVisible);
    }

}
