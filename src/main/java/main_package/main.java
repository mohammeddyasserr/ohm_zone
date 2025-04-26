package main_package;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
<<<<<<< Updated upstream
        /*FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("login.fxml"));
=======
        // Load the FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
>>>>>>> Stashed changes
        Scene scene = new Scene(fxmlLoader.load());
         stage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/logo.png")));
        // Set title and scene for the stage
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();*/
        Parent root =FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene =new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}