import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file
        Parent root = FXMLLoader.load(getClass().getResource("C:\\Users\\noura\\Documents\\GitHub\\SnakesAndLadders-Porcupine\\Controller\\src\\MainPageFXML.fxml"));

        // Set up the stage
        primaryStage.setTitle("Snakes and ladders");
        Scene scene = new Scene(root, 872, 589); // Set the scene dimensions based on your FXML
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
