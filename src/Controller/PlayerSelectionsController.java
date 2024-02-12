package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

public class PlayerSelectionsController {

    @FXML
    private ChoiceBox<String> gameLevelChoiceBox = new ChoiceBox<>();

    @FXML
    private ChoiceBox<String> playersNumberChoiceBox= new ChoiceBox<String>();

    @FXML
    private Button nextButton;

    @FXML
    private void initialize() {
        ObservableList<String> gameLevelChoiceBoxlist = FXCollections.observableArrayList("Easy", "Medium", "Hard");
        ObservableList<String> playersNumberChoiceBoxlist = FXCollections.observableArrayList("2", "3", "4");
        gameLevelChoiceBox.setItems(gameLevelChoiceBoxlist);
        playersNumberChoiceBox.setItems(playersNumberChoiceBoxlist);

        // Add event handler for the nextButton
        nextButton.setOnAction(event -> handleNextButtonClicked());
    }

    private void handleNextButtonClicked() {

        System.out.println("Next button clicked!");
    }
}
