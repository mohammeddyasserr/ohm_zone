package main_package;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.*;  // For macOS and Windows taskbar
import java.io.IOException;
import java.io.InputStream;

public class main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Load the icon from resources
        InputStream iconStream = getClass().getResourceAsStream("/icons/logo.png");
        if (iconStream != null) {
            Image fxIcon = new Image(iconStream);
            stage.getIcons().add(fxIcon); // JavaFX window icon

            // macOS: Set Dock icon
            if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                try {
                    java.awt.Image awtImage = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/icons/logo.png"));
                    Taskbar.getTaskbar().setIconImage(awtImage);
                } catch (UnsupportedOperationException | SecurityException e) {
                    System.err.println("Unable to set Dock icon: " + e.getMessage());
                }
            }

            // Windows/Linux: Set Taskbar icon
            else if (System.getProperty("os.name").toLowerCase().contains("win") || System.getProperty("os.name").toLowerCase().contains("nix")) {
                try {
                    // This may be specific to Windows, but it will help for Linux as well
                    java.awt.Image awtImage = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/icons/logo.png"));
                    Taskbar.getTaskbar().setIconImage(awtImage);
                } catch (UnsupportedOperationException | SecurityException e) {
                    System.err.println("Unable to set Taskbar icon: " + e.getMessage());
                }
            }
        } else {
            System.err.println("logo.png not found in resources!");
        }

        // Load the FXML scene
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Ohm Zone"); // You can set the title of the window here
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}