package Controller;

import Model.GameBoard;
import Model.Tile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;

import java.util.concurrent.atomic.AtomicInteger;

public class GameBoardController extends GridPane {

    @FXML
    private GridPane dynamicGridPane;
    @FXML
    private ImageView diceImage;
    @FXML
    private ImageView diceImage2;
    @FXML
    private Text timerLabel;


    @FXML
    private Button rollButton;

    public void initialize(String selectedLevel) throws FileNotFoundException {
        GameBoard gameBoard = new GameBoard(selectedLevel);
        initializeBoardUI(gameBoard);
        placeSnake(selectedLevel);
        initializeTimer();
    }
    Random random = new Random();
    private void initializeTimer() {
        AtomicInteger timerSeconds = new AtomicInteger();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timerSeconds.getAndIncrement();
            timerLabel.setText("Timer: " + timerSeconds + " seconds");
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    @FXML
    void roll(ActionEvent event) {

        rollButton.setDisable(true);

        Thread thread = new Thread(){
            public void run(){
                System.out.println("Thread Running");
                try {
                    for (int i = 0; i < 15; i++) {
                        File file = new File("src/View/photos/dice/dice" + (random.nextInt(5)+1)+".png");
                        File file2 = new File("src/View/photos/dice/dice" + (random.nextInt(5)+1)+".png");
                        diceImage.setImage(new Image(file.toURI().toString()));
                        diceImage2.setImage(new Image(file2.toURI().toString()));
                        Thread.sleep(50);
                    }
                    rollButton.setDisable(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();
    }

    private void placeSnake(String selectedLevel) {          //   NOT READY !!!!
        Image greenSnakeimage = new Image(getClass().getResourceAsStream("/View/Photos/greenSnake.png"));
        Image bludSnakeimage = new Image(getClass().getResourceAsStream("/View/Photos/blueSnake.png"));
        Image yellowSnakeimage = new Image(getClass().getResourceAsStream("/View/Photos/yellowSnake.png"));
        Image redSnakeimage = new Image(getClass().getResourceAsStream("/View/Photos/redSnake.png"));

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
        dynamicGridPane.add(stackPane, 4, 2, 4, 4);


        bludSnakeimageimageView.setRotate(60);
        bludSnakeimageimageView.setSmooth(false);
        bludSnakeimageimageView.setPreserveRatio(false);
        bludSnakeimageimageView.setFitHeight(250);
        bludSnakeimageimageView.setFitWidth(60);
        bludSnakeimageimageView.setVisible(true);
        StackPane bludSnakeimageimageViewstackPane = new StackPane(bludSnakeimageimageView);
        dynamicGridPane.add(bludSnakeimageimageViewstackPane, 1, 6, 3, 5);


        yellowSnakeimageimageView.setRotate(45);
        yellowSnakeimageimageView.setSmooth(false);
        yellowSnakeimageimageView.setPreserveRatio(false);
        yellowSnakeimageimageView.setFitHeight(180);
        yellowSnakeimageimageView.setFitWidth(30);
        yellowSnakeimageimageView.setVisible(true);
        StackPane yellowSnakeimageimageViewstackPane = new StackPane(yellowSnakeimageimageView);
        dynamicGridPane.add(yellowSnakeimageimageViewstackPane, 2, 3, 2, 2);



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
