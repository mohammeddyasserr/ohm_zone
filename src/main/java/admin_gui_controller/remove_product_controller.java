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
import main_package.user_session;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;



public class remove_product_controller implements Initializable {


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
    private Label selection_error;

    @FXML
    private Button account_btn;


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
    void remove_product(ActionEvent event) {
        selection_error.setText(""); // Reset any previous error message

        if (selectedProduct == null) {
            selection_error.setText("Please select a product from the table");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delete this product?");
        alert.setContentText("This action cannot be undone.");

        ButtonType deleteButton = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(deleteButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == deleteButton) {
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:store.db")) {
                String sql = "DELETE FROM product WHERE id = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, selectedProduct.getId());

                int affectedRows = ps.executeUpdate();

                if (affectedRows == 0) {
                    showAlert("Error", "Delete Failed", "No products were deleted.");
                    return;
                }

                refreshTableAndFields();
                Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
                alert2.setTitle("SUCCESS");
                alert2.setHeaderText("Delete Successful");
                alert2.setContentText("Product Deleted Successfully");
                alert2.setGraphic(null);
                alert2.showAndWait();

            }
            catch (SQLException e) {
                showAlert("Database Error", "Delete Failed", "Error: " + e.getMessage());
                e.printStackTrace();
            }
        }

    }

    private void refreshTableAndFields() {
        data.clear(); // Clear the table data
        showDataInTable(); // Reload the data into the table

        // Reset the selected product after deletion
        selectedProduct = null;
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void rowClicked(javafx.scene.input.MouseEvent mouseEvent) {
        product clickedProduct = table.getSelectionModel().getSelectedItem();

        if (selectedProduct != null && selectedProduct.equals(clickedProduct)) {

            table.getSelectionModel().clearSelection();
            selectedProduct = null;
        }
        else {
            selectedProduct = clickedProduct;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        account_btn.setText(user_session.get_user());
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



}