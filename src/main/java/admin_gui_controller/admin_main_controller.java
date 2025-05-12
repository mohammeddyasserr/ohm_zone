package admin_gui_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main_package.user_session;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class admin_main_controller implements Initializable {

    @FXML
    private Pane side_panel;

    @FXML
    private Button menu_btn;

    @FXML
    private Button account_btn;

    @FXML
    private LineChart<String, Number> incomeChart;

    @FXML
    private Label total_value;

    @FXML
    private Label monthly_value;

    @FXML
    private Label daily_value;

    private void loadIncomeData() {
        String query = """
        SELECT order_date, SUM(total_price) AS total_income
        FROM orders
        GROUP BY order_date
        ORDER BY order_date
    """;

        try (Connection con = DriverManager.getConnection("jdbc:sqlite:store.db");
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Daily Income");

            while (rs.next()) {
                String date = rs.getString("order_date");
                double income = rs.getDouble("total_income");
                series.getData().add(new XYChart.Data<>(date, income));
            }

            incomeChart.getData().clear();
            incomeChart.getData().add(series);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadTotalIncome() {
        try (Connection con = DriverManager.getConnection("jdbc:sqlite:store.db");
             PreparedStatement ps = con.prepareStatement("SELECT SUM(total_price) AS total FROM orders");
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                double total = rs.getDouble("total");
                total_value.setText(String.format("%.2f", total)); // format with 2 decimals
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadMonthlyIncome() {
        try (Connection con = DriverManager.getConnection("jdbc:sqlite:store.db");
             PreparedStatement ps = con.prepareStatement(
                     "SELECT SUM(total_price) AS total FROM orders WHERE strftime('%Y-%m', order_date) = strftime('%Y-%m', 'now')")
        ) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double total = rs.getDouble("total");
                monthly_value.setText(String.format("%.2f", total));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadDailyIncome() {
        try (Connection con = DriverManager.getConnection("jdbc:sqlite:store.db");
             PreparedStatement ps = con.prepareStatement(
                     "SELECT SUM(total_price) AS total FROM orders WHERE order_date = date('now')")
        ) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double total = rs.getDouble("total");
                daily_value.setText(String.format("%.2f", total));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadIncomeData();
        loadTotalIncome();
        loadMonthlyIncome();
        loadDailyIncome();
        account_btn.setText(user_session.get_user());
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
        boolean isVisible=side_panel.isVisible();
        side_panel.setVisible(!isVisible);
        //side_panel.setManaged(!isVisible);
    }

}
