package admin_gui_controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import db_edit_functions.product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import org.w3c.dom.events.MouseEvent;

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
    private TextField id1;

    @FXML
    private TextField nameField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField quantityField;


    @FXML
    void edit_product(ActionEvent event) {
        if (selectedProduct == null) return;

        String newName = nameField.getText();
        double newPrice = Double.parseDouble(priceField.getText());
        int newQuantity = Integer.parseInt(quantityField.getText());

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:store.db")) {
            String sql = "UPDATE product SET name = ?, price = ?, quantity = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newName);
            ps.setDouble(2, newPrice);
            ps.setInt(3, newQuantity);
            ps.setInt(4, selectedProduct.getId());
            ps.executeUpdate();

            // Refresh table
            showDataInTable();

            // Optional: clear fields
            nameField.clear();
            priceField.clear();
            quantityField.clear();
            selectedProduct = null;

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void rowClicked(javafx.scene.input.MouseEvent mouseEvent) {
        selectedProduct = table.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
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
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }

    private void showDataInTable() {
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
    }

    @FXML
    void toggle_menu(ActionEvent event) {
        boolean isVisible = side_panel.isVisible();
        side_panel.setVisible(!isVisible);
    }


}
