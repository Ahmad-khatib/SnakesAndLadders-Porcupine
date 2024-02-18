package Controller;

import Model.GameBoard;
import Model.Tile;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.FileNotFoundException;

public class GameBoardController extends GridPane {

    @FXML
    private GridPane dynamicGridPane;

    public void initialize(String selectedLevel) throws FileNotFoundException {
        GameBoard gameBoard = new GameBoard(selectedLevel);
        initializeBoardUI(gameBoard);
        placeSnake(selectedLevel);
    }

    private void placeSnake(String selectedLevel) {
        String greenSnake = "/View/Photos/greenSnake.png";
        String blueSnake = "/View/Photos/blueSnake.png";
        String yellowSnake = "/View/Photos/yellowSnake.png";
        String redSnake = "/View/Photos/redSnake.png";
        Image greenSnakeimage = new Image(getClass().getResourceAsStream(greenSnake));
        Image bludSnakeimage = new Image(getClass().getResourceAsStream(blueSnake));
        Image yellowSnakeimage = new Image(getClass().getResourceAsStream(yellowSnake));
        Image redSnakeimage = new Image(getClass().getResourceAsStream(redSnake));

        ImageView greenSnakeimageimageView = new ImageView(greenSnakeimage);
        ImageView bludSnakeimageimageView = new ImageView(bludSnakeimage);
        ImageView yellowSnakeimageimageView = new ImageView(yellowSnakeimage);
        ImageView redSnakeimageimageView = new ImageView(redSnakeimage);

        greenSnakeimageimageView.setRotate(60);
        greenSnakeimageimageView.setSmooth(false);
        greenSnakeimageimageView.setPreserveRatio(false);
        greenSnakeimageimageView.setFitHeight(250);
        greenSnakeimageimageView.setFitWidth(60);
        greenSnakeimageimageView.setVisible(true);
        StackPane stackPane = new StackPane(greenSnakeimageimageView);
        dynamicGridPane.add(stackPane, 2, 2, 4, 4);


        bludSnakeimageimageView.setRotate(60);
        bludSnakeimageimageView.setSmooth(false);
        bludSnakeimageimageView.setPreserveRatio(false);
        bludSnakeimageimageView.setFitHeight(250);
        bludSnakeimageimageView.setFitWidth(60);
        bludSnakeimageimageView.setVisible(true);
        StackPane bludSnakeimageimageViewstackPane = new StackPane(bludSnakeimageimageView);
        dynamicGridPane.add(bludSnakeimageimageViewstackPane, 1, 6, 8, 5);


        yellowSnakeimageimageView.setRotate(45);
        yellowSnakeimageimageView.setSmooth(false);
        yellowSnakeimageimageView.setPreserveRatio(false);
        yellowSnakeimageimageView.setFitHeight(180);
        yellowSnakeimageimageView.setFitWidth(30);
        yellowSnakeimageimageView.setVisible(true);
        StackPane yellowSnakeimageimageViewstackPane = new StackPane(yellowSnakeimageimageView);
        dynamicGridPane.add(yellowSnakeimageimageViewstackPane, 7, 3, 2, 2);


// Rotate the ImageView 45 degrees (diagonally)
        redSnakeimageimageView.setRotate(310);
        redSnakeimageimageView.setSmooth(true);
        redSnakeimageimageView.setPreserveRatio(true);
        redSnakeimageimageView.setFitHeight(50);
        redSnakeimageimageView.setFitWidth(50);
        redSnakeimageimageView.setVisible(true);
        StackPane redSnakeimageimageViewstackPane = new StackPane(redSnakeimageimageView);
        dynamicGridPane.add(redSnakeimageimageViewstackPane, 5, 5);


    }

    private void initializeBoardUI(GameBoard gameBoard) throws FileNotFoundException {
        Tile[][] tiles = gameBoard.getTiles();
        for (int i = 0; i < gameBoard.getSize(); i++) {
            for (int j = 0; j < gameBoard.getSize(); j++) {
                // Set all tiles to white color
                tiles[i][j].setFill(Color.WHITE);

                // Set a border to visualize each tile
                tiles[i][j].setStroke(Color.BLACK);
                tiles[i][j].setStrokeWidth(1);

                Text tileIdText = new Text(Integer.toString(tiles[i][j].getTileId()));
                tileIdText.setFill(Color.BLACK); // Set text color to black for better visibility
                tileIdText.setFont(Font.font(12)); // Set font size
                StackPane.setAlignment(tileIdText, Pos.CENTER); // Center align text in StackPane

                StackPane tileStackPane = new StackPane(tiles[i][j], tileIdText);

                // Add the tile to the GridPane
                dynamicGridPane.add(tileStackPane, j, i);
            }
        }

    }
}
