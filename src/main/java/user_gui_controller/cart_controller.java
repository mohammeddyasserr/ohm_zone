package user_gui_controller;
import user_gui_controller.SharedCart;

import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import main_package.user_session;

import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.ResourceBundle;

public class cart_controller implements Initializable {


    @FXML
    private ImageView logo;

    @FXML
    private AnchorPane left_menu;

    @FXML
    private ScrollPane main_scroll_pane;

    private boolean isMenuVisible = true;

    @FXML
    private Button account_btn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        account_btn.setText(user_session.get_user());
        setupTable();
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

    @FXML
    void calculator_page(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/user_gui/calc.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void acount_page(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/user_gui/account_page.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void cart_page(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/user_gui/cart.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void home_page_user(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/user_gui/user_main.fxml"));
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
    void pills_page_user(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/admin_gui/pills.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void shop_page(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/user_gui/shop.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    private Label checkout_error;

    @FXML
    private Label checkout;

    @FXML private TableView<HashMap<String, Object>> table;
    @FXML private TableColumn<HashMap<String, Object>, String> name;
    @FXML private TableColumn<HashMap<String, Object>, Double> price;
    @FXML private TableColumn<HashMap<String, Object>, Integer> quantity;
    @FXML private TableColumn<HashMap<String, Object>, Double> total_price;


    @FXML
    void rowClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            HashMap<String, Object> selected = (HashMap<String, Object>) table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                SharedCart.cartItems.remove(selected);
            }
        }

    }

    @FXML
    private Label label2;

    @FXML
    private Button b1;

//    @FXML
//    void checkout(ActionEvent event) {
//        checkout.setText("");
//        checkout_error.setText("");
//
//        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:store.db")) {
//            conn.setAutoCommit(false);
//
//            for (HashMap<String, Object> item : SharedCart.cartItems) {
//                String updateSQL = "UPDATE product SET quantity = quantity - ? WHERE name = ?";
//                try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
//                    pstmt.setInt(1, (int) item.get("quantity"));
//                    pstmt.setString(2, (String) item.get("name"));
//                    pstmt.executeUpdate();
//                }
//            }
//
//            conn.commit();
//            SharedCart.cartItems.clear();
//            checkout.setText("Checkout completed successfully!");
//        } catch (SQLException e) {
//            checkout_error.setText("Error during checkout");
//        }
//
//    }

    @FXML
    void checkout(ActionEvent event) {
        checkout.setText("");
        checkout_error.setText("");
        Connection conn = null; // Declare outside try-with-resources

        try {
            conn = DriverManager.getConnection("jdbc:sqlite:store.db");
            conn.setAutoCommit(false);

            // 1. Calculate total price
            double total = SharedCart.cartItems.stream()
                    .mapToDouble(item -> (double) item.get("total"))
                    .sum();

            // 2. Update product quantities
            for (HashMap<String, Object> item : SharedCart.cartItems) {
                String updateSQL = "UPDATE product SET quantity = quantity - ? WHERE name = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
                    pstmt.setInt(1, (int) item.get("quantity"));
                    pstmt.setString(2, (String) item.get("name"));
                    pstmt.executeUpdate();
                }
            }

            // 3. Insert order record
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String insertOrderSQL = "INSERT INTO orders (username, total_price, order_date) VALUES (?, ?, ?)";
            try (PreparedStatement orderStmt = conn.prepareStatement(insertOrderSQL)) {
                orderStmt.setString(1, user_session.get_user());
                orderStmt.setDouble(2, total);
                orderStmt.setTimestamp(3, timestamp);
                orderStmt.executeUpdate();
            }

            conn.commit();
            SharedCart.cartItems.clear();
            checkout.setText("Checkout completed successfully!");

        } catch (SQLException e) {
            checkout_error.setText("Error during checkout");
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                checkout_error.setText(checkout_error.getText() + " Rollback failed: " + ex.getMessage());
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    private void setupTable() {
        // Configure column bindings
        name.setCellValueFactory(data ->
                new SimpleStringProperty((String) data.getValue().get("name")));

        price.setCellValueFactory(data ->
                new SimpleDoubleProperty((Double) data.getValue().get("price")).asObject());

        quantity.setCellValueFactory(data ->
                new SimpleIntegerProperty((Integer) data.getValue().get("quantity")).asObject());

        total_price.setCellValueFactory(data ->
                new SimpleDoubleProperty((Double) data.getValue().get("total")).asObject());
        // Add quantity spinners
        quantity.setCellFactory(tc -> new TableCell<>() {
            private final Spinner<Integer> spinner = new Spinner<>(1, 100, 1);

            @Override
            protected void updateItem(Integer value, boolean empty) {
                super.updateItem(value, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    spinner.getValueFactory().setValue(value);
                    spinner.valueProperty().addListener((obs, oldVal, newVal) -> {
                        HashMap<String, Object> item = getTableView().getItems().get(getIndex());
                        updateItemQuantity(item, newVal);
                    });
                    setGraphic(spinner);
                }
            }
        });

        table.setItems(SharedCart.cartItems);
    }

    private void updateItemQuantity(HashMap<String, Object> item, int newQuantity) {
        double price = (double) item.get("price");
        item.put("quantity", newQuantity);
        item.put("total", price * newQuantity);
        table.refresh();
    }

    public void addItemToCart(String itemName, double price, int quantity) {
        for (HashMap<String, Object> item : SharedCart.cartItems) {
            if (item.get("name").equals(itemName)) {
                int currentQty = (int) item.get("quantity");
                int newQty = currentQty + quantity;
                updateItemQuantity(item, newQty);
                return;
            }
        }

        // Add new item if not found
        HashMap<String, Object> newItem = new HashMap<>();
        newItem.put("name", itemName);
        newItem.put("price", price);
        newItem.put("quantity", quantity);
        newItem.put("total", price * quantity);
        SharedCart.cartItems.add(newItem);
    }





}
