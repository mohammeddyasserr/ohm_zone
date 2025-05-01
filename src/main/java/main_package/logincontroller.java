package main_package;
import db_edit_functions.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class logincontroller {
    private Stage stage;
    private Scene scene;
    private Parent root;
    /*public void switchtoadmin(ActionEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("admin_home.fxml"));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene= new Scene(root);
        stage.setScene(scene);
        stage.show();
    }*/
    @FXML
    private ImageView logo;
    @FXML
    private RadioButton admin;
    @FXML
    private RadioButton user;
    @FXML
    private ToggleGroup user_admin;
    @FXML
    private TextField userfield;
    @FXML
    private PasswordField passfield;
    @FXML
    private Button signinbtn;
    @FXML
    private Label question;
    @FXML
    private Button signup;
    @FXML
    private PasswordField pass2signup;
    @FXML
    private TextField usernamesignup;
    @FXML
    private PasswordField pass1signup;
    @FXML
    private TextField phonesignup;
    @FXML
    private TextField addresssignup;
    @FXML
    private Button signupbtn;
    @FXML
    private Button back_to_login;
    @FXML
    private Label pass_error;
    @FXML
    private Label user_error;
    @FXML
    private Label user_empty;
    @FXML
    private Label user_taken;
    @FXML
    private Label pass_empty;
    @FXML
    private Label pass_match;
    @FXML
    private Label phone_error;
    @FXML
    private Label address_error;

    @FXML
    void adminselect(ActionEvent event) {

    }

    @FXML
    void signinclick(ActionEvent event)throws IOException {
        pass_error.setVisible(false);
        user_error.setVisible(false);
        if(admin.isSelected()){
            try{
                Connection con=DriverManager.getConnection("jdbc:sqlite:store.db");
                System.out.println("connected");
                PreparedStatement ps=con.prepareStatement("select * from admins where user_name=?");
                ps.setString(1,userfield.getText());
                ResultSet r=ps.executeQuery();
                if (r.next()) {
                    String dbPassword = r.getString("password");
                    if (dbPassword.equals(passfield.getText())) {
                        user_session.set_user(userfield.getText());
                        
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin_gui/admin_main.fxml"));
                        Parent root = loader.load();
                        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
                        scene= new Scene(root);
                        stage.setScene(scene);
                        stage.show();

                    } else {
                        pass_error.setVisible(true);
                    }
                } else {
                    user_error.setVisible(true);
                }
                r.close();
                ps.close();
                con.close();
        }
            catch(SQLException ee){
            System.out.print(ee.getMessage());
        }
        }
        else {
            try{
                Connection con=DriverManager.getConnection("jdbc:sqlite:store.db");
                System.out.println("connected");
                PreparedStatement ps=con.prepareStatement("select * from users where user_name=?");
                ps.setString(1,userfield.getText());
                ResultSet r=ps.executeQuery();
                if (r.next()) {
                    String dbPassword = r.getString("password");
                    if (dbPassword.equals(passfield.getText())) {
                        Parent root= FXMLLoader.load(getClass().getResource("/user_gui/user_main.fxml"));
                        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
                        scene= new Scene(root);
                        stage.setScene(scene);
                        stage.show();

                    } else {
                        pass_error.setVisible(true);
                    }
                } else {
                    user_error.setVisible(true);
                }
                r.close();
                ps.close();
                con.close();
            }
            catch(SQLException ee){
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
        try{
            Connection con=DriverManager.getConnection("jdbc:sqlite:store.db");
            System.out.println("connected");
            PreparedStatement ps=con.prepareStatement("select * from users where user_name=?");
            ps.setString(1,usernamesignup.getText());
            ResultSet r=ps.executeQuery();
            if(usernamesignup.getText().isEmpty()){
                user_empty.setVisible(true);

            }
            else if (r.next()) {

                user_taken.setVisible(true);
            }else if(pass1signup.getText().isEmpty()){
                pass_empty.setVisible(true);
            }
            else if(!pass1signup.getText().equals(pass2signup.getText())){
                pass_match.setVisible(true);
            }else if(phonesignup.getText().length()!=11){
                phone_error.setVisible(true);
            }
            else if(addresssignup.getText().isEmpty()){
                address_error.setVisible(true);
            }
            else{
                User.addToDatabase(con,usernamesignup.getText(),pass1signup.getText(),phonesignup.getText(),addresssignup.getText());
                TranslateTransition transition = new TranslateTransition();
                transition.setNode(logo);  // تحديد الشعار كالعنصر الذي سيتم تحريكه
                transition.setByX(-440);    // تحريك الشعار بمقدار 300 وحدة إلى اليمين (يمكنك تعديل هذه القيمة حسب احتياجك)
                transition.setDuration(Duration.seconds(1)); // تحديد مدة الانتقال (1 ثانية في هذا المثال)
                transition.play();  // تشغيل الانتقال
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
                usernamesignup.setText("");
                pass1signup.setText("");
                pass2signup.setText("");
                phonesignup.setText("");
                addresssignup.setText("");

            }
            r.close();
            ps.close();
            con.close();
        }
        catch(SQLException ee){
            System.out.print(ee.getMessage());
        }

    }

    @FXML
    void signuprequest(ActionEvent event) {
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(logo);  // تحديد الشعار كالعنصر الذي سيتم تحريكه
        transition.setByX(440);    // تحريك الشعار بمقدار 300 وحدة إلى اليمين (يمكنك تعديل هذه القيمة حسب احتياجك)
        transition.setDuration(Duration.seconds(1)); // تحديد مدة الانتقال (1 ثانية في هذا المثال)
        transition.play();  // تشغيل الانتقال
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
    }

    @FXML
    void backtologin(ActionEvent event) {
        user_empty.setVisible(false);
        user_taken.setVisible(false);
        pass_empty.setVisible(false);
        pass_match.setVisible(false);
        phone_error.setVisible(false);
        address_error.setVisible(false);
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(logo);  // تحديد الشعار كالعنصر الذي سيتم تحريكه
        transition.setByX(-440);    // تحريك الشعار بمقدار 300 وحدة إلى اليمين (يمكنك تعديل هذه القيمة حسب احتياجك)
        transition.setDuration(Duration.seconds(1)); // تحديد مدة الانتقال (1 ثانية في هذا المثال)
        transition.play();  // تشغيل الانتقال
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
        usernamesignup.setText("");
        pass1signup.setText("");
        pass2signup.setText("");
        phonesignup.setText("");
        addresssignup.setText("");
    }

    @FXML
    void userselect(ActionEvent event) {

    }

}
