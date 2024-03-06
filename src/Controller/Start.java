package Controller;

import Model.SystemData;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.fxml.FXMLLoader.load;

public class Start extends Application {





    public static void main(String[] args) {

        SystemData systemData = SystemData.getInstance();
        boolean loadSuccess = systemData.loadQuestions();


        if (loadSuccess) {

            System.out.println("Succsed to load questions from JSON.");
        } else {
            System.out.println("Failed to load questions from JSON.");
        }
        SystemData.loadGamesHistoryFromJson("src/Model/History.json");
        launch(args);

    }


    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file
            Parent root = load(getClass().getResource("/View/MainPage.fxml"));

            // Create the scene
            Scene scene = new Scene(root);
            primaryStage.getIcons().add(new Image("/View/Photos/SnakeLadders2.png", 100, 100, true, true));
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
