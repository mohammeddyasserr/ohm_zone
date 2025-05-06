package admin_gui_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main_package.user_session;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class admin_main_controller implements Initializable {

    @FXML
    private Pane side_panel;

    @FXML
    private Button menu_btn;

    @FXML
    private Button account_btn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        account_btn.setText(user_session.get_user());
    }

    @FXML
    void acount_page(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/admin_gui/account.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void adda_page(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/admin_gui/add_admin.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void addp_page(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/admin_gui/add_product.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void editp_page(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/admin_gui/edit_product.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void home_page(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/admin_gui/admin_main.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void pills_page(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/admin_gui/pills_admin.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void removep_page(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/admin_gui/remove_product.fxml"));
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
    void toggle_menu(ActionEvent event) {
        boolean isVisible=side_panel.isVisible();
        side_panel.setVisible(!isVisible);
        //side_panel.setManaged(!isVisible);
    }

}
