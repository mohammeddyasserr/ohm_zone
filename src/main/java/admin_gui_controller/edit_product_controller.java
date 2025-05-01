package admin_gui_controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import db_edit_functions.product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;



public class edit_product_controller implements Initializable {


    private product selectedProduct;

    private ObservableList<product> data = FXCollections.observableArrayList();

    @FXML
    private Pane side_panel;

    @FXML
    private Button menu_btn;

    @FXML
    private TableView<product> table;

    @FXML
    private TableColumn<product, Integer> id;

    @FXML
    private TableColumn<product, String> name;

    @FXML
    private TableColumn<product, Double> price;

    @FXML
    private TableColumn<product, Integer> quantity;


    @FXML
    private Button b1;

    @FXML
    private TextField id1;

    @FXML
    private TextField nameField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField quantityField;

    @FXML
    private Label price_error;

    @FXML
    private Label product_error;

    @FXML
    private Label quantity_error;

    @FXML
    private Label selection_error;

    @FXML
    private Label empty_error;

    @FXML
    void acount_page(ActionEvent event) {

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
    void edit_product(ActionEvent event) {
        price_error.setText("");
        quantity_error.setText("");
        selection_error.setText("");
        empty_error.setText("");
        product_error.setText("");

        if (selectedProduct == null) {
            selection_error.setText("Please select a product from the table");
            return;
        }

        try {
            String newName = nameField.getText().trim();
            String priceText = priceField.getText().trim();
            String quantityText = quantityField.getText().trim();

               // empty fields
            if (newName.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
                empty_error.setText("Empty Fields");
                return;
            }
                //check for existing product name
            if (!newName.equalsIgnoreCase(selectedProduct.getName())) {
                try {
                    if (isProductNameExists(newName)) {
                       product_error.setText("Product name already exists!");
                        return;
                    }
                } catch (SQLException e) {
                    product_error.setText("Error checking product name");
                    e.printStackTrace();
                    return;
                }
            }


            // Parse numerical values
            double newPrice = Double.parseDouble(priceText);
            int newQuantity = Integer.parseInt(quantityText);


            if (newPrice <= 0 ){
                price_error.setText("Price must be positive");
                return;
            }
            if (newQuantity < 0){
                quantity_error.setText("Quantity must be positive");
                return;
            }

            // 3. Database update
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:store.db")) {
                String sql = "UPDATE product SET name = ?, price = ?, quantity = ? WHERE id = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, newName);
                ps.setDouble(2, newPrice);
                ps.setInt(3, newQuantity);
                ps.setInt(4, selectedProduct.getId());
                ps.executeUpdate();


                refreshTableAndFields();

            } catch (SQLException e) {
                selection_error.setText("Update Failed");
                e.printStackTrace();
            }

        } catch (NumberFormatException e) {
            selection_error.setText("Please enter valid numbers");
        }
    }

    private void refreshTableAndFields() {
        // Refresh table data
        data.clear();
        showDataInTable();

        // Clear input fields
        nameField.clear();
        priceField.clear();
        quantityField.clear();
        selectedProduct = null;
    }


    @FXML
    void rowClicked(javafx.scene.input.MouseEvent mouseEvent) {
        // el id msh bybanan (donee)
        selectedProduct = table.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            id1.setText(String.valueOf(selectedProduct.getId()));
            nameField.setText(selectedProduct.getName());
            priceField.setText(String.valueOf(selectedProduct.getPrice()));
            quantityField.setText(String.valueOf(selectedProduct.getQuantity()));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
        showDataInTable();
    }

        private void loadData() {
            id.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
            name.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
            price.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPrice()));
            quantity.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getQuantity()));
}


    private ObservableList<product> showDataInTable() {
        //el data msh btzhar fe el table btzhar lma ba click 3leha bs
        data.clear();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:store.db")) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM product");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                data.add(new product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        table.setItems(data);
        return data;
    }

    @FXML
    void toggle_menu(ActionEvent event) {
        boolean isVisible = side_panel.isVisible();
        side_panel.setVisible(!isVisible);
    }
    private boolean isProductNameExists(String productName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM product WHERE LOWER(name) = LOWER(?) AND id != ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:store.db");
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, productName);
            ps.setInt(2, selectedProduct.getId());

            try (ResultSet rs = ps.executeQuery()) {
                return rs.getInt(1) > 0;
            }
        }
    }
    }