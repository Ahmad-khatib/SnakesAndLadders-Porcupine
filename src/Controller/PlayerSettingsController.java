package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PlayerSettingsController {

    @FXML
    private VBox playerBoxContainer;

    private int maxPlayers = 4; // Maximum number of players

    private Circle[] iconOptions = {createColoredCircle(Color.RED), createColoredCircle(Color.GREEN), createColoredCircle(Color.BLUE)};

    @FXML
    private void initialize() {
        // Initialize player fields and choice boxes for default number of players
        addPlayerFields(2); // Assuming default number of players is 2
    }

    private void addPlayerFields(int numPlayers) {
        // Add player fields and choice boxes dynamically based on the number of players
        for (int i = 1; i <= numPlayers; i++) {
            HBox playerBox = new HBox();
            TextField playerNameField = new TextField();
            playerNameField.setPromptText("Player " + i + " Name");
            ChoiceBox<Circle> iconChoiceBox = new ChoiceBox<>();
            iconChoiceBox.getItems().addAll(iconOptions); // Add icon options to choice box

            playerBox.getChildren().addAll(playerNameField, iconChoiceBox);
            playerBoxContainer.getChildren().add(playerBox);
        }
    }

    // Method to set the number of players
    public void setNumberOfPlayers(int numPlayers) {
        // Clear existing player fields and choice boxes
        playerBoxContainer.getChildren().clear();
        // Add player fields and choice boxes for the new number of players
        addPlayerFields(numPlayers);
    }

    // Method to create a colored circle
    private Circle createColoredCircle(Color color) {
        Circle circle = new Circle(10);
        circle.setFill(color);
        return circle;
    }
}
