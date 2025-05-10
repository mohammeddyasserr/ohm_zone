package user_gui_controller;

import db_edit_functions.product;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import main_package.user_session;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import user_gui_controller.SharedCart;
import user_gui_controller.cart_controller;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class shop_controller implements Initializable {

    @FXML private ImageView logo;
    @FXML private AnchorPane left_menu;
    @FXML private Button account_btn;
    @FXML private GridPane productGrid;
    @FXML private AnchorPane detailsPane;
    @FXML private ImageView detailsImage;
    @FXML private Label detailsName;
    @FXML private Label detailsPrice;
    @FXML private ComboBox<String> options;
    @FXML private TextField quantity0;
    @FXML private VBox Resistance, Capacitor, Battery, Diode, ICS, Jumpers, Transistor, Test_Board, Soldering_Iron, Avo, Boards, Switch;
    @FXML private ColumnConstraints col1;
    @FXML private ColumnConstraints col2;
    @FXML private ColumnConstraints col3;
    @FXML private ColumnConstraints col4;
    @FXML private Button closeDetailsBtn;
    @FXML private Text detailsDescription;
    @FXML private Label quantityLabel;
    @FXML private Button cart_btn;
    @FXML private Label quantityErrorLabel;
    @FXML private Label successLabel;


    private boolean isMenuVisible = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        account_btn.setText(user_session.get_user());
        Resistance.setUserData("Resistance");
        Capacitor.setUserData("Capacitor");
        Battery.setUserData("Battery");
        Diode.setUserData("Diode");
        ICS.setUserData("IC");
        Jumpers.setUserData("Jumpers");
        Transistor.setUserData("Transistor");
        Test_Board.setUserData("Test Board");
        Soldering_Iron.setUserData("Soldering Iron");
        Avo.setUserData("Multimeter");
        Boards.setUserData("Programmable Boards");
        Switch.setUserData("Switch");
        options.setCellFactory(listView -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: black; -fx-background-color: transparent;");
                }
            }
        });

        options.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: silver; -fx-background-color: transparent;");
                }
            }
        });
        quantity0.textProperty().addListener((obs, oldVal, newVal) -> quantityErrorLabel.setText(""));
         }

    private void showDetailsPane() {
        detailsPane.setVisible(true);

        // Shrink the product cards dynamically (width and height)
        resizeProductCards(0.22, 0.5);  // Shrink to 22% width and 50% height

        // Slide the details pane in
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), detailsPane);
        slideIn.setToX(0);
        slideIn.play();

        // Animate product grid items (fade and scale)
        animateProductGrid(0.8, 0.5);  // Shrink and fade the product cards
    }

    @FXML
    private void closeDetailsPane() {
        TranslateTransition slideOut = new TranslateTransition(Duration.millis(300), detailsPane);
        slideOut.setToX(300);  // Slide the details pane out of view
        slideOut.setOnFinished(e -> {
            detailsPane.setVisible(false);
            detailsPane.setTranslateX(300);  // Reset position

            // Restore product cards to full size
            resizeProductCards(0.25, 1.0);  // Restore to 25% width and 100% height
            animateProductGrid(1.0, 1.0);  // Restore size and opacity
        });
        slideOut.play();
    }

    private void animateProductGrid(double scaleTo, double opacityTo) {
        for (Node node : productGrid.getChildren()) {
            if (node instanceof VBox) {
                // Fade Transition
                FadeTransition fade = new FadeTransition(Duration.millis(300), node);
                fade.setToValue(opacityTo);  // Fade out/in
                fade.play();

                // Scale Transition (shrink/grow size)
                ScaleTransition scale = new ScaleTransition(Duration.millis(300), node);
                scale.setToX(scaleTo);  // Shrink/grow horizontally
                scale.setToY(scaleTo);  // Shrink/grow vertically
                scale.play();
            }
        }
    }

    private void resizeProductCards(double widthPercent, double heightPercent) {
        double gridWidth = productGrid.getWidth();
        double gridHeight = productGrid.getHeight();

        double targetWidth = gridWidth * widthPercent;   // Shrink to this width
        double targetHeight = gridHeight * heightPercent; // Shrink to this height

        for (Node node : productGrid.getChildren()) {
            if (node instanceof VBox vbox) {
                vbox.setPrefWidth(targetWidth);
                vbox.setPrefHeight(targetHeight);  // Shrink height as well
            }
        }
    }

    @FXML
    private void onMouseEnterClose() {
        Glow glow = new Glow();
        glow.setLevel(1.0);  // Set the glow intensity (0.0 to 1.0)
        closeDetailsBtn.setEffect(glow);  // Apply glow effect
    }

    @FXML
    private void onMouseExitClose() {
        closeDetailsBtn.setEffect(null);  // Remove the glow effect
    }

    @FXML
    private void onProductClick(MouseEvent event) {
        VBox clickedBox = (VBox) event.getSource();
        String productName = (String) clickedBox.getUserData();
        detailsName.setText(productName);

        ImageView clickedImage = (ImageView) clickedBox.getChildren().get(0);
        detailsImage.setImage(clickedImage.getImage());

        Map<String, String> productCategoryMap = Map.ofEntries(
                Map.entry("Resistance", "RES"),
                Map.entry("Capacitor", "CAP"),
                Map.entry("Battery", "Battery"),
                Map.entry("Diode", "Diode"),
                Map.entry("IC", "IC"),
                Map.entry("Jumpers", "Jumpers"),
                Map.entry("Transistor", "Transistor"),
                Map.entry("Test Board", "TestBoard"),
                Map.entry("Soldering Iron", "SolderingIron"),
                Map.entry("Multimeter", "Multimeter"),
                Map.entry("Programmable Boards", "Kits"),
                Map.entry("Switch", "Switch")
        );

        String cat = productCategoryMap.get(productName);
        if (cat == null) return;

        options.getItems().clear();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:store.db")) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM product WHERE name LIKE ?");
            ps.setString(1, cat + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String fullName = rs.getString("name");   // e.g., RES-1000ohm
                String displayName = fullName.replaceFirst(cat + "-", ""); // Remove RES- part
                options.getItems().add(displayName);
            }

            if (!options.getItems().isEmpty()) {
                cart_btn.setDisable(false);
                options.getSelectionModel().selectFirst();  // Select the first item by default
                updateDetailsForValue(cat, options.getValue());
                showDetailsPane(); // Only show details if we have something to show
            } else {
                options.setPromptText("No items available");
                detailsName.setText(productName);
                detailsPrice.setText("");
                quantityLabel.setText("");
                detailsDescription.setText("No available products for this category.");
                cart_btn.setDisable(true);
                showDetailsPane();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private void updateDetailsForValue(String catPrefix, String selectedValue) {
        String fullProductName = catPrefix + "-" + selectedValue;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:store.db")) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM product WHERE name = ?");
            ps.setString(1, fullProductName);
            ResultSet rs = ps.executeQuery();


            if (rs.next()) {
                String price = rs.getString("Price");
                String quantity = rs.getString("Quantity");

                detailsPrice.setText(price + " EGP");
                quantityLabel.setText("Available in stock: " + (quantity != null ? quantity : "0"));
                detailsDescription.setText(detailsName.getText() + " " + selectedValue);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onOptionSelected() {
        String selectedValue = options.getValue();
        if (selectedValue == null) return;

        String productType = detailsName.getText(); // e.g., "Resistance"
        String productPrefix = getProductPrefix(productType); // e.g., "RES"

        if (productPrefix != null) {
            updateDetailsForValue(productPrefix, selectedValue);
        }
    }

    private String getProductPrefix(String productName) {
        Map<String, String> productCategoryMap = Map.ofEntries(
                Map.entry("Resistance", "RES"),
                Map.entry("Capacitor", "CAP"),
                Map.entry("Battery", "Battery"),
                Map.entry("Diode", "Diode"),
                Map.entry("IC", "IC"),
                Map.entry("Jumpers", "Jumpers"),
                Map.entry("Transistor", "Transistor"),
                Map.entry("Test Board", "TestBoard"),
                Map.entry("Soldering Iron", "SolderingIron"),
                Map.entry("Multimeter", "Multimeter"),
                Map.entry("Programmable Boards", "Kits"),
                Map.entry("Switch", "Switch")
        );
        return productCategoryMap.get(productName);
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

    @FXML
    void addtocart(ActionEvent event) {
        String selectedValue = options.getValue();
        String productType = detailsName.getText();
        String productPrefix = getProductPrefix(productType);

        String fullName = productPrefix + "-" + selectedValue;
        String quantityText = quantity0.getText().trim();
        String price = detailsPrice.getText(); // e.g. "50.75 EGP"
        String amount = price.replaceAll("[^\\d.]", ""); // Keep only digits and dot

        if (quantityText.isEmpty()) {
            showMessage(quantityErrorLabel, "Enter quantity", "red");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityText);
            if (quantity <= 0) {
                showMessage(quantityErrorLabel, "Enter valid quantity", "red");
                return;
            }

            SharedCart.addItem(fullName, Integer.parseInt(amount), quantity);
            showMessage(successLabel, "Product added to cart successfully!", "lightgreen");

        } catch (NumberFormatException e) {
            showMessage(quantityErrorLabel, "Enter numeric value", "red");
        }
    }


    @FXML
    private void toggle_menu() {
        isMenuVisible = !isMenuVisible;

        if (isMenuVisible) {
            left_menu.setVisible(true);
            left_menu.setManaged(true);

            TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), left_menu);
            slideIn.setFromX(-left_menu.getWidth());
            slideIn.setToX(0);
            slideIn.play();

        } else {
            TranslateTransition slideOut = new TranslateTransition(Duration.millis(300), left_menu);
            slideOut.setFromX(0);
            slideOut.setToX(-left_menu.getWidth());
            slideOut.setOnFinished(event -> {
                left_menu.setVisible(false);
                left_menu.setManaged(false);
            });
            slideOut.play();

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
        Parent root = FXMLLoader.load(getClass().getResource("/user_gui/pills.fxml"));
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
}
