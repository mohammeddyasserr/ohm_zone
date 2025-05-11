package user_gui_controller;

import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import main_package.user_session;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.ResourceBundle;

public class receipt_controller implements Initializable {

    @FXML private Label id, username, total_price, date, cartCounter;
    @FXML private ImageView logo, logoImage, logoImage1, screenshotView;
    @FXML private AnchorPane receipt, left_menu, topBar;
    @FXML private ScrollPane main_scroll_pane;
    @FXML private MediaView checkmark;
    @FXML private Button back;

    @FXML private TableView<HashMap<String, Object>> table;
    @FXML private TableColumn<HashMap<String, Object>, String> name;
    @FXML private TableColumn<HashMap<String, Object>, Double> price, total;
    @FXML private TableColumn<HashMap<String, Object>, Integer> quantity;

    private ObservableList<HashMap<String, Object>> receivedCartItems = FXCollections.observableArrayList();
    private boolean isMenuVisible = true;

    public void setCartItems(ObservableList<HashMap<String, Object>> items) {
        this.receivedCartItems = items;
        table.setItems(receivedCartItems);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        username.setText(user_session.get_user());
        date.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        updateOrderInfo();
        checkmark();
        updateCartCount();

        SharedCart.cartItems.addListener((ListChangeListener<? super HashMap<String, Object>>) change -> updateCartCount());
    }

    private void setupTableColumns() {
        name.setCellValueFactory(data -> new SimpleStringProperty((String) data.getValue().get("name")));
        price.setCellValueFactory(data -> new SimpleDoubleProperty((Double) data.getValue().get("price")).asObject());
        quantity.setCellValueFactory(data -> new SimpleIntegerProperty((Integer) data.getValue().get("quantity")).asObject());
        total.setCellValueFactory(data -> new SimpleDoubleProperty((Double) data.getValue().get("total")).asObject());
    }

    private void updateOrderInfo() {
        try (Connection con = DriverManager.getConnection("jdbc:sqlite:store.db");
             PreparedStatement ps = con.prepareStatement("SELECT bill_id, total_price FROM orders ORDER BY bill_id DESC LIMIT 1");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                id.setText(String.valueOf(rs.getInt("bill_id")));
                total_price.setText(String.valueOf(rs.getInt("total_price")));
            }
        } catch (SQLException e) {
            System.out.println("DB error: " + e.getMessage());
        }
    }

    public void checkmark() {
        String path = getClass().getResource("/videos/checkmark.mp4").toExternalForm();
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(path));
        checkmark.setMediaPlayer(mediaPlayer);
        mediaPlayer.play();

        mediaPlayer.setOnEndOfMedia(() -> {
            // Hide circles
            var hiddenCircles = receipt.getChildren().stream()
                    .filter(node -> node instanceof Circle)
                    .peek(node -> node.setVisible(false))
                    .toList();

            WritableImage snapshot = receipt.snapshot(null, null);

            hiddenCircles.forEach(node -> node.setVisible(true));

            File screenshotsDir = new File("Bills");
            if (!screenshotsDir.exists()) screenshotsDir.mkdirs();

            String user = username.getText().trim().replaceAll("\\s+", "_");
            File outputFile;
            do {
                outputFile = new File(screenshotsDir, id.getText() + ".png");
            } while (outputFile.exists());

            try {
                ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", outputFile);
                System.out.println("Screenshot saved: " + outputFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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
            AnchorPane.setLeftAnchor(main_scroll_pane, 165.0);
        } else {
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
    void back_btn(ActionEvent event) throws IOException {
        Parent newRoot = FXMLLoader.load(getClass().getResource("/user_gui/user_main.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(newRoot);
    }

    private void updateCartCount() {
        if (cartCounter != null) {
            int count = SharedCart.getTotalItemCount();
            cartCounter.setText("Your Cart (" + count + ")");
        }
    }
}