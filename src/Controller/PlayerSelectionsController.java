package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class PlayerSelectionsController {
    public static String chosenLevel;

    @FXML
    private ChoiceBox<String> gameLevelChoiceBox = new ChoiceBox<>();

    @FXML
    private ChoiceBox<String> playersNumberChoiceBox= new ChoiceBox<String>();

    @FXML
    private Button nextButton;
    @FXML
    private Button backButton;
    @FXML
    private HBox playerBox1;
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
        // Check if both choice boxes have a selection
        if (gameLevelChoiceBox.getValue() == null || playersNumberChoiceBox.getValue() == null) {
            // Show an alert informing the user to select options from both choice boxes
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Selection Required");
            alert.setHeaderText(null);
            alert.setContentText("Please select options from both choice boxes.");
            alert.showAndWait();
            return;
        }
        // Add functionality to move to the next page(playersettings) when clicking on next and also maintaining the level selected by player because we will be using it in the game board
        String selectedLevel = gameLevelChoiceBox.getValue();
        this.chosenLevel=selectedLevel;
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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/PlayerSettings.fxml"));
        Parent root;
        try {
            root = loader.load();
            PlayerSettingsController controller = loader.getController();
            int numPlayers = Integer.parseInt(playersNumberChoiceBox.getValue());
            controller.initializePlayerFields(numPlayers); // Initialize player fields based on the selected number of players
            root.getStylesheets().add(getClass().getResource("/View/PorcupineStyle.css").toExternalForm());
            Stage stage = (Stage) nextButton.getScene().getWindow();
            stage.setTitle("Player Settings");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackButtonClicked() {
        // Add functionality to navigate to the previous page  MainPage.fxml screen
        try {
            // Load the FXML file for the main page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/MainPage.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            // Get the current stage
            Stage stage = (Stage) backButton.getScene().getWindow();

            // Set the main page as the scene
            stage.setScene(scene);
            stage.setTitle("Main Page");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
