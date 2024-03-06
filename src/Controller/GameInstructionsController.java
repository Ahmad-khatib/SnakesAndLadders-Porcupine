package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameInstructionsController {

    @FXML
    private VBox gameInstructionsVBox;

    @FXML
    private Button closeButton;

    @FXML
    private void initialize() {
        gameInstructionsVBox.setStyle("-fx-background-color: #f0f0f0;");
    }

    @FXML
    private void handleCloseButtonClicked() {
        // Get the reference to the stage
        Stage stage = (Stage) closeButton.getScene().getWindow();

        // Close the stage
        stage.close();
    }

    public void displayGameInstructions () {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("path/to/GameInstructions.fxml"));
            Parent root = loader.load();

            // Create a new scene with the loaded FXML
            Scene scene = new Scene(root);

            // Create a new stage (window) and set the scene
            Stage stage = new Stage();
            stage.setScene(scene);

            // Show the stage
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}