package Controller;

import javafx.fxml.FXML;
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
}
