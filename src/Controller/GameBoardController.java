package Controller;

import Model.*;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
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
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static Model.Snake.SnakeColor.RED;


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
    @FXML
    private Button backButton;
    @FXML
    private Label currentPlayer;
    @FXML
    private ImageView currentPlayerIcon;

    private CountDownLatch rollLatch;
    private double tileSize;
    private GameBoard gameBoard;
    private int gridSize;
    private Game game = new Game();
    private ArrayList<Player> players = new ArrayList<>();
    private int diceResult = 0;
    private boolean rollButtonClicked = false;
    private int currentPlayerIndex = 0;
    static Set<Integer> usedHeadPositions = new HashSet<>();
    static Set<Integer> usedTailPositions = new HashSet<>();
    boolean playedMyTurn;


    public void initialize(String selectedLevel, ArrayList<Player> playersN) throws IOException {
        gameBoard = new GameBoard(selectedLevel);
        initializeBoardUI();
        initializeTimer();
        players.clear();
        players = playersN;
        game = new Game(gameBoard.getGameId(), gameBoard, players, SystemData.getInstance().getQuestions());
        rollButton.setVisible(false);
        SnakeAndLaddersPlacment.placeSnakes(selectedLevel, gameBoard, dynamicGridPane);
        // SnakeAndLaddersPlacment.placeLadders(selectedLevel, gameBoard, dynamicGridPane);
        usedHeadPositions = SnakeAndLaddersPlacment.usedHeadPositions;
        usedTailPositions = SnakeAndLaddersPlacment.usedTailPositions;
        currentPlayer.setText("Welcome to Game Snakes and Ladders\n by Porcupine");



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
            handleStartNow();
        } else {
            int diceSum = roll();
            playedMyTurn = false;
            movePlayer(players.get(currentPlayerIndex), diceSum);

            // Check for game completion
            if (game.isGameFinished()) {
                Game gameToSave = new Game(players.get(currentPlayerIndex).getPlayerName(),timerLabel.getText(), game.getGAMELEVEL());
                showGameOverDialog(players.get(currentPlayerIndex).getPlayerName(), (Stage) rollButton.getScene().getWindow());

                currentPlayer.setText("The game is over and the winner is : "+players.get(currentPlayerIndex).getPlayerName());

                currentPlayerIcon.setImage(players.get(currentPlayerIndex).getIcon().getImage());

            } else {
                // Update to the next player's turn
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

            }
        }
    }

    /*private void startGame() {
        // Additional setup for starting the game if needed
        isGameRunning = true;
        currentPlayerIndex = 0;
        currentPlayer.setText("Current Player: "+players.get(currentPlayerIndex).getPlayerName());
        currentPlayerIcon.setImage(players.get(currentPlayerIndex).getIcon().getImage());
        // Additional setup for the first player's turn
        // (if any specific actions need to be taken at the beginning)
    }*/


    private void movePlayer(Player player, int moves) {
        player.movePlayerTo(player.getPlayerPosition() + moves, gameBoard.getSize());
        int newRow = player.getPlayerPosition() % gameBoard.getSize() == 0 ? (gameBoard.getSize() - (player.getPlayerPosition() / gameBoard.getSize())) : gameBoard.getSize() - ((player.getPlayerPosition() / gameBoard.getSize()) + 1);
        int newCol = player.getPlayerPosition() % gameBoard.getSize() == 0 ? gameBoard.getSize() - 1 : ((player.getPlayerPosition() % gameBoard.getSize()) - 1);

        movePlayerWithAnimation(player, newCol, newRow, gameBoard.getCellHeight(), () -> {
            if(playedMyTurn == false)
                handleAfterMove(player);
        });
        // Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5)));
        // timeline.play();
    }

    private void finishedTurn(Player player) {
        currentPlayer.setText("Current Player: " + players.get((currentPlayerIndex) % players.size()).getPlayerName());
        currentPlayerIcon.setImage(players.get((currentPlayerIndex) % players.size()).getIcon().getImage());

        // Create a Timeline with a KeyFrame to wait for three seconds
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
            // Code to be executed after three seconds
            // (If there's anything specific you want to do, you can add it here)
        }));

        // Play the timeline
        timeline.play();
    }

    private static void movePlayerWithAnimation(Player player, int newColumnIndex, int newRowIndex, double move, Runnable callback) {
        TranslateTransition horizontalTransition = new TranslateTransition(Duration.millis(ANIMATION_DURATION), player.getIcon());
        horizontalTransition.setToX((newColumnIndex - player.getColIndex()) * move);

        TranslateTransition verticalTransition = new TranslateTransition(Duration.millis(ANIMATION_DURATION), player.getIcon());
        verticalTransition.setToY((newRowIndex - player.getRowIndex()) * move);

        ParallelTransition parallelTransition = new ParallelTransition(horizontalTransition, verticalTransition);
        parallelTransition.play();
        // Set up a callback to be executed after the animation is complete
        parallelTransition.setOnFinished(event -> {
            callback.run();
        });

    }

    private void handleAfterMove(Player player) {
        playedMyTurn =true;
        int newRow = player.getPlayerPosition() % gameBoard.getSize() == 0 ? (gameBoard.getSize() - (player.getPlayerPosition() / gameBoard.getSize())) : gameBoard.getSize() - ((player.getPlayerPosition() / gameBoard.getSize()) + 1);
        int newCol = player.getPlayerPosition() % gameBoard.getSize() == 0 ? gameBoard.getSize() - 1 : ((player.getPlayerPosition() % gameBoard.getSize()) - 1);
        if (player.getPlayerPosition() == gridSize * gridSize) {
            game.setGameFinished(true);
        }

        if (usedHeadPositions.contains(player.getPlayerPosition())) {

            for (int i = 0; i < gameBoard.getSnakes().size(); i++) {

                if (gameBoard.getSnakes().get(i).getHeadPosition() == player.getPlayerPosition() && gameBoard.getSnakes().get(i).getColor() != RED) {
                    int stepsBack = gameBoard.getSnakes().get(i).getTailPosition() - gameBoard.getSnakes().get(i).getHeadPosition();
                    movePlayer(player, stepsBack);
                }
                if (gameBoard.getSnakes().get(i).getHeadPosition() == player.getPlayerPosition() && gameBoard.getSnakes().get(i).getColor().equals(RED)) {
                    int stepsBack =-( player.getPlayerPosition() - 1);
                    movePlayer(player, stepsBack);
                }
                finishedTurn(player);
            }
        }
        Tile.TileType tileType = gameBoard.getTiles()[newRow][newCol].getTileType();
        switch (tileType) {
            case NORMAL:
                finishedTurn(player);
                break;
            case QUESTION:

                handleQuestionTile(player);
                break;
            case SURPRISE_JUMP:
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
        int stepsToMove = 0;

        switch (gameBoard.getDifficultyLevel()) {
            case 1:
                stepsToMove = loadQuestionPopUp(Difficulty.EASY, player, () -> {
                });
                break;
            case 2:
                stepsToMove = loadQuestionPopUp(Difficulty.MEDIUM, player, () -> {
                });
                break;
            case 3:
                stepsToMove = loadQuestionPopUp(Difficulty.HARD, player, () -> {
                });
                break;
            default:
                break;
        }

        // You can add any additional logic here that should be executed after the callback (outside the switch statement)
    }



    @FXML
    private void handlebutton(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("src/View/EditQuestion.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) backButton.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private int loadQuestionPopUp(Difficulty difficulty, Player player, Runnable callback) {
        final int[] stepsToMove = {0}; // Using an array to store the result

        Platform.runLater(() -> {
            try {
                // Load the FXML for the question window
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/QuestionPopUp.fxml"));
                Parent root = loader.load();
                PopUpQuestionController controller = loader.getController();

                // Retrieve the question based on the difficulty and set it in the controller
                Question question = SystemData.getInstance().popQuestion(difficulty);
                controller.setQuestion(question);

                // Create the stage for the pop-up window
                Stage stage = new Stage();
                stage.setTitle("Question");
                stage.setScene(new Scene(root));

                // Set the modality to APPLICATION_MODAL
                stage.initModality(Modality.APPLICATION_MODAL);

                // Set up a callback to be executed after the user submits an answer
                controller.setSubmitCallback(() -> {
                    stepsToMove[0] = controller.getStepsToMove();
                    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> stage.close()));
                    timeline.play();
                    movePlayer(player, stepsToMove[0]);
                    finishedTurn(player);

                    // Execute the provided callback
                    callback.run();
                });

                // Show the pop-up window and wait for the user to close it
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return stepsToMove[0];
    }

    private void movePlayerBack(Player player, int steps) {
        int newPosition = player.getPlayerPosition() - steps;
        if (newPosition < 1) {
            newPosition = 1; // Ensure the player doesn't go beyond the start position
        }
        movePlayer(player, newPosition - player.getPlayerPosition());
    }

    private void handleSurpriseJumpTile(Player player) {
        boolean moveForward = random.nextBoolean();
        int steps = moveForward ? 10 : -10;

        movePlayer(player, steps);

        String direction = moveForward ? "forward" : "backward";
        System.out.println("Player moved " + direction + " by 10 steps.");
    }


    @FXML
    private void handleStartNow() {
        rollButton.setVisible(true);
        startNow.setVisible(false);
        isGameRunning = true;
        currentPlayerIndex = 0;
        currentPlayer.setText("Current Player: "+players.get(currentPlayerIndex).getPlayerName());
        currentPlayerIcon.setImage(players.get(currentPlayerIndex).getIcon().getImage());


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

    public static void showGameOverDialog(String winnerName, Stage primaryStage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("The game has ended.");
        alert.setContentText("Winner: " + winnerName + "\nThank you for playing!");

        // Add a button to go back to the main page
        ButtonType backButtonType = new ButtonType("Back to Main Page");
        // Add a button to close the application
        ButtonType exitButtonType = new ButtonType("Exit");
        alert.getButtonTypes().setAll(backButtonType, exitButtonType);

        // Handle the back button action
        alert.setOnCloseRequest(event -> {
            if (alert.getResult() == backButtonType) {
                goBack(primaryStage);
            } else if (alert.getResult() == exitButtonType) {
                System.exit(0);
            }
        });

        // Show the alert and wait for the user's response
        alert.showAndWait();
    }

    private static void goBack(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(GameBoardController.class.getResource("/View/MainPage.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
