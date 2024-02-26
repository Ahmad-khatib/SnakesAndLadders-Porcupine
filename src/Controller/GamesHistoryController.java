package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.List;

public class GamesHistoryController {

    @FXML
    private TextArea gameHistoryTextArea;

    @FXML
    private Label noGamesLabel;

    public void initialize(List<String> gameHistory) {


    }


    private void displayNoGamesMessage() {
        // Show the noGamesLabel when there are no games to display
        noGamesLabel.setText("No games played yet.");
        noGamesLabel.setVisible(true);
        gameHistoryTextArea.setVisible(false);
    }
}
