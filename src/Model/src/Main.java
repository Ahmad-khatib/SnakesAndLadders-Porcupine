import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Load FXML using the correct path
        InputStream fxmlStream = getClass().getResourceAsStream("/src/view/Screens/MainPage.fxml");

        if (fxmlStream == null) {
            System.err.println("FXML file not found!");
            // Handle the situation when the FXML file is not found, e.g., show an error message
            return;
        }

        try {
            Parent root = new FXMLLoader().load(fxmlStream);
            Scene scene = new Scene(root);
            primaryStage.setTitle("Main Page");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the IOException, e.g., show an error message
        }
    }
}
