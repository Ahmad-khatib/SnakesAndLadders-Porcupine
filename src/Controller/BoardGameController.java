package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

import java.util.Random;

public class BoardGameController {

    @FXML
    private GridPane gameBoard;

    public void initializeBoard(int boardSize) {
        // Clear existing children from the game board
        this.gameBoard.getChildren().clear();

        // Set column and row constraints for equal width and height
        for (int i = 0; i < boardSize; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(100.0 / boardSize);
            colConstraints.setHgrow(Priority.ALWAYS);
            this.gameBoard.getColumnConstraints().add(colConstraints);

            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / boardSize);
            rowConstraints.setVgrow(Priority.ALWAYS);
            this.gameBoard.getRowConstraints().add(rowConstraints);
        }

        // Populate the game board with tiles and set the correct numbers
        Random random = new Random();
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                int tileNumber = (boardSize - row - 1) * boardSize + col + 1;
                Label tileLabel = new Label(Integer.toString(tileNumber));

                // Set a random background color for each tile
                String color = String.format("#%02x%02x%02x",
                        random.nextInt(256),
                        random.nextInt(256),
                        random.nextInt(256));
                tileLabel.setStyle("-fx-background-color: " + color + "; -fx-border-color: black;");

                tileLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                this.gameBoard.add(tileLabel, col, row);
            }
        }
    }
}
