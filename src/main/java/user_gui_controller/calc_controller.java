package user_gui_controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import java.util.HashMap;
import java.util.Map;

public class calc_controller {

    @FXML private ComboBox<String> band_num;
    @FXML private Pane fourbandpane;
    @FXML private Pane fivebandpane;
    @FXML private Pane sixbandpane;

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

    @FXML private Label result;

    private final Map<String, Integer> digitMap = new HashMap<>();
    private final Map<String, Double> multiplierMap = new HashMap<>();
    private final Map<String, String> toleranceMap = new HashMap<>();

    @FXML
    public void initialize() {
        setupMaps();
        setupComboBoxes();
        band_num.getItems().addAll("4", "5", "6");
        band_num.setOnAction(e -> switchBandPane());
    }

    private void setupMaps() {
        digitMap.put("Black", 0);
        digitMap.put("Brown", 1);
        digitMap.put("Red", 2);
        digitMap.put("Orange", 3);
        digitMap.put("Yellow", 4);
        digitMap.put("Green", 5);
        digitMap.put("Blue", 6);
        digitMap.put("Violet", 7);
        digitMap.put("Gray", 8);
        digitMap.put("White", 9);

        multiplierMap.put("Black", 1.0);
        multiplierMap.put("Brown", 10.0);
        multiplierMap.put("Red", 100.0);
        multiplierMap.put("Orange", 1_000.0);
        multiplierMap.put("Yellow", 10_000.0);
        multiplierMap.put("Green", 100_000.0);
        multiplierMap.put("Blue", 1_000_000.0);
        multiplierMap.put("Violet", 10_000_000.0);
        multiplierMap.put("Gray", 100_000_000.0);
        multiplierMap.put("White", 1_000_000_000.0);
        multiplierMap.put("Gold", 0.1);
        multiplierMap.put("Silver", 0.01);

        toleranceMap.put("Brown", "±1%");
        toleranceMap.put("Red", "±2%");
        toleranceMap.put("Green", "±0.5%");
        toleranceMap.put("Blue", "±0.25%");
        toleranceMap.put("Violet", "±0.1%");
        toleranceMap.put("Gray", "±0.05%");
        toleranceMap.put("Gold", "±5%");
        toleranceMap.put("Silver", "±10%");
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

        // Optional: Stretch to fill the space
        HBox.setHgrow(fourbandpane, is4 ? Priority.ALWAYS : Priority.NEVER);
        HBox.setHgrow(fivebandpane, is5 ? Priority.ALWAYS : Priority.NEVER);
        HBox.setHgrow(sixbandpane, is6 ? Priority.ALWAYS : Priority.NEVER);
    }

    @FXML
    public void calculate() {
        try {
            switch (band_num.getValue()) {
                case "4":
                    calculate4Band();
                    break;
                case "5":
                    calculate5Band();
                    break;
                case "6":
                    calculate6Band();
                    break;
                default:
                    result.setText("Please select band count");
            }
        } catch (Exception e) {
            result.setText("Invalid input");
        }
    }

    private void calculate4Band() {
        int digit1 = digitMap.get(band1_4.getValue());
        int digit2 = digitMap.get(band2_4.getValue());
        double multiplier = multiplierMap.get(multiplier_4.getValue());
        String tolerance = toleranceMap.get(tolerance_4.getValue());

        double resistance = (digit1 * 10 + digit2) * multiplier;
        result.setText(formatResistance(resistance) + " Ω " + tolerance);
    }

    private void calculate5Band() {
        int digit1 = digitMap.get(band1_5.getValue());
        int digit2 = digitMap.get(band2_5.getValue());
        int digit3 = digitMap.get(band3_5.getValue());
        double multiplier = multiplierMap.get(multiplier_5.getValue());
        String tolerance = toleranceMap.get(tolerance_5.getValue());

        double resistance = (digit1 * 100 + digit2 * 10 + digit3) * multiplier;
        result.setText(formatResistance(resistance) + " Ω " + tolerance);
    }

    private void calculate6Band() {
        int digit1 = digitMap.get(band1_6.getValue());
        int digit2 = digitMap.get(band2_6.getValue());
        int digit3 = digitMap.get(band3_6.getValue());
        double multiplier = multiplierMap.get(multiplier_6.getValue());
        String tolerance = toleranceMap.get(tolerance_6.getValue());
        String tempco = tempco_6.getValue();

        double resistance = (digit1 * 100 + digit2 * 10 + digit3) * multiplier;
        result.setText(formatResistance(resistance) + " Ω " + tolerance + ", " + tempco);
    }

    private String formatResistance(double resistance) {
        if (resistance >= 1_000_000) return String.format("%.2fM", resistance / 1_000_000);
        if (resistance >= 1_000) return String.format("%.2fk", resistance / 1_000);
        return String.format("%.2f", resistance);
    }
}