package Controller;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.fxml.FXMLLoader.load;

public class Start extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file
            Parent root = load(getClass().getResource("/View/MainPage.fxml"));

            // Create the scene
            Scene scene = new Scene(root);

            // Set the scene and show the stage
            primaryStage.setScene(scene);
            primaryStage.setTitle("Snake and Ladders Game"); // Set the title of the window
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading MainPage.fxml: " + e.getMessage());
        }
    }
}
