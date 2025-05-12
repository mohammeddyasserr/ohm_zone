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
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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
                String billId = String.valueOf(selectedBill.getBill_id());
                String imagePath = "Bills/" + billId + ".png";

                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    javafx.scene.image.Image image = new javafx.scene.image.Image(imageFile.toURI().toString());
                    ImageView imageView = new ImageView(image);

                    Stage popup = new Stage();
                    popup.setScene(new Scene(new StackPane(imageView), 845, 478));
                    popup.setTitle("Bill ID: " + billId);
                    popup.show();
                } else {
                    System.out.println("File not found for Bill ID: " + billId);
                }
            }
        });
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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
        Parent newRoot = FXMLLoader.load(getClass().getResource("/admin_gui/account.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(newRoot);
    }

    @FXML
    void adda_page(ActionEvent event) throws IOException {
        Parent newRoot = FXMLLoader.load(getClass().getResource("/admin_gui/add_admin.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(newRoot);

        System.out.println("Admin page button clicked");
    }

    @FXML
    void addp_page(ActionEvent event) throws IOException {
        Parent newRoot = FXMLLoader.load(getClass().getResource("/admin_gui/add_product.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(newRoot);
    }

    @FXML
    void editp_page(ActionEvent event) throws IOException {
        Parent newRoot = FXMLLoader.load(getClass().getResource("/admin_gui/edit_product.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(newRoot);
    }

    @FXML
    void home_page(ActionEvent event) throws IOException {
        Parent newRoot = FXMLLoader.load(getClass().getResource("/admin_gui/admin_main.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(newRoot);
    }

    @FXML
    void pills_page(ActionEvent event) throws IOException {
        Parent newRoot = FXMLLoader.load(getClass().getResource("/admin_gui/pills_admin.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(newRoot);
    }

    @FXML
    void removep_page(ActionEvent event) throws IOException {
        Parent newRoot = FXMLLoader.load(getClass().getResource("/admin_gui/remove_product.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(newRoot);
    }

    @FXML
    void login_page(ActionEvent event) throws IOException {
        Parent newRoot = FXMLLoader.load(getClass().getResource("/main_package/login.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(newRoot);
    }

    @FXML
    void toggle_menu(ActionEvent event) {
        boolean isVisible = side_panel.isVisible();
        side_panel.setVisible(!isVisible);
        //side_panel.setManaged(!isVisible);
    }
}