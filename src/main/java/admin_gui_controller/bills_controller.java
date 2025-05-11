package admin_gui_controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import db_edit_functions.adminbills;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main_package.user_session;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class bills_controller implements Initializable {

    @FXML
    private Pane side_panel;

    @FXML
    private Button menu_btn;

    @FXML
    private Button account_btn;

    @FXML
    private Button get_pdf;

    @FXML
    private TableView<adminbills> table;

    @FXML
    private TableColumn<adminbills, Integer> bill_id;

    @FXML
    private TableColumn<adminbills, String> date;

    @FXML
    private TableColumn<adminbills, Double> total_price;

    @FXML
    private TableColumn<adminbills, String> user_name;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        account_btn.setText(user_session.get_user());

        bill_id.setCellValueFactory(new PropertyValueFactory<>("Bill_id"));
        date.setCellValueFactory(new PropertyValueFactory<>("Date"));
        total_price.setCellValueFactory(new PropertyValueFactory<>("Total_price"));
        user_name.setCellValueFactory(new PropertyValueFactory<>("Username"));

        loadBillsFromDatabase();

        // Handle row click to open image for admin
        table.setOnMouseClicked(event -> {
            adminbills selectedBill = table.getSelectionModel().getSelectedItem();
            if (selectedBill != null) {
                String billId = String.valueOf(selectedBill.getBill_id()); // Use getter from adminbills
                String imagePath = "Bills/" + billId + ".png"; // Adjust path as needed
                try {
                    File imageFile = new File(imagePath);
                    if (imageFile.exists()) {
                        Desktop.getDesktop().open(imageFile);
                    } else {
                        System.out.println("File not found for Bill ID: " + billId);
                    }
                } catch (IOException e) {
                    System.out.println("Error opening image for Bill ID " + billId + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadBillsFromDatabase() {
        ObservableList<adminbills> bills = FXCollections.observableArrayList();
        String query = "SELECT * FROM orders";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:store.db");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int billId = rs.getInt("bill_id");
                String date = rs.getString("order_date");
                double total = rs.getDouble("total_price");
                String username = rs.getString("username");

                bills.add(new adminbills(billId, date, total, username));
            }

            table.setItems(bills);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void acount_page(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/admin_gui/account.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void adda_page(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/admin_gui/add_admin.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void addp_page(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/admin_gui/add_product.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void editp_page(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/admin_gui/edit_product.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void home_page(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/admin_gui/admin_main.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void pills_page(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/admin_gui/pills_admin.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void removep_page(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/admin_gui/remove_product.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void login_page(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/main_package/login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void toggle_menu(ActionEvent event) {
        boolean isVisible = side_panel.isVisible();
        side_panel.setVisible(!isVisible);
        //side_panel.setManaged(!isVisible);
    }
}