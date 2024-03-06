package Controller;

import Model.*;
import javafx.animation.*;
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
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
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
    static Set<Integer> usedHeadPositions = new HashSet<>();
    static Set<Integer> usedTailPositions = new HashSet<>();
    static Set<Integer> usedTopPositions = new HashSet<>();
    static Set<Integer> usedBottomPositions = new HashSet<>();
    private static final int snakeIdCounter = 0;
    private static boolean isGameRunning = false;
    private static final boolean waitFlag = false;
    private final Random random = new Random();
    boolean playedMyTurn;
    @FXML
    private ImageView supriseJump;
    @FXML
    private GridPane dynamicGridPane;
    @FXML
    private ImageView diceImage;
    @FXML
    private Text timerLabel;
    @FXML
    private Button rollButton;
    @FXML
    private Button startNow;
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
    private final int diceResult = 0;
    private final boolean rollButtonClicked = false;
    private int currentPlayerIndex = 0;

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

    public static void showGameOverDialog(String winnerName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("The game has ended.");
        alert.setContentText("Winner: " + winnerName + "\nThank you for playing!");

        // Add a button to close the application
        ButtonType exitButtonType = new ButtonType("Exit");
        alert.getButtonTypes().setAll(exitButtonType);

        // Handle the exit button action
        alert.setOnCloseRequest(event -> System.exit(0));

        // Show the alert and wait for the user's response
        alert.show();
    }

    public void initialize(String selectedLevel, ArrayList<Player> playersN) throws IOException {
        gameBoard = new GameBoard(selectedLevel);
        initializeBoardUI();

        players.clear();
        players = playersN;
        game = new Game(gameBoard.getGameId(), gameBoard, players, SystemData.getInstance().getQuestions());
        rollButton.setVisible(false);
        SnakeAndLaddersPlacment.placeSnakes(selectedLevel, gameBoard, dynamicGridPane);
        SnakeAndLaddersPlacment.placeLadders(selectedLevel, gameBoard, dynamicGridPane);
        usedHeadPositions = SnakeAndLaddersPlacment.usedHeadPositions;
        usedTailPositions = SnakeAndLaddersPlacment.usedTailPositions;
        usedTopPositions = SnakeAndLaddersPlacment.usedTopPositions;
        usedBottomPositions = SnakeAndLaddersPlacment.usedBottomPositions;

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
                    // Define the base color
                    double hue = 120; // Green hue
                    double saturation = 0.5; // Adjust the saturation level as needed
                    double opacity = 1.0; // Adjust the opacity as needed

                    // Calculate the distance from the center of the grid
                    double centerX = (tiles.length - 1) / 2.0;
                    double centerY = (tiles[i].length - 1) / 2.0;
                    double distance = Math.sqrt(Math.pow(i - centerX, 2) + Math.pow(j - centerY, 2));

                    // Normalize the distance to range from 0 to 1
                    double maxDistance = Math.sqrt(Math.pow(centerX, 2) + Math.pow(centerY, 2));
                    double normalizedDistance = distance / maxDistance;

                    // Use a smoother function to calculate brightness (e.g., quadratic)
                    double brightness = 1 - Math.pow(normalizedDistance, 4);

                    // Avoid reaching zero brightness to prevent black color
                    brightness = Math.max(brightness, 0.4);

                    // Set the fill color based on the brightness with gradient effect
                    LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                            new Stop(0, Color.hsb(hue, saturation, brightness, opacity)),
                            new Stop(1, Color.hsb(hue, saturation, brightness * 0.8, opacity)));
                    tileRectangle.setFill(gradient);
                }
                double arcSize = 10; // Adjust the arc size as needed
                tileRectangle.setArcWidth(arcSize);
                tileRectangle.setArcHeight(arcSize);
                tileRectangle.setStroke(Color.BLACK);
                tileRectangle.setWidth(gameBoard.getPreferredTileSize());
                tileRectangle.setHeight(gameBoard.getPreferredTileSize());
                tileRectangle.setStrokeWidth(1);
                tiles[i][j].setId("cell_" + i + "_" + j);
                Text tileIdText = new Text(Integer.toString(tiles[i][j].getTileId()));
                tileIdText.setFill(Color.BLACK);
                tileIdText.setFont(Font.font("Jokerman", 12));

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
    private void handleStartNow() {
        initializeTimer();
        rollButton.setVisible(true);
        startNow.setVisible(false);
        isGameRunning = true;
        currentPlayerIndex = 0;
        currentPlayer.setText("Current Player: " + players.get(currentPlayerIndex).getPlayerName());
        currentPlayerIcon.setImage(players.get(currentPlayerIndex).getIcon().getImage());


    }

    @FXML
    private void handlerollButtonClicked() throws InterruptedException {
        if (!isGameRunning) {
            handleStartNow();
        } else {
            int result = roll();
            switch (result) {
                case 7:
                    loadQuestionPopUp(Difficulty.EASY, players.get(currentPlayerIndex), () -> {
                    });
                    break;
                case 8:
                    loadQuestionPopUp(Difficulty.MEDIUM, players.get(currentPlayerIndex), () -> {
                    });
                    break;
                case 9:
                    loadQuestionPopUp(Difficulty.HARD, players.get(currentPlayerIndex), () -> {
                    });
                    break;
                default:
                    movePlayer(players.get(currentPlayerIndex), result);
                    break;
            }

            playedMyTurn = false;

            // Update to the next player's turn
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

        }
    }

    private void movePlayer(Player player, int moves) {
        player.movePlayerTo(player.getPlayerPosition() + moves, gameBoard.getSize());
        int newRow = player.getPlayerPosition() % gameBoard.getSize() == 0 ? (gameBoard.getSize() - (player.getPlayerPosition() / gameBoard.getSize())) : gameBoard.getSize() - ((player.getPlayerPosition() / gameBoard.getSize()) + 1);
        int newCol = player.getPlayerPosition() % gameBoard.getSize() == 0 ? gameBoard.getSize() - 1 : ((player.getPlayerPosition() % gameBoard.getSize()) - 1);

        // Introduce a 3-second delay using PauseTransition
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> {
            movePlayerWithAnimation(player, newCol, newRow, gameBoard.getCellHeight(), () -> {
                if (!playedMyTurn) {
                    handleAfterMove(player);
                }
            });

        });

        pause.play();
    }

    private void handleAfterMove(Player player) {
        playedMyTurn = true;
        int newRow = player.getPlayerPosition() % gameBoard.getSize() == 0 ? (gameBoard.getSize() - (player.getPlayerPosition() / gameBoard.getSize())) : gameBoard.getSize() - ((player.getPlayerPosition() / gameBoard.getSize()) + 1);
        int newCol = player.getPlayerPosition() % gameBoard.getSize() == 0 ? gameBoard.getSize() - 1 : ((player.getPlayerPosition() % gameBoard.getSize()) - 1);

        if (usedHeadPositions.contains(player.getPlayerPosition())) {

            for (int i = 0; i < gameBoard.getSnakes().size(); i++) {

                if (gameBoard.getSnakes().get(i).getHeadPosition() == player.getPlayerPosition() && gameBoard.getSnakes().get(i).getColor() != RED) {
                    int stepsBack = gameBoard.getSnakes().get(i).getTailPosition() - gameBoard.getSnakes().get(i).getHeadPosition();
                    movePlayer(player, stepsBack);
                }
                if (gameBoard.getSnakes().get(i).getHeadPosition() == player.getPlayerPosition() && gameBoard.getSnakes().get(i).getColor().equals(RED)) {
                    int stepsBack = -(player.getPlayerPosition() - 1);
                    movePlayer(player, stepsBack);
                }
            }
        }
        if (usedBottomPositions.contains(player.getPlayerPosition())) {
            for (int i = 0; i < gameBoard.getLadders().size(); i++) {
                if (gameBoard.getLadders().get(i).getBottomPosition() == player.getPlayerPosition()) {
                    int stepsForward = gameBoard.getLadders().get(i).getTopPosition() - gameBoard.getLadders().get(i).getBottomPosition();
                    movePlayer(player, stepsForward);
                }

            }
        }
        Tile tile = gameBoard.getTiles()[newRow][newCol];
        Tile.TileType tileType = tile.getTileType();
        switch (tileType) {
            case NORMAL:
                finishedTurn(player);
                break;
            case QUESTION:
                handleQuestionTile(player, tile.getDifficulty()); //example
                break;
            case SURPRISE_JUMP:
                handleSurpriseJumpTile(player);
                break;
            default:
                break;
        }

    }

    private void finishedTurn(Player player) {
        if (player.getPlayerPosition() == gridSize * gridSize) {
            game.setGameFinished(true);
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
                Game gameToSave = new Game(players.get(currentPlayerIndex).getPlayerName(), timerLabel.getText(), GameBoard.getLevel());
                SystemData.getInstance().addGameToHistory(gameToSave);
                showGameOverDialog(player.getPlayerName());
                currentPlayer.setText("The game is over and the winner is : " + players.get(currentPlayerIndex).getPlayerName());
                currentPlayerIcon.setImage(players.get(currentPlayerIndex).getIcon().getImage());
            }));

            // Play the timeline
            timeline.play();
        } else {
            currentPlayer.setText("Current Player: " + players.get((currentPlayerIndex) % players.size()).getPlayerName());
            currentPlayerIcon.setImage(players.get((currentPlayerIndex) % players.size()).getIcon().getImage());
            rollButton.setDisable(false);
        }
    }

    public void handleQuestionTile(Player player, Difficulty d) {
        loadQuestionPopUp(d, player, () -> {
            // Add any logic to execute after the callback if needed
        });
    }

    private void handleSurpriseJumpTile(Player player) {
        boolean moveForward = random.nextBoolean();
        int steps = moveForward ? 10 : -10;

        movePlayer(player, steps);

        String direction = moveForward ? "forward" : "backward";
        System.out.println("Player moved " + direction + " by 10 steps.");
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
                // Disable window close button (X)
                stage.setOnCloseRequest(event -> event.consume());
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

    private int getRandomNumber(int[] numbers) {

        return numbers[random.nextInt(numbers.length)];
    }

    @FXML
    public int roll() {
        int diceValue = 0;
        rollButton.setDisable(true);
        switch (gameBoard.getDifficultyLevel()) {
            case 1:
                diceValue = getRandomNumber(new int[]{0, 1, 2, 3, 4, 7, 8, 9});
                break;
            case 2:
                diceValue = getRandomNumber(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 7, 8, 8, 9, 9});
                break;
            case 3:
                diceValue = getRandomNumber(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 7, 8, 8, 9, 9, 9, 9});
                break;
        }

        Timeline timeline = new Timeline();

        for (int i = 0; i < 20; i++) {

            File file = new File("src/View/photos/dice/dice" + diceValue + ".png");

            Image image1 = new Image(file.toURI().toString());


            int finalI = i;
            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.millis(100 * finalI), event -> {
                        diceImage.setImage(image1);
                    })
            );

        }

        timeline.setOnFinished(event -> {

        });

        timeline.play();
        return diceValue;
    }
}
