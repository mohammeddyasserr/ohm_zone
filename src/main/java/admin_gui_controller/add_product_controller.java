package admin_gui_controller;

import db_edit_functions.product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class add_product_controller implements Initializable {
    public add_product_controller() {
        // Constructor افتراضي
    }
    int const_id;


    public void initialize(URL location, ResourceBundle resources) {

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

        try {
            Connection con2 = DriverManager.getConnection("jdbc:sqlite:store.db");

            PreparedStatement ps2 = con2.prepareStatement("select * from product where name=?");
            ps2.setString(1,name.getText());
            ResultSet r2 = ps2.executeQuery();
            if(name.getText().isEmpty()){
                name_error.setText("name can't be empty");
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
            }if (quantity.getText().isEmpty()) {
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
                name.setText("");
                price.setText("");
                quantity.setText("");
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