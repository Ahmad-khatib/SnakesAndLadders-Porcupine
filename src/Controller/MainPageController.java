package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Parent;


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
    private void startGame(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/PlayerSelections.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Get the current stage from the event's source (the button)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
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

            Stage stage = (Stage) ManageQuestionsButton.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
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
