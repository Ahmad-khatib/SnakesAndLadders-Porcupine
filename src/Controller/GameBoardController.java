package Controller;

import Model.Difficulty;
import Model.Game;
import Model.GameBoard;
import Model.Player;
import Model.Question;
import Model.SystemData;
import Model.Tile;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import Controller.PopUpQuestionController;


public class GameBoardController extends GridPane {
    private static final double ANIMATION_DURATION = 1000;
    private static int snakeIdCounter = 0;
    private static boolean isGameRunning = false;
    private static volatile boolean waitFlag = false;
    private final Random random = new Random();
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
    @FXML
    private Button startNow;
    private CountDownLatch rollLatch;
    private double tileSize;
    private GameBoard gameBoard;
    private int gridSize;
    private Game game = new Game();
    private ArrayList<Player> players = new ArrayList<>();
    private int diceResult = 0;
    private boolean rollButtonClicked = false;
    private int currentPlayerIndex = 0;
    private int gameLevel =0;

    private static void movePlayerWithAnimation(Player player, int newColumnIndex, int newRowIndex, double move) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(ANIMATION_DURATION), player.getIcon());
        transition.setToX((newColumnIndex - player.getColIndex()) * move);
        transition.setToY((newRowIndex - player.getRowIndex()) * move);

        transition.play();
    }

    public void initialize(String selectedLevel, ArrayList<Player> playersN) throws IOException {
        gameBoard = new GameBoard(selectedLevel);
        gameLevel = gameBoard.getDifficultyLevel();
        initializeBoardUI();
        initializeTimer();
        players.clear();
        players = playersN;
        game = new Game(gameBoard.getGameId(), gameBoard, players, SystemData.getInstance().getQuestions());
        rollButton.setVisible(false);
        SnakeAndLaddersPlacment.placeSnakes(selectedLevel, gameBoard, dynamicGridPane);
        // SnakeAndLaddersPlacment.placeLadders(selectedLevel);


        for (int i = 0; i < players.size(); i++) {
            dynamicGridPane.add(players.get(i).getIcon(), 0, gridSize - 1);
            players.get(i).setRowIndex(gridSize - 1);
            players.get(i).setColIndex(0);
            players.get(i).getIcon().setTranslateX(20);
        }


    }

    private void initializeBoardUI() {
        Tile[][] tiles = gameBoard.getTiles();
        this.gridSize = gameBoard.getSize();

        for (int i = 0; i < gameBoard.getSize(); i++) {
            for (int j = 0; j < gameBoard.getSize(); j++) {
                // Access the Rectangle object within each Tile
                Rectangle tileRectangle = tiles[i][j].getTileRectangle();
                // Set properties of the Rectangle object
                if (tiles[i][j].getTileType().equals(Tile.TileType.QUESTION)) {
                    tileRectangle.setFill(Color.RED);
                }
                if (tiles[i][j].getTileType().equals(Tile.TileType.SURPRISE_JUMP)) {
                    tileRectangle.setFill(Color.YELLOW);
                } else if (tiles[i][j].getTileType().equals(Tile.TileType.NORMAL)) {
                    tileRectangle.setFill(Color.WHITE);
                }
                tileRectangle.setStroke(Color.BLACK);
                tileRectangle.setWidth(gameBoard.getPreferredTileSize());
                tileRectangle.setHeight(gameBoard.getPreferredTileSize());
                tileRectangle.setStrokeWidth(1);
                tiles[i][j].setId("cell_" + i + "_" + j);
                Text tileIdText = new Text(Integer.toString(tiles[i][j].getTileId()));
                tileIdText.setFill(Color.BLACK);
                tileIdText.setFont(Font.font(12));
                StackPane.setAlignment(tileIdText, Pos.CENTER);
                StackPane tileStackPane = new StackPane(tiles[i][j], tileIdText);
                dynamicGridPane.add(tileStackPane, j, i);
            }
        }
    }

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
    private void handlerolButtonClicked() throws InterruptedException {
        if (!isGameRunning) {
            startGame();
        } else {
            int diceSum = roll();
            movePlayer(players.get(currentPlayerIndex), diceSum);

            // Check for game completion
            if (game.isGameFinished()) {
                System.out.println("Game Over!");
                // Handle game-over logic (e.g., display winner)
            } else {
                // Update to the next player's turn
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            }
        }
    }

    private void startGame() {
        // Additional setup for starting the game if needed
        isGameRunning = true;
        rollButton.setVisible(true);
        currentPlayerIndex = 0;
        // Additional setup for the first player's turn
        // (if any specific actions need to be taken at the beginning)
    }

    private void movePlayer(Player player, int moves) {
        player.movePlayerTo(player.getPlayerPosition() + moves, gameBoard.getSize());
        int newRow = player.getPlayerPosition() % gameBoard.getSize() == 0 ? (gameBoard.getSize() - (player.getPlayerPosition() / gameBoard.getSize())) : gameBoard.getSize() - ((player.getPlayerPosition() / gameBoard.getSize()) + 1);
        int newCol = player.getPlayerPosition() % gameBoard.getSize() == 0 ? gameBoard.getSize() - 1 : ((player.getPlayerPosition() % gameBoard.getSize()) - 1);
        movePlayerWithAnimation(player, newCol, newRow, gameBoard.getCellHeight());
        // Check the type of tile the player is moving to
        Tile.TileType tileType = gameBoard.getTiles()[newRow][newCol].getTileType();
        switch (tileType) {
            case NORMAL:
                // Perform actions for a normal tile (if any)
                break;
            case QUESTION:
                // Perform actions for a question tile (if any)
                handleQuestionTile(player);
                break;
            case SURPRISE_JUMP:
                // Perform actions for a surprise jump tile (if any)
                handleSurpriseJumpTile(player);
                break;
            default:
                break;
        }
        if (player.getPlayerPosition() == gridSize * gridSize) {
            game.setGameFinished(true);
        }

    }

    public void handleQuestionTile(Player player) {

        switch (gameLevel) {
            case 1:
                movePlayer(player,loadQuestionPopUp(Difficulty.EASY));
                break;
            case 2:
                movePlayer(player,loadQuestionPopUp(Difficulty.MEDIUM));
                break;
            case 3:
                movePlayer(player,loadQuestionPopUp(Difficulty.HARD));
                break;
            default:
                break;
        }


    }

    private int loadQuestionPopUp(Difficulty d) {
        int stepsToMove = 0;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"ok", ButtonType.OK);
        alert.show();
        // Load the FXML for the question window
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("src/View/PopUpQuestion.fxml")); //ihave aproblem here

            Parent root = loader.load();
            // Create the scene and show the window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Question");
            stage.initModality(Modality.APPLICATION_MODAL); // Ensure the window is modal
            stage.showAndWait(); // Wait for the user to close the window
            // Get the controller instance
            PopUpQuestionController controller = loader.getController();

            // Retrieve the question based on the difficulty and set it in the controller
            Question question = SystemData.getInstance().popQuestion(d);
            controller.setQuestion(question.getText(), question.getAnswer1(), question.getAnswer2(), question.getAnswer3(), question.getAnswer4(), question.getCorrectAnswer());



            // Get the steps to move from the controller
            stepsToMove = controller.getStepsToMove();
            // Handle the steps to move based on the game logic

        } catch (IOException e) {
            e.printStackTrace();
        }
        return stepsToMove;
    }

    private void movePlayerBack(Player player, int steps) {
        int newPosition = player.getPlayerPosition() - steps;
        if (newPosition < 1) {
            newPosition = 1; // Ensure the player doesn't go beyond the start position
        }
        movePlayer(player, newPosition - player.getPlayerPosition());
    }

    private void handleSurpriseJumpTile(Player player) {
        // Add logic to handle actions when a player lands on a surprise jump tile
        // For example, move the player to a different position on the board
    }

    @FXML
    private void handleStartNow() {
        rollButton.setVisible(true);
        startNow.setVisible(false);
    }

    @FXML
    public int roll() {
        rollButton.setDisable(true);

        int totalSum = 0;
        int finalDice1Value = 0;
        int finalDice2Value = 0;

        Timeline timeline = new Timeline();

        for (int i = 0; i < 20; i++) {
            int dice1Value = (random.nextInt(5) + 1);
            int dice2Value = (random.nextInt(5) + 1);

            File file = new File("src/View/photos/dice/dice" + dice1Value + ".png");
            File file2 = new File("src/View/photos/dice/dice" + dice2Value + ".png");

            Image image1 = new Image(file.toURI().toString());
            Image image2 = new Image(file2.toURI().toString());

            finalDice1Value = dice1Value;
            finalDice2Value = dice2Value;

            int finalI = i;
            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.millis(100 * finalI), event -> {
                        diceImage.setImage(image1);
                        diceImage2.setImage(image2);
                    })
            );

            totalSum = finalDice1Value + finalDice2Value;
        }

        timeline.setOnFinished(event -> {
            // Update UI after animation is complete
            rollButton.setDisable(false);
        });

        timeline.play();
        return totalSum;
    }
}