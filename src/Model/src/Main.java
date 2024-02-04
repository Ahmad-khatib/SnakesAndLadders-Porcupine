// In your model module main class

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Path to the FXML file in the Screens module
        String fxmlPath = "MainPage.fxml";

        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        // Set up the scene
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Main Page");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
