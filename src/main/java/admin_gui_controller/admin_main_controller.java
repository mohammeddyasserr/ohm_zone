package admin_gui_controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class admin_main_controller {
    @FXML
    private AnchorPane sidepanel;
    @FXML
    private Button menubutton;

    @FXML
    private void togglesidepanel() {
        boolean isVisible = sidepanel.isVisible();
        sidepanel.setVisible(!isVisible);
        sidepanel.setManaged(!isVisible); // hides layout space when invisible
    }
}
