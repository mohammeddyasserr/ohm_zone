package user_gui_controller;
import javafx.animation.FadeTransition;
import javafx.collections.ListChangeListener;
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
import java.sql.Date;
import javafx.scene.control.Label;

import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
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

    @FXML
    private Label quantity_error;

    @FXML
    private Label cartCounter;

    @FXML private TableView<HashMap<String, Object>> table;
    @FXML private TableColumn<HashMap<String, Object>, String> name;
    @FXML private TableColumn<HashMap<String, Object>, Double> price;
    @FXML private TableColumn<HashMap<String, Object>, Integer> quantity;
    @FXML private TableColumn<HashMap<String, Object>, Double> total_price;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        account_btn.setText(user_session.get_user());
        setupTable();
        updateCartCount(); // initial load
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        SharedCart.cartItems.addListener((ListChangeListener<? super HashMap<String, Object>>) change -> {
            updateCartCount(); // auto update on add/remove
        });
    }

    private void updateCartCount() {
        if (cartCounter != null) {
            int count = SharedCart.getTotalItemCount();
            cartCounter.setText("Your Cart (" + count + ")");
        }
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
    private Label checkout_error;

    @FXML
    private Label checkout;




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

    @FXML
    void checkout(ActionEvent event) throws IOException {

        if (SharedCart.cartItems.isEmpty()) {
            showMessage(checkout_error,"Cart is empty!","red");
            return;
        }

        Connection conn = null;

        try {
            conn = DriverManager.getConnection("jdbc:sqlite:store.db");
            conn.setAutoCommit(false);

            double total = SharedCart.cartItems.stream()
                    .mapToDouble(item -> (double) item.get("total"))
                    .sum();

            boolean allAvailable = true;
            for (HashMap<String, Object> item : SharedCart.cartItems) {
                String selectSQL = "SELECT quantity FROM product WHERE name = ?";
                try (PreparedStatement check = conn.prepareStatement(selectSQL)) {
                    check.setString(1, (String) item.get("name"));
                    try (ResultSet rs = check.executeQuery()) {
                        if (rs.next()) {
                            int dbQuantity = rs.getInt("quantity");
                            int requestedQuantity = (int) item.get("quantity");

                            if (requestedQuantity > dbQuantity) {
                                showMessage(quantity_error,"Not enough quantity for: " + item.get("name"),"red");
                                allAvailable = false;
                                break;
                            }
                        }
                    }
                }
            }

            if (allAvailable) {
                ObservableList<HashMap<String, Object>> tempCartItems =
                        FXCollections.observableArrayList(SharedCart.cartItems);

                for (HashMap<String, Object> item : SharedCart.cartItems) {
                    String updateSQL = "UPDATE product SET quantity = quantity - ? WHERE name = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
                        pstmt.setInt(1, (int) item.get("quantity"));
                        pstmt.setString(2, (String) item.get("name"));
                        pstmt.executeUpdate();
                    }
                }

                LocalDate today = LocalDate.now();
                String dateStr = today.toString();
                String insertOrderSQL = "INSERT INTO orders (username, total_price, order_date) VALUES (?, ?, ?)";
                try (PreparedStatement orderStmt = conn.prepareStatement(insertOrderSQL)) {
                    orderStmt.setString(1, user_session.get_user());
                    orderStmt.setDouble(2, total);
                    orderStmt.setString(3, dateStr);
                    orderStmt.executeUpdate();
                }

                conn.commit();

                SharedCart.cartItems.clear();
                showMessage(checkout,"Checkout completed successfully!","darkgreen");
                checkout_error.setText("");

                // Pass cart items to receipt page
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/user_gui/receipt.fxml"));
                Parent newRoot = loader.load();
                receipt_controller controller = loader.getController();
                controller.setCartItems(tempCartItems); // <-- pass preserved cart

                Scene scene = ((Node) event.getSource()).getScene();
                scene.setRoot(newRoot);
            }

        } catch (SQLException e) {
            showMessage(checkout_error,"Error during checkout","red");
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                checkout_error.setText(checkout_error.getText() + " Rollback failed: " + ex.getMessage());
            }
        } finally {
            try {
                if (conn != null) conn.close();
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
            private final Spinner<Integer> spinner = new Spinner<>(1, 1000, 1);
            {
                spinner.setStyle("-fx-text-fill: white; -fx-background-color: transparent ;");
                spinner.getEditor().setStyle("-fx-text-fill: white; -fx-background-color: transparent;");
            }
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

    private void showMessage(Label label, String message, String color) {
        label.setText(message);
        label.setStyle("-fx-font-size: 12px; -fx-text-fill: " + color + ";");
        label.setVisible(true);
        label.setManaged(true);
        label.setOpacity(1.0);

        FadeTransition fade = new FadeTransition(Duration.seconds(2), label);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setDelay(Duration.seconds(1));
        fade.setOnFinished(e -> {
            label.setVisible(false);
            label.setManaged(false);
        });
        fade.play();
    }

}
