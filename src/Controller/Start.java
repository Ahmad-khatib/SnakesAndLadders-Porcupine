package Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Start extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file
            Parent root = FXMLLoader.load(getClass().getResource("/View/MainPage.fxml"));

            // Create the scene
            Scene scene = new Scene(root);

            // Add the CSS file to the scene
            scene.getStylesheets().add(getClass().getResource("/View/PorcupineStyle.css").toExternalForm());

            // Set the scene and show the stage
            primaryStage.setScene(scene);

            // Adjust stage size to fit the content
            primaryStage.sizeToScene();

            // Maximize the window (optional)
            primaryStage.setMaximized(true);

            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
