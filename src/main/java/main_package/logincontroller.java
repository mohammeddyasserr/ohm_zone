package main_package;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class logincontroller {

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
    void adminselect(ActionEvent event) {

    }

    @FXML
    void signinclick(ActionEvent event) {

    }

    @FXML
    void signup(ActionEvent event) {
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
    }

    @FXML
    void userselect(ActionEvent event) {

    }

}
