import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class PlayerSelectionController extends Player {

    @FXML
    private ChoiceBox<Integer> playersChoiceBox;

    @FXML
    private ToggleGroup levelToggleGroup;

    @FXML
    private Button readyButton;

    @FXML
    private void initialize() {
        // Initialize the choice box with player numbers
        playersChoiceBox.getItems().addAll(2, 3, 4);
    }

    @FXML
    private void readyButtonClicked() {
        // Retrieve selected values and perform actions
        int selectedPlayers = playersChoiceBox.getValue();
        RadioButton selectedLevelRadioButton = (RadioButton) levelToggleGroup.getSelectedToggle();
        String selectedLevel = selectedLevelRadioButton.getText();

        // Perform actions based on selected values
        System.out.println("Players: " + selectedPlayers);
        System.out.println("Level: " + selectedLevel);

        // Add logic to start the game with the selected parameters
        // You might want to transition to the actual game scene
    }
}
