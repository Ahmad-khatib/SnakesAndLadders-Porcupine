package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class BoardGameController {

    @FXML
    private GridPane gameBoard;

    public void initializeBoard(int size) {
        // Clear the existing game board
        gameBoard.getChildren().clear();

        // Create cells and add them to the grid pane
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Button cell = new Button();
                cell.setMinSize(50, 50); // Adjust size as needed
                gameBoard.add(cell, col, row);
            }
        }
    }
}
