package user_gui_controller;

import javafx.animation.TranslateTransition;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import main_package.user_session;

import java.io.IOException;
import java.util.*;

public class calc_controller {

    @FXML private Button account_btn;
    @FXML private ComboBox<String> band_num;
    @FXML private Pane fourbandpane;
    @FXML private Pane fivebandpane;
    @FXML private Pane sixbandpane;
    @FXML private Pane side_panel;

    // 4-band
    @FXML private ComboBox<String> band1_4;
    @FXML private ComboBox<String> band2_4;
    @FXML private ComboBox<String> multiplier_4;
    @FXML private ComboBox<String> tolerance_4;

    // 5-band
    @FXML private ComboBox<String> band1_5;
    @FXML private ComboBox<String> band2_5;
    @FXML private ComboBox<String> band3_5;
    @FXML private ComboBox<String> multiplier_5;
    @FXML private ComboBox<String> tolerance_5;

    // 6-band
    @FXML private ComboBox<String> band1_6;
    @FXML private ComboBox<String> band2_6;
    @FXML private ComboBox<String> band3_6;
    @FXML private ComboBox<String> multiplier_6;
    @FXML private ComboBox<String> tolerance_6;
    @FXML private ComboBox<String> tempco_6;

    @FXML private Rectangle band1;
    @FXML private Rectangle band2;
    @FXML private Rectangle band3;
    @FXML private Rectangle multiplier;
    @FXML private Rectangle tol;

    @FXML private Label result;
    @FXML private Label cartCounter;

    // Reverse calculator
    @FXML private TextField resistanceInput;
    @FXML private ComboBox<Integer> bandCountCombo;
    @FXML private Button reverseCalcButton;

    private final Map<String, Integer> digitMap = new HashMap<>();
    private final Map<String, Double> multiplierMap = new HashMap<>();
    private final Map<String, String> toleranceMap = new HashMap<>();
    private final Map<String, Color> colorMap = new HashMap<>();

    @FXML
    public void initialize() {
        account_btn.setText(user_session.get_user());
        setupMaps();
        setupComboBoxes();

        band_num.getItems().addAll("4", "5", "6");
        band_num.setOnAction(e -> switchBandPane());

        bandCountCombo.getItems().addAll(4, 5, 6);
        bandCountCombo.setValue(4);
        reverseCalcButton.setOnAction(e -> calculateFromReverseResistance());

        updateCartCount();
        SharedCart.cartItems.addListener((ListChangeListener<? super HashMap<String, Object>>) change -> updateCartCount());
    }

    @FXML
    void toggle_menu(ActionEvent event) {
        side_panel.setVisible(!side_panel.isVisible());
    }

    private void updateCartCount() {
        if (cartCounter != null) {
            int count = SharedCart.getTotalItemCount();
            cartCounter.setText("Your Cart (" + count + ")");
        }
    }

    @FXML void calculator_page(ActionEvent event) throws IOException { switchScene("/user_gui/calc.fxml", event); }
    @FXML void acount_page(ActionEvent event) throws IOException { switchScene("/user_gui/account_page.fxml", event); }
    @FXML void cart_page(ActionEvent event) throws IOException { switchScene("/user_gui/cart.fxml", event); }
    @FXML void home_page_user(ActionEvent event) throws IOException { switchScene("/user_gui/user_main.fxml", event); }
    @FXML void login_page(ActionEvent event) throws IOException { switchScene("/main_package/login.fxml", event); }
    @FXML void pills_page_user(ActionEvent event) throws IOException { switchScene("/user_gui/pills.fxml", event); }
    @FXML void shop_page(ActionEvent event) throws IOException { switchScene("/user_gui/shop.fxml", event); }

    private void switchScene(String fxmlPath, ActionEvent event) throws IOException {
        Parent newRoot = FXMLLoader.load(getClass().getResource(fxmlPath));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(newRoot);
    }

    private void setupMaps() {
        digitMap.put("Black", 0);     colorMap.put("Black", Color.BLACK);
        digitMap.put("Brown", 1);     colorMap.put("Brown", Color.SADDLEBROWN);
        digitMap.put("Red", 2);       colorMap.put("Red", Color.RED);
        digitMap.put("Orange", 3);    colorMap.put("Orange", Color.ORANGE);
        digitMap.put("Yellow", 4);    colorMap.put("Yellow", Color.YELLOW);
        digitMap.put("Green", 5);     colorMap.put("Green", Color.GREEN);
        digitMap.put("Blue", 6);      colorMap.put("Blue", Color.BLUE);
        digitMap.put("Violet", 7);    colorMap.put("Violet", Color.DARKVIOLET);
        digitMap.put("Gray", 8);      colorMap.put("Gray", Color.GRAY);
        digitMap.put("White", 9);     colorMap.put("White", Color.WHITE);
        colorMap.put("Gold", Color.GOLD);   colorMap.put("Silver", Color.SILVER);

        multiplierMap.put("Black", 1.0);      multiplierMap.put("Brown", 10.0);
        multiplierMap.put("Red", 100.0);      multiplierMap.put("Orange", 1_000.0);
        multiplierMap.put("Yellow", 10_000.0);multiplierMap.put("Green", 100_000.0);
        multiplierMap.put("Blue", 1_000_000.0);multiplierMap.put("Violet", 10_000_000.0);
        multiplierMap.put("Gray", 100_000_000.0);multiplierMap.put("White", 1_000_000_000.0);
        multiplierMap.put("Gold", 0.1);       multiplierMap.put("Silver", 0.01);

        toleranceMap.put("Brown", "±1%");   toleranceMap.put("Red", "±2%");
        toleranceMap.put("Green", "±0.5%"); toleranceMap.put("Blue", "±0.25%");
        toleranceMap.put("Violet", "±0.1%");toleranceMap.put("Gray", "±0.05%");
        toleranceMap.put("Gold", "±5%");    toleranceMap.put("Silver", "±10%");
    }

    private void setupComboBoxes() {
        var digits = digitMap.keySet();
        var mults = multiplierMap.keySet();
        var tols = toleranceMap.keySet();

        band1_4.getItems().addAll(digits);
        band2_4.getItems().addAll(digits);
        multiplier_4.getItems().addAll(mults);
        tolerance_4.getItems().addAll(tols);

        band1_5.getItems().addAll(digits);
        band2_5.getItems().addAll(digits);
        band3_5.getItems().addAll(digits);
        multiplier_5.getItems().addAll(mults);
        tolerance_5.getItems().addAll(tols);

        band1_6.getItems().addAll(digits);
        band2_6.getItems().addAll(digits);
        band3_6.getItems().addAll(digits);
        multiplier_6.getItems().addAll(mults);
        tolerance_6.getItems().addAll(tols);
        tempco_6.getItems().addAll("100ppm/K", "50ppm/K", "15ppm/K", "10ppm/K", "5ppm/K", "1ppm/K");

        band1_4.setOnAction(e -> setRectColor(band1, band1_4.getValue()));
        band2_4.setOnAction(e -> setRectColor(band2, band2_4.getValue()));
        multiplier_4.setOnAction(e -> setRectColor(multiplier, multiplier_4.getValue()));
        tolerance_4.setOnAction(e -> setRectColor(tol, tolerance_4.getValue()));

        band1_5.setOnAction(e -> setRectColor(band1, band1_5.getValue()));
        band2_5.setOnAction(e -> setRectColor(band2, band2_5.getValue()));
        band3_5.setOnAction(e -> setRectColor(band3, band3_5.getValue()));
        multiplier_5.setOnAction(e -> setRectColor(multiplier, multiplier_5.getValue()));
        tolerance_5.setOnAction(e -> setRectColor(tol, tolerance_5.getValue()));

        band1_6.setOnAction(e -> setRectColor(band1, band1_6.getValue()));
        band2_6.setOnAction(e -> setRectColor(band2, band2_6.getValue()));
        band3_6.setOnAction(e -> setRectColor(band3, band3_6.getValue()));
        multiplier_6.setOnAction(e -> setRectColor(multiplier, multiplier_6.getValue()));
        tolerance_6.setOnAction(e -> setRectColor(tol, tolerance_6.getValue()));
    }

    private void setRectColor(Rectangle rect, String colorName) {
        Color color = colorMap.getOrDefault(colorName, Color.TRANSPARENT);
        rect.setFill(color);
    }

    private void resetBandColors() {
        band1.setFill(Color.TRANSPARENT);
        band2.setFill(Color.TRANSPARENT);
        band3.setFill(Color.TRANSPARENT);
        multiplier.setFill(Color.TRANSPARENT);
        tol.setFill(Color.TRANSPARENT);
    }

    private void switchBandPane() {
        String selected = band_num.getValue();
        boolean is4 = "4".equals(selected);
        boolean is5 = "5".equals(selected);
        boolean is6 = "6".equals(selected);

        fourbandpane.setVisible(is4);
        fourbandpane.setManaged(is4);
        fivebandpane.setVisible(is5);
        fivebandpane.setManaged(is5);
        sixbandpane.setVisible(is6);
        sixbandpane.setManaged(is6);

        resetBandColors();
    }

    @FXML
    void claculate_act(ActionEvent event) {
        try {
            switch (band_num.getValue()) {
                case "4": calculate4Band(); break;
                case "5": calculate5Band(); break;
                case "6": calculate6Band(); break;
                default: result.setText("Please select band count");
            }
        } catch (Exception e) {
            result.setText("Invalid input");
        }
    }

    private void calculate4Band() {
        int digit1 = digitMap.get(band1_4.getValue());
        int digit2 = digitMap.get(band2_4.getValue());
        double mult = multiplierMap.get(multiplier_4.getValue());
        String tolVal = toleranceMap.get(tolerance_4.getValue());
        double resistance = (digit1 * 10 + digit2) * mult;
        result.setText(formatResistance(resistance) + " Ω " + tolVal);
    }

    private void calculate5Band() {
        int d1 = digitMap.get(band1_5.getValue());
        int d2 = digitMap.get(band2_5.getValue());
        int d3 = digitMap.get(band3_5.getValue());
        double mult = multiplierMap.get(multiplier_5.getValue());
        String tolVal = toleranceMap.get(tolerance_5.getValue());
        double resistance = (d1 * 100 + d2 * 10 + d3) * mult;
        result.setText(formatResistance(resistance) + " Ω " + tolVal);
    }

    private void calculate6Band() {
        int d1 = digitMap.get(band1_6.getValue());
        int d2 = digitMap.get(band2_6.getValue());
        int d3 = digitMap.get(band3_6.getValue());
        double mult = multiplierMap.get(multiplier_6.getValue());
        String tolVal = toleranceMap.get(tolerance_6.getValue());
        String tempco = tempco_6.getValue();
        double resistance = (d1 * 100 + d2 * 10 + d3) * mult;
        result.setText(formatResistance(resistance) + " Ω " + tolVal + ", " + tempco);
    }

    private String formatResistance(double resistance) {
        if (resistance >= 1_000_000) return String.format("%.2fM", resistance / 1_000_000);
        if (resistance >= 1_000) return String.format("%.2fk", resistance / 1_000);
        return String.format("%.2f", resistance);
    }

    private List<String> getColorBandsFromResistance(double resistance, int bandCount) {
        String[] colorArray = {"Black", "Brown", "Red", "Orange", "Yellow", "Green", "Blue", "Violet", "Gray", "White"};
        int digits = bandCount == 4 ? 2 : 3;

        String str = String.format("%.0f", resistance);
        if (str.length() < digits) str = "0".repeat(digits - str.length()) + str;

        String sig = str.substring(0, digits);
        int mult = str.length() - digits;
        if (mult < 0 || mult >= colorArray.length) return null;

        List<String> bands = new ArrayList<>();
        for (char ch : sig.toCharArray()) bands.add(colorArray[Character.getNumericValue(ch)]);
        bands.add(colorArray[mult]);
        bands.add("Gold");
        return bands;
    }

    private void calculateFromReverseResistance() {
        try {
            double resistance = Double.parseDouble(resistanceInput.getText().trim());
            int bands = bandCountCombo.getValue();
            List<String> colors = getColorBandsFromResistance(resistance, bands);

            if (colors == null) {
                result.setText("Invalid value");
                return;
            }

            Rectangle[] rects = {band1, band2, band3, multiplier, tol};
            for (int i = 0; i < rects.length; i++) {
                rects[i].setFill(i < colors.size() ? colorMap.getOrDefault(colors.get(i), Color.TRANSPARENT) : Color.TRANSPARENT);
            }

            result.setText("Reverse OK: " + formatResistance(resistance));
        } catch (Exception e) {
            result.setText("Invalid resistance input.");
        }
    }
}