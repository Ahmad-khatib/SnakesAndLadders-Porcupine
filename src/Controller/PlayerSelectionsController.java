package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.io.IOException;

public class PlayerSelectionsController {

    @FXML
    private ChoiceBox<String> gameLevelChoiceBox = new ChoiceBox<>();

    @FXML
    private ChoiceBox<String> playersNumberChoiceBox= new ChoiceBox<String>();

    @FXML
    private Button nextButton;
    @FXML
    private Button backButton;
    @FXML
    private void initialize() {
        ObservableList<String> gameLevelChoiceBoxlist = FXCollections.observableArrayList("Easy", "Medium", "Hard");
        ObservableList<String> playersNumberChoiceBoxlist = FXCollections.observableArrayList("2", "3", "4");
        gameLevelChoiceBox.setItems(gameLevelChoiceBoxlist);
        playersNumberChoiceBox.setItems(playersNumberChoiceBoxlist);

        // Add event handler for the nextButton
        nextButton.setOnAction(event -> handleNextButtonClicked());
    }
    @FXML
    private void handleNextButtonClicked() {
        String selectedLevel = gameLevelChoiceBox.getValue();
        int boardSize = 0;

        switch (selectedLevel) {
            case "Easy":
                boardSize = 7;
                break;
            case "Medium":
                boardSize = 10;
                break;
            case "Hard":
                boardSize = 13;
                break;
            default:
                break;
        }

        // Pass the board size to the next controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/BoardGame.fxml"));
        Parent root;
        try {
            root = loader.load();
            BoardGameController boardGameController = loader.getController();
            boardGameController.initializeBoard(boardSize); // Pass board size to the controller
            Stage stage = (Stage) nextButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackButtonClicked() {
        // Load the main page when the back button is clicked
        try {
            // Load the FXML file for the main page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/MainPage.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) backButton.getScene().getWindow();

            // Set the main page as the scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
