package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static Controller.PlayerSelectionsController.chosenLevel;

public class PlayerSettingsController {
    private Set<ImageView> selectedIcons = new HashSet<>(); // Set to store selected icons
    private Set<CheckBox> selectedCheckboxes = new HashSet<>(); // Set to store selected checkboxes
    @FXML
    private VBox playerBoxContainer;
    @FXML
    private Button startButton;
    @FXML
    private Button backButton;

    private int maxPlayers = 4; // Maximum number of players

    public void initializePlayerFields(int numPlayers) {
        for (int i = 1; i <= numPlayers; i++) {
            HBox playerBox = new HBox();
            TextField playerNameField = new TextField();
            playerNameField.setPromptText("Player " + i + " Name");
            playerBox.getChildren().add(playerNameField);
            playerNameField.setStyle("-fx-background-color: #173f02; -fx-text-fill: #77d472;  -fx-border-radius: 19px;");

            for (int j = 0; j < 4; j++) {
                Image iconImage = new Image("/View/Photos/" + getIconFileName(j));
                ImageView iconImageView = new ImageView(iconImage);
                iconImageView.setFitWidth(30); // Adjust the size of the icon image view
                iconImageView.setFitHeight(30);
                CheckBox iconCheckBox = new CheckBox();
                iconCheckBox.setOnAction(event -> handleIconSelection(iconCheckBox, iconImageView));
                HBox iconBox = new HBox(iconImageView, iconCheckBox);
                playerBox.getChildren().add(iconBox);
            }

            playerBoxContainer.getChildren().add(playerBox);
        }
    }

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

    @FXML
    private void handleStartButtonClicked() {
        // Add functionality to move to the board game page where the game is being started
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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/BoardGame.fxml"));
        Parent root;
        try {
            root = loader.load();
            GameBoardController boardGameController = loader.getController();
            boardGameController.initialize(chosenLevel); // Pass board size to the controller
            root.getStylesheets().add(getClass().getResource("/View/PorcupineStyle.css").toExternalForm());
            Stage stage = (Stage) startButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackButtonClicked() {
        // Add functionality to navigate to the previous page (PlayerSelections.fxml)
        // Load the main page when the back button is clicked
        try {
            // Load the FXML file for the main page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/PlayerSelections.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            // Get the current stage
            Stage stage = (Stage) backButton.getScene().getWindow();

            // Set the main page as the scene
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleIconSelection(CheckBox iconCheckBox, ImageView iconImageView) {
        if (iconCheckBox.isSelected()) {
            // Disable checkboxes for the selected icon for other players
            playerBoxContainer.getChildren().forEach(playerBox -> {
                if (playerBox instanceof HBox) {
                    HBox hbox = (HBox) playerBox;
                    hbox.getChildren().forEach(node -> {
                        if (node instanceof HBox && node != iconCheckBox.getParent()) { // Exclude the current player's icon box
                            HBox iconBox = (HBox) node;
                            iconBox.getChildren().forEach(childNode -> {
                                if (childNode instanceof CheckBox) {
                                    ((CheckBox) childNode).setDisable(true);
                                    System.out.print(childNode);
                                }
                            });
                        }
                    });
                }
            });

            selectedCheckboxes.add(iconCheckBox);
            selectedIcons.add(iconImageView);
        } else {
            // Re-enable checkboxes if the icon is deselected
            playerBoxContainer.getChildren().forEach(playerBox -> {
                if (playerBox instanceof HBox) {
                    HBox hbox = (HBox) playerBox;
                    hbox.getChildren().forEach(node -> {
                        if (node instanceof HBox) {
                            HBox iconBox = (HBox) node;
                            iconBox.getChildren().forEach(childNode -> {
                                if (childNode instanceof CheckBox) {
                                    ((CheckBox) childNode).setDisable(false);
                                }
                            });
                        }
                    });
                }
            });

            selectedCheckboxes.remove(iconCheckBox);
            selectedIcons.remove(iconImageView);
        }
    }

}
