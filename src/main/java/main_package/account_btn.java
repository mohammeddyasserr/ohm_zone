package main_package;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public abstract class account_btn {
    @FXML
    protected Button account_btn;

    @FXML
    protected void initialize() {
        if (account_btn != null) {
            account_btn.setText(user_session.get_user());
        }
    }
}
