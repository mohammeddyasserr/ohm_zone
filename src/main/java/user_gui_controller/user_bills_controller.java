package user_gui_controller;

import db_edit_functions.product;
import javafx.animation.TranslateTransition;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import main_package.user_session;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import java.awt.*;
import java.io.File;
import java.util.HashMap;

import static javafx.scene.control.TableView.CONSTRAINED_RESIZE_POLICY;


public class user_bills_controller implements Initializable {


    @FXML
    private TableView<Bill> bills_table;

    @FXML
    private TableColumn<Bill, Integer> id;

    @FXML
    private TableColumn<Bill, String> date;

    @FXML
    private TableColumn<Bill, Double> price;

    private ObservableList<Bill> bills = FXCollections.observableArrayList();

    @FXML
    private AnchorPane left_menu;

    @FXML
    private ImageView logoImage;

    @FXML
    private Button menu_btn;

    @FXML
    private AnchorPane topBar;
    @FXML
    private ImageView logo;

    @FXML
    private ScrollPane main_scroll_pane;

    private boolean isMenuVisible = true;

    @FXML
    private Button account_btn;

    @FXML private Label cartCounter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        account_btn.setText(user_session.get_user());
        setupBillsColumns();
        loadBillsData();
        updateCartCount();
        bills_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setupBillsColumns() {
        id.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper(cellData.getValue().getId()));
        date.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDate()));
        price.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper(cellData.getValue().getPrice()));
    }

    private void loadBillsData() {
        bills.clear();
        String username = user_session.get_user();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:store.db");
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT \"bill_id\", \"order_date\", \"total_price\" FROM orders WHERE username = ?")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                bills.add(new Bill(
                        rs.getString("bill_id"),
                        rs.getString("order_date"),
                        rs.getString("total_price")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        bills_table.setItems(bills);
    }




    @FXML
    private void toggle_menu() {
        isMenuVisible = !isMenuVisible;

        if (isMenuVisible) {
            // Show menu with animation
            left_menu.setVisible(true);
            left_menu.setManaged(true);

            TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), left_menu);
            slideIn.setFromX(-left_menu.getWidth());
            slideIn.setToX(0);
            slideIn.play();

            AnchorPane.setLeftAnchor(main_scroll_pane, 165.0);
        } else {
            // Hide menu with animation
            TranslateTransition slideOut = new TranslateTransition(Duration.millis(300), left_menu);
            slideOut.setFromX(0);
            slideOut.setToX(-left_menu.getWidth());
            slideOut.setOnFinished(event -> {
                left_menu.setVisible(false);
                left_menu.setManaged(false);
            });
            slideOut.play();

            AnchorPane.setLeftAnchor(main_scroll_pane, 0.0);
        }
    }

    private void updateCartCount() {
        if (cartCounter != null) {
            int count = SharedCart.getTotalItemCount();
            cartCounter.setText("Your Cart (" + count + ")");
        }
    }

    @FXML
    void calculator_page(ActionEvent event) throws IOException {
        Parent newRoot = FXMLLoader.load(getClass().getResource("/user_gui/calc.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(newRoot);
    }

    @FXML
    void acount_page(ActionEvent event) throws IOException {
        Parent newRoot = FXMLLoader.load(getClass().getResource("/user_gui/account_page.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(newRoot);
    }

    @FXML
    void cart_page(ActionEvent event) throws IOException {
        Parent newRoot = FXMLLoader.load(getClass().getResource("/user_gui/cart.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(newRoot);
    }

    @FXML
    void home_page_user(ActionEvent event) throws IOException {
        Parent newRoot = FXMLLoader.load(getClass().getResource("/user_gui/user_main.fxml"));
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
    void pills_page_user(ActionEvent event) throws IOException {
        Parent newRoot = FXMLLoader.load(getClass().getResource("/user_gui/pills.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(newRoot);
    }

    @FXML
    void shop_page(ActionEvent event) throws IOException {
        Parent newRoot = FXMLLoader.load(getClass().getResource("/user_gui/shop.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(newRoot);
    }

    @FXML
    void rowClicked(MouseEvent event) {
            Bill selectedBill = bills_table.getSelectionModel().getSelectedItem();
            if (selectedBill != null) {
                String fileName = selectedBill.getId().toString() + ".png";
                File imageFile = new File("Bills", fileName); // Bills/<id>.png

                if (imageFile.exists()) {
                    javafx.scene.image.Image image = new javafx.scene.image.Image(imageFile.toURI().toString());
                    ImageView imageView = new ImageView(image);

                    Stage popup = new Stage();
                    popup.setScene(new Scene(new StackPane(imageView), 845, 478)); // Adjust size as needed
                    popup.setTitle("Bill ID: " + selectedBill.getId());
                    popup.show();
                } else {
                    System.out.println("File not found: " + imageFile.getAbsolutePath());
                }
            }
        }

    public class Bill {
        private final SimpleStringProperty id;
        private final SimpleStringProperty date;
        private final SimpleStringProperty price;

        public Bill(String id, String date, String price) {
            this.id = new SimpleStringProperty(id);
            this.date = new SimpleStringProperty(date);
            this.price = new SimpleStringProperty(price);
        }

        public String getId() { return id.get(); }
        public String getDate() { return date.get(); }
        public String getPrice() { return price.get(); }
    }
}