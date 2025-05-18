package main_package;

import admin_gui_controller.admin_main_controller;
import db_edit_functions.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import main_package.user_session;
import org.mindrot.jbcrypt.BCrypt;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.sql.*;

public class logincontroller {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML private ImageView logo;
    @FXML private RadioButton admin;
    @FXML private RadioButton user;
    @FXML private ToggleGroup user_admin;
    @FXML private TextField userfield;
    @FXML private PasswordField passfield;
    @FXML private Button signinbtn;
    @FXML private Label question;
    @FXML private Button signup;
    @FXML private PasswordField pass2signup;
    @FXML private TextField usernamesignup;
    @FXML private PasswordField pass1signup;
    @FXML private TextField phonesignup;
    @FXML private TextField addresssignup;
    @FXML private Button signupbtn;
    @FXML private Button back_to_login;
    @FXML private Label pass_error;
    @FXML private Label user_error;
    @FXML private Label user_empty;
    @FXML private Label user_taken;
    @FXML private Label pass_empty;
    @FXML private Label pass_match;
    @FXML private Label phone_error;
    @FXML private Label address_error;
    @FXML private StackPane rootPane; // fx:id of the outer StackPane
    @FXML private Group layoutGroup; // fx:id of the Group containing all UI
    private double baseWidth = 923.0;  // Match your design size
    private double baseHeight = 631.0;



    @FXML
    public void initialize() {
        // Login flow: username -> password -> sign in
        userfield.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                passfield.requestFocus();
            }
        });
        passfield.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                signinbtn.fire();
            }
        });

        // Signup flow: username -> pass1 -> pass2 -> phone -> address -> signup
        usernamesignup.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                pass1signup.requestFocus();
            }
        });
        pass1signup.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                pass2signup.requestFocus();
            }
        });
        pass2signup.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                phonesignup.requestFocus();
            }
        });
        phonesignup.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                addresssignup.requestFocus();
            }
        });
        addresssignup.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                signupbtn.fire();
            }
        });
        rootPane.widthProperty().addListener((obs, oldVal, newVal) -> scaleUI());
        rootPane.heightProperty().addListener((obs, oldVal, newVal) -> scaleUI());
    }
    private void scaleUI() {
        double scaleX = rootPane.getWidth() / baseWidth;
        double scaleY = rootPane.getHeight() / baseHeight;
        double scale = Math.min(scaleX, scaleY); // Keep aspect ratio

        layoutGroup.setScaleX(scale);
        layoutGroup.setScaleY(scale);
    }

    @FXML
    void adminselect(ActionEvent event) {}

    @FXML
    void userselect(ActionEvent event) {}

    @FXML
    void signinclick(ActionEvent event) throws IOException {
        pass_error.setVisible(false);
        user_error.setVisible(false);
        if (admin.isSelected()) {
            try (Connection con = DriverManager.getConnection("jdbc:sqlite:store.db")) {
                PreparedStatement ps = con.prepareStatement("select * from admins where user_name=?");
                ps.setString(1, userfield.getText());
                ResultSet r = ps.executeQuery();
                if (r.next() && BCrypt.checkpw(passfield.getText(), r.getString("password"))) {
                    user_session.set_user(userfield.getText());
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin_gui/admin_main.fxml"));
                    Parent root = loader.load();
                    Scene scene = signinbtn.getScene();  // Reuse existing scene
                    scene.setRoot(root);                 // Set new root
                } else {
                    pass_error.setVisible(!r.next());
                    user_error.setVisible(r.next());
                }
                r.close(); ps.close();
            } catch (SQLException ee) {
                System.out.print(ee.getMessage());
            }
        } else {
            try (Connection con = DriverManager.getConnection("jdbc:sqlite:store.db")) {
                PreparedStatement ps = con.prepareStatement("select * from users where user_name=?");
                ps.setString(1, userfield.getText());
                ResultSet r = ps.executeQuery();
                if (r.next() && BCrypt.checkpw(passfield.getText(), r.getString("password"))) {
                    user_session.set_user(userfield.getText());
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/user_gui/user_main.fxml"));
                    Parent root = loader.load();
                    Scene scene = ((Node) event.getSource()).getScene();
                    scene.setRoot(root);
                } else {
                    pass_error.setVisible(!r.next());
                    user_error.setVisible(r.next());
                }
                r.close(); ps.close();
            } catch (SQLException ee) {
                System.out.print(ee.getMessage());
            }
        }
    }

    @FXML
    void signup(ActionEvent event) {
        user_empty.setVisible(false);
        user_taken.setVisible(false);
        pass_empty.setVisible(false);
        pass_match.setVisible(false);
        phone_error.setVisible(false);
        address_error.setVisible(false);
        try (Connection con = DriverManager.getConnection("jdbc:sqlite:store.db")) {
            PreparedStatement ps = con.prepareStatement("select * from users where user_name=?");
            ps.setString(1, usernamesignup.getText());
            ResultSet r = ps.executeQuery();
            if (usernamesignup.getText().isEmpty()) {
                user_empty.setVisible(true);
            } else if (r.next()) {
                user_taken.setVisible(true);
            } else if (pass1signup.getText().isEmpty()) {
                pass_empty.setVisible(true);
            } else if (!pass1signup.getText().equals(pass2signup.getText())) {
                pass_match.setVisible(true);
            } else if (phonesignup.getText().length() != 11) {
                phone_error.setVisible(true);
            } else if (addresssignup.getText().isEmpty()) {
                address_error.setVisible(true);
            } else {
                User.addToDatabase(con, usernamesignup.getText(), BCrypt.hashpw(pass1signup.getText(), BCrypt.gensalt()), phonesignup.getText(), addresssignup.getText());
                TranslateTransition transition = new TranslateTransition(Duration.seconds(1), logo);
                transition.setByX(-440);
                transition.play();
                showLoginForm();
                clearSignupFields();
            }
            r.close(); ps.close();
        } catch (SQLException ee) {
            System.out.print(ee.getMessage());
        }
    }

    @FXML
    void signuprequest(ActionEvent event) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), logo);
        transition.setByX(440);
        transition.play();
        showSignupForm();
    }

    @FXML
    void backtologin(ActionEvent event) {
        user_empty.setVisible(false);
        user_taken.setVisible(false);
        pass_empty.setVisible(false);
        pass_match.setVisible(false);
        phone_error.setVisible(false);
        address_error.setVisible(false);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), logo);
        transition.setByX(-440);
        transition.play();
        showLoginForm();
        clearSignupFields();
    }

    private void showLoginForm() {
        userfield.setVisible(true);
        passfield.setVisible(true);
        admin.setVisible(true);
        user.setVisible(true);
        signinbtn.setVisible(true);
        question.setVisible(true);
        signup.setVisible(true);
        pass2signup.setVisible(false);
        usernamesignup.setVisible(false);
        pass1signup.setVisible(false);
        phonesignup.setVisible(false);
        addresssignup.setVisible(false);
        signupbtn.setVisible(false);
        back_to_login.setVisible(false);
    }

    private void showSignupForm() {
        userfield.setVisible(false);
        passfield.setVisible(false);
        admin.setVisible(false);
        user.setVisible(false);
        signinbtn.setVisible(false);
        question.setVisible(false);
        signup.setVisible(false);
        pass2signup.setVisible(true);
        usernamesignup.setVisible(true);
        pass1signup.setVisible(true);
        phonesignup.setVisible(true);
        addresssignup.setVisible(true);
        signupbtn.setVisible(true);
        back_to_login.setVisible(true);
        pass_error.setVisible(false);
    }

    private void clearSignupFields() {
        usernamesignup.setText("");
        pass1signup.setText("");
        pass2signup.setText("");
        phonesignup.setText("");
        addresssignup.setText("");
    }
}