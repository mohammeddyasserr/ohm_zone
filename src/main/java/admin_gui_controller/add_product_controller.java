package admin_gui_controller;

import db_edit_functions.product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main_package.*;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class add_product_controller implements Initializable {
    public add_product_controller() {
        // Constructor افتراضي
    }
    int const_id;
    private String currentPrefix = "";

    private void enforceCategoryPrefix(String categoryPrefix) {
        name.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.startsWith(categoryPrefix + "-")) {
                // Re-add the prefix if the user tries to delete or modify it
                name.setText(categoryPrefix + "-");
                name.positionCaret(name.getText().length());
            }
        });
    }

    public void initialize(URL location, ResourceBundle resources) {
        if(category.getValue() == null) {
            name.setDisable(true);
        }
        if (category.getItems().isEmpty()) {
            category.getItems().addAll("RES", "CAP", "Battery");
        }

        category.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                currentPrefix = newVal + "-";
                name.setDisable(false);
                name.setText(currentPrefix);
                name.positionCaret(currentPrefix.length());
            }
        });
        name.textProperty().addListener((obs, oldText, newText) -> {
            if (!currentPrefix.isEmpty() && !newText.startsWith(currentPrefix)) {
                // Restore the prefix if it's removed or changed
                name.setText(currentPrefix);
                name.positionCaret(currentPrefix.length());
            }
        });
        account_btn.setText(user_session.get_user());

        try {
            Connection con = DriverManager.getConnection("jdbc:sqlite:store.db");
            System.out.println("connected");
            PreparedStatement ps = con.prepareStatement("SELECT id FROM product ORDER BY id DESC LIMIT 1");
            ResultSet r = ps.executeQuery();

            int const_id;
            if (r.next()) {
                const_id = r.getInt("id") + 1; // لو عايز id الجديد
            } else {
                const_id = 1;
            }

            id.setText(Integer.toString(const_id));

            r.close();
            ps.close();
            con.close();
        } catch(SQLException ee){
            System.out.print(ee.getMessage());
        }
    }

    @FXML
    private Button addbtn;

    @FXML
    private TextField id;

    @FXML
    private Button menu_btn;

    @FXML
    private TextField name;

    @FXML
    private Label name_error;

    @FXML
    private TextField price;

    @FXML
    private Label price_error;

    @FXML
    private TextField quantity;

    @FXML
    private Label quantity_error;

    @FXML
    private Pane side_panel;

    @FXML
    private Button account_btn;

    @FXML
    private ComboBox category;

    @FXML
    private Label cat_error;

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

    @FXML
    void add_product(ActionEvent event) {
        name_error.setText("");
        price_error.setText("");
        quantity_error.setText("");
        cat_error.setText("");

        try {
            Connection con2 = DriverManager.getConnection("jdbc:sqlite:store.db");

            PreparedStatement ps2 = con2.prepareStatement("select * from product where name=?");
            ps2.setString(1,name.getText());
            ResultSet r2 = ps2.executeQuery();
            if(category.getValue() == null){
                cat_error.setText("please choose a category");
            }
            else if (name.getText().trim().equals(currentPrefix)) {
                name_error.setText("you must complete the product name");
            }
            else if (r2.next()) {
                name_error.setText("there is another product with this name");
            }
            else if (price.getText().isEmpty()){
                price_error.setText("price can't be empty");
            }
            else if(price.getText().matches(".*[a-zA-Z]+.*")){
                price_error.setText("Must be digits only");
            }else if (Double.parseDouble(price.getText().trim()) == 0) {
                price_error.setText("Price can't be zero");
            }else if (quantity.getText().isEmpty()) {
                quantity_error.setText("quantity can't be empty");
            } else if (!quantity.getText().matches("\\d+")) {
                quantity_error.setText("Must be an integer value.");
            } else if (Integer.parseInt(quantity.getText().trim()) == 0) {
                quantity_error.setText("quantity can't be zero");
            }else{
                product.addToDatabase(
                        con2,
                        Integer.parseInt(id.getText().trim()),
                        name.getText(),
                        Double.parseDouble(price.getText().trim()),
                        Integer.parseInt(quantity.getText().trim())
                );
                // Clear all fields
                name.clear();
                price.clear();
                quantity.clear();
                category.getSelectionModel().clearSelection();

// Disable name field again
                name.setDisable(true);

// Clear any stored prefix
                currentPrefix = "";

// Reset the prompt (optional)
                name.setPromptText("Enter product name");
                category.setPromptText("Category");

            }
            con2.close();
            r2.close();
            ps2.close();

            initialize(null, null);
        }
        catch(SQLException ee){
            System.out.print(ee.getMessage());
        }
    }
}