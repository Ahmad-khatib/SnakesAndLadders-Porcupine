import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

public class MainPageController {

    @FXML
    private Label instructionLabel;
    @FXML
    private Label questionMarkLabel;

    public void initialize() {
        // Set up tooltip for the question mark label
        Tooltip tooltip = new Tooltip("This is additional information.");
        questionMarkLabel.setTooltip(tooltip);

        // Optional: You can add event handlers if needed
        questionMarkLabel.setOnMouseEntered(event -> {
            // Code to execute when the mouse enters the label
        });

        questionMarkLabel.setOnMouseExited(event -> {
            // Code to execute when the mouse exits the label
        });
    }
    @FXML
    private void startGame() {
        // Code to start the game
        System.out.println("Game started!");
        instructionLabel.setText("Game started!"); // Update the instruction label if needed
    }

    @FXML
    private void viewHistory() {
        // Code to view game history
        System.out.println("Viewing game history");
        instructionLabel.setText("Viewing game history"); // Update the instruction label if needed
    }

    @FXML
    private void manageQuestions() {
        // Code to manage game questions
        System.out.println("Managing questions");
        instructionLabel.setText("Managing questions"); // Update the instruction label if needed
    }

}
