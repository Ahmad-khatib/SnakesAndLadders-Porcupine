package Controller;

import Model.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static Controller.PlayerSelectionsController.chosenLevel;

public class PlayerSettingsController {
    // Set to store selected icon images
    private final Set<ImageView> selectedIcons = new HashSet<>();
    private ArrayList<Player> players = new ArrayList<>();
    // VBox element to contain player information
    @FXML
    private VBox playerBoxContainer;

    // Button elements
    @FXML
    private Button resetButton;
    @FXML
    private Button startButton;
    @FXML
    private Button backButton;

    // Method to initialize player fields
    public void initializePlayerFields(int numPlayers) {
        startButton.setDisable(true);
        // Set an action for the reset button
        resetButton.setOnAction(event -> handleResetButtonClicked());
        // Loop through each player
        for (int i = 1; i <= numPlayers; i++) {
            // Create a new HBox for each player
            HBox playerBox = new HBox();
            // Create a text field for the player's name
            TextField playerNameField = new TextField();
            playerNameField.setPromptText("Player " + i + " Name");
            playerBox.getChildren().add(playerNameField);
            // Styling for the text field
            playerNameField.setStyle("-fx-background-color: #173f02; -fx-text-fill: #77d472;  -fx-border-radius: 19px;");

            // Add listener for text field changes
            playerNameField.textProperty().addListener((observable, oldValue, newValue) -> {
                startButton.setDisable(!areAllPlayersReady());
            });

            // Add listener for focus loss
            playerNameField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) { // If focus lost
                    startButton.setDisable(!areAllPlayersReady());
                }
            });

            // Inside initializePlayerFields method
            for (int j = 0; j < 4; j++) {
                // Load the icon image
                ImageView iconImageView = new ImageView(new Image("/View/Photos/" + getIconFileName(j)));
                // Set the size of the icon image view
                iconImageView.setFitWidth(30);
                iconImageView.setFitHeight(30);
                // Make the ImageView clickable
                iconImageView.setPickOnBounds(true);
                // Event handler for when the icon is clicked
                iconImageView.setOnMouseClicked(event -> handleIconSelection(iconImageView, playerBox));
                // Add the icon image view to the player's HBox
                playerBox.getChildren().add(iconImageView);
            }

            // Add the player's HBox to the player box container
            playerBoxContainer.getChildren().add(playerBox);
        }

    }


    // Method to get the filename of the icon image based on its index
    private String getIconFileName(int index) {
        switch (index) {
            case 0:
                return "blueIcon.png";
            case 1:
                return "redIcon.png";
            case 2:
                return "yellowIcon.png";
            case 3:
                return "greenIcon.png";
            default:
                return "";
        }
    }

    // Method to handle the start button being clicked
    @FXML
    private void handleStartButtonClicked() {

        startButton.setDisable(true);
        // Collect player details
        for (Node playerBox : playerBoxContainer.getChildren()) {
            if (playerBox instanceof HBox) {
                HBox hbox = (HBox) playerBox;
                String playerName = "";
                ImageView selectedIcon = null;

                // Iterate through each node in the player's HBox
                for (Node node : hbox.getChildren()) {
                    if (node instanceof TextField) {
                        // Get the player's name
                        TextField playerNameField = (TextField) node;
                        playerName = playerNameField.getText();
                    } else if (node instanceof ImageView && selectedIcons.contains(node)) {
                        // Get the selected icon
                        selectedIcon = (ImageView) node;
                    }
                }

                // If both name and icon are selected, create a Player object and add it to the list
                if (!playerName.isEmpty() && selectedIcon != null) {
                    int id=0;
                    players.add(new Player(++id,playerName, selectedIcon,1));
                }}
}

        // Determine the board size based on the chosen level
        int boardSize = 0;

        switch (chosenLevel) {
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

        // Load the board game FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/BoardGame.fxml"));
        Parent root;
        try {
            root = loader.load();
            // Initialize the board game controller
            GameBoardController boardGameController = loader.getController();
            boardGameController.initialize(chosenLevel,players); // Pass board size to the controller
            // Set the stylesheet for the scene
            root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/View/PorcupineStyle.css")).toExternalForm());
            // Get the current stage and set the scene to the board game scene
            Stage stage = (Stage) startButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to handle the back button being clicked
    @FXML
    private void handleBackButtonClicked() {
        // Load the player selections FXML file
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/PlayerSelections.fxml"));
            Parent root = loader.load();
            // Get the current stage and set the scene to the player selections scene
            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Player Selections");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Method to handle the selection of an icon
    private void handleIconSelection(ImageView selectedIconImageView) {


        // Add the selected icon to the set of chosen icons
        selectedIcons.add(selectedIconImageView);

        // Disable the selected icon for all other players
        for (Node playerBox : playerBoxContainer.getChildren()) {
            if (playerBox instanceof HBox) {
                HBox hbox = (HBox) playerBox;
                // Find the index of the selected icon in this player's HBox
                int iconIndex = hbox.getChildren().indexOf(selectedIconImageView);
                if (iconIndex != -1) { // If the selected icon exists in this player's HBox
                    // Iterate through each player's HBox and disable the same icon
                    for (Node otherPlayerBox : playerBoxContainer.getChildren()) {
                        if (otherPlayerBox instanceof HBox && otherPlayerBox != playerBox) {
                            HBox otherHBox = (HBox) otherPlayerBox;
                            Node otherIconNode = otherHBox.getChildren().get(iconIndex);
                            if (otherIconNode instanceof ImageView) {
                                ImageView otherIconImageView = (ImageView) otherIconNode;
                                otherIconImageView.setDisable(true);
                                otherIconImageView.setStyle("-fx-opacity: 0.5;"); // Optionally, reduce opacity to indicate disabled state
                            }
                        }
                    }
                    break; // Break out of the loop once the selected icon is found
                }
            }
        }

        // Add a visual indication of selection
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.BLACK); // Change the color as desired
        dropShadow.setRadius(10); // Adjust the radius as desired
        selectedIconImageView.setEffect(dropShadow); // Apply drop shadow effect


    }

    // Method to handle the selection of an icon
    private void handleIconSelection(ImageView selectedIconImageView, HBox playerBox) {


        // Find the index of the selected icon in the player's HBox
        int iconIndex = playerBox.getChildren().indexOf(selectedIconImageView);

        // Disable the selected icon for all other players
        for (Node otherPlayerBox : playerBoxContainer.getChildren()) {
            if (otherPlayerBox instanceof HBox && otherPlayerBox != playerBox) {
                HBox otherHBox = (HBox) otherPlayerBox;
                if (iconIndex != -1 && iconIndex < otherHBox.getChildren().size()) {
                    Node otherIconNode = otherHBox.getChildren().get(iconIndex);
                    if (otherIconNode instanceof ImageView) {
                        ImageView otherIconImageView = (ImageView) otherIconNode;
                        otherIconImageView.setDisable(true);
                        otherIconImageView.setStyle("-fx-opacity: 0.5;"); // Optionally, reduce opacity to indicate disabled state
                    }
                }
            }
        }

        // Enable all icons for this player
        for (Node node : playerBox.getChildren()) {
            if (node instanceof ImageView) {
                ImageView iconImageView = (ImageView) node;
                iconImageView.setDisable(false);
                iconImageView.setStyle(""); // Clear any previous styles
            }
        }

        // Clear the previously chosen icon from the set of chosen icons
        selectedIcons.remove(selectedIconImageView);

        // Disable all icons for this player except the selected one
        for (Node node : playerBox.getChildren()) {
            if (node instanceof ImageView && !node.equals(selectedIconImageView)) {
                ImageView iconImageView = (ImageView) node;
                iconImageView.setDisable(true);
                iconImageView.setStyle("-fx-opacity: 0.5;"); // Optionally, reduce opacity to indicate disabled state
            }
        }

        // Add the selected icon to the set of chosen icons
        selectedIcons.add(selectedIconImageView);
        // Check if any player has started choosing icons
        if (selectedIcons.size() == 1) {
            // Show the reset button
            resetButton.setVisible(true);
        }
        // Add a visual indication of selection
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.BLACK); // Change the color as desired
        dropShadow.setRadius(10); // Adjust the radius as desired
        selectedIconImageView.setEffect(dropShadow); // Apply drop shadow effect

        startButton.setDisable(!areAllPlayersReady());
    }
    // Method to handle the reset button being clicked
    @FXML
    private void handleResetButtonClicked() {
        // Clear the set of chosen icons
        selectedIcons.clear();
        startButton.setDisable(true);

        // Enable all icons for all players
        for (Node playerBox : playerBoxContainer.getChildren()) {
            if (playerBox instanceof HBox) {
                HBox hbox = (HBox) playerBox;
                for (Node node : hbox.getChildren()) {
                    if (node instanceof ImageView) {
                        ImageView iconImageView = (ImageView) node;
                        iconImageView.setDisable(false);
                        iconImageView.setStyle(""); // Clear any previous styles
                        iconImageView.setEffect(null); // Clear any effects
                    }
                }
            }
        }
    }
    private boolean areAllPlayersReady() {
        // Loop through each player
        for (Node playerBox : playerBoxContainer.getChildren()) {
            if (playerBox instanceof HBox) {
                HBox hbox = (HBox) playerBox;
                boolean iconChosen = false;
                boolean nameFilled = false;
                // Check if the player has chosen an icon and filled in their name
                for (Node node : hbox.getChildren()) {
                    if (node instanceof ImageView) {
                        ImageView iconImageView = (ImageView) node;
                        if (selectedIcons.contains(iconImageView)) {
                            iconChosen = true;
                        }
                    }
                    if (node instanceof TextField && !((TextField) node).getText().isEmpty()) {
                        nameFilled = true;
                    }
                }
                // If any player is not ready, return false
                if (!iconChosen || !nameFilled) {
                    return false;
                }
            }
        }
        // All players are ready
        return true;
}




}