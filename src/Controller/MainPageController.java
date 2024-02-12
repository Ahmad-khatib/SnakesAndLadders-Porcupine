package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainPageController {
    @FXML
    private Button startGameButton;
    @FXML
    private Button ManageQuestionsButton;
    @FXML
    private Button backButton;

    @FXML
    private void initialize() {

    }

    @FXML
    private void startGame() {
        // Add functionality to start the game and navigate to PlayerSelections.fxml
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/PlayerSelections.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) startGameButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.widthProperty().addListener((obs, oldWidth, newWidth) -> {
                // Adjust UI elements based on the new width
            });
            stage.heightProperty().addListener((obs, oldHeight, newHeight) -> {
                // Adjust UI elements based on the new height
            });
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void manageQuestions() {
        // Add functionality to navigate to the ManageQuestions.fxml screen
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ManageQuestions.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) startGameButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Handle the back button action
    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/PreviousPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
