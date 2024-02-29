package Controller;

import Model.GameBoard;
import Model.Ladder;
import Model.Snake;
import Model.Tile;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Clock;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
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

    private final Random random = new Random();
    private double tileSize;

    public void initialize(String selectedLevel) throws FileNotFoundException {
        GameBoard gameBoard = new GameBoard(selectedLevel);
        Set<Integer> usedHeadPositions = new HashSet<>();
        Set<Integer> usedTailPositions = new HashSet<>();
        initializeBoardUI(gameBoard);
        placeSnakes(selectedLevel, usedHeadPositions, usedTailPositions); // Pass the sets of used head and tail positions
        initializeTimer();
        placeLadders(selectedLevel, usedHeadPositions, usedTailPositions); // Pass the sets of used head and tail positions
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
    void roll() {
        rollButton.setDisable(true);

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (int i = 0; i < 15; i++) {
                    File file = new File("src/View/photos/dice/dice" + (random.nextInt(5) + 1) + ".png");
                    File file2 = new File("src/View/photos/dice/dice" + (random.nextInt(5) + 1) + ".png");
                    Image image1 = new Image(file.toURI().toString());
                    Image image2 = new Image(file2.toURI().toString());
                    Platform.runLater(() -> {
                        diceImage.setImage(image1);
                        diceImage2.setImage(image2);
                    });
                    Thread.sleep(50);
                }
                Platform.runLater(() -> rollButton.setDisable(false));
                return null;
            }
        };

        new Thread(task).start();
    }


    private void placeSnakes(String selectedLevel, Set<Integer> usedHeadPositions, Set<Integer> usedTailPositions) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                GameBoard gameBoard = new GameBoard(selectedLevel);
                int gridSize = gameBoard.getSize();
                tileSize = calculateTileSize(gridSize); // Assign tileSize

                // Determine the number of snakes and their counts based on the selected level
                int numberOfSnakes = determineNumberOfSnakes(selectedLevel);
                int[] snakeCounts = getSnakeCounts(selectedLevel);


                Snake.SnakeColor[] snakeColors = Snake.SnakeColor.values();

                // Loop through each snake count for each color
                for (int colorIndex = 0;colorIndex < snakeCounts.length; colorIndex++) {
                    Snake.SnakeColor color = snakeColors[colorIndex];
                    int snakeCount = snakeCounts[colorIndex];
                    int generatedSnakes = 0;
                    // Place the specified number of snakes for the current color
                    for (int j = 0; j < snakeCount && generatedSnakes < snakeCount; j++) {
                        // Generate a unique snake and check for valid positions
                        Snake snake = generateUniqueSnake(colorIndex , color, usedHeadPositions, usedTailPositions, gridSize,generatedSnakes,snakeCount,selectedLevel);
                        if (snake != null) {
                            int headRow = gridSize - 1 - snake.getHeadPosition() / gridSize;
                            int headCol = snake.getHeadPosition() % gridSize;
                            int tailRow = gridSize - 1 - snake.getTailPosition() / gridSize;
                            int tailCol = snake.getTailPosition() % gridSize;

                            // Ensure the snake is within the grid bounds
                            if (headCol >= gridSize) {
                                headCol = gridSize - 1;
                            }
                            if (tailCol >= gridSize) {
                                tailCol = gridSize - 1;
                            }

                            // Create snake image views and stack panes
                            ImageView snakeHeadImageView = createSnakeImageView(color, tileSize, snake.getSnakeEffect(), gameBoard.getCellHeight());
                          //  ImageView snakeTailImageView = createSnakeImageView(color, tileSize, snake.getSnakeEffect(), gameBoard.getCellHeight());
                            StackPane snakeHeadStackPane = new StackPane(snakeHeadImageView);
                            //StackPane snakeTailStackPane = new StackPane(snakeTailImageView);
                            snakeHeadStackPane.setAlignment(Pos.CENTER);
                           // snakeTailStackPane.setAlignment(Pos.BOTTOM_RIGHT);

                            // Set size constraints for snake head and tail stack panes
                            snakeHeadStackPane.setMaxSize(tileSize, tileSize);
                            snakeHeadStackPane.setPrefSize(tileSize, tileSize);
                            snakeHeadStackPane.setMinSize(tileSize, tileSize);
                          //  snakeTailStackPane.setMaxSize(tileSize, tileSize);
                           // snakeTailStackPane.setPrefSize(tileSize, tileSize);
                          //  snakeTailStackPane.setMinSize(tileSize, tileSize);

                            // Add snake images to the grid pane
                            int finalHeadCol = headCol;
                            int finalTailCol = tailCol;
                            Platform.runLater(() -> {
                                dynamicGridPane.add(snakeHeadStackPane, finalHeadCol, headRow);
                              //  dynamicGridPane.add(snakeTailStackPane, finalTailCol, tailRow);
                            });

                            // Calculate positions of snake head and tail within the cell
                            double headX, headY, tailX, tailY;
                            headX = (headCol + 0.5) * tileSize - snakeHeadImageView.getImage().getWidth() / 2;
                            headY = (headRow + 0.5) * tileSize - snakeHeadImageView.getImage().getHeight() / 2;
                           // tailX = (tailCol + 0.5) * tileSize - snakeTailImageView.getImage().getWidth() / 2;
                          //  tailY = (tailRow + 0.5) * tileSize - snakeTailImageView.getImage().getHeight() / 2;

                            // Mirror image if head is in the first left column
                            if (headCol == 0) {
                                snakeHeadImageView.setScaleX(-1);
                               // snakeTailImageView.setScaleX(-1);
                            }

                            // Set layout positions for snake head and tail
                            snakeHeadStackPane.setLayoutX(headX);
                            snakeHeadStackPane.setLayoutY(headY);
                          //  snakeTailStackPane.setLayoutX(tailX);
                          //  snakeTailStackPane.setLayoutY(tailY);

                            // Update used positions with new snake head and tail positions
                            usedHeadPositions.add(snake.getHeadPosition());
                            usedTailPositions.add(snake.getTailPosition());

                            // Increment snake index for the next iteration
                            generatedSnakes++;
                        }
                    }
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    private ImageView createSnakeImageView(Snake.SnakeColor color, double tileSize, Snake.Effect effect, double cellHeight) {
        // Calculate the scaled height based on the snake's effect and the cell height
        double scaledHeight;
        if (effect.equals("YELLOW")) {
            scaledHeight = cellHeight * 2.0;
        } else if (effect.equals("GREEN")) {
            scaledHeight = cellHeight * 3.0;
        } else if (effect.equals("BLUE")) {
            scaledHeight = cellHeight * 4.0;
        } else if (effect.equals("RED")) {
            scaledHeight = cellHeight * 0.5;
        } else {
            // Handle default case if necessary
            scaledHeight = 0.0; // Or any other default value
        }


        // Construct the image path based on the snake color
        String colorName = color.toString().toLowerCase();
        String imagePath = "/View/Photos/" + colorName + "Snake.png";

        // Load the image with the calculated dimensions
        Image snakeImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)), tileSize, scaledHeight, true, true);

        // Create the ImageView with the scaled image
        ImageView snakeImageView = new ImageView(snakeImage);
        snakeImageView.setFitWidth(tileSize); // Set the width to match the initial tile size
        snakeImageView.setFitHeight(scaledHeight); // Set the height to match the adjusted scaled height
        return snakeImageView;
    }

    private Snake generateUniqueSnake(int snakeIndex, Snake.SnakeColor color, Set<Integer> usedHeadPositions, Set<Integer> usedTailPositions, int gridSize,int generatedSnakes, int snakeCount,String selectedLevel) {
        while (true) {
            Snake snake = Snake.generateRandomSnake(snakeIndex, selectedLevel, gridSize,color);// Check the snake's color and adjust head position accordingly
            int headPos = snake.getHeadPosition();
            int tailPos = snake.getTailPosition();

            switch (color) {
                case GREEN:
                    // Ensure the head is not in the first 3 rows
                    if (headPos / gridSize >= gridSize - 3) {
                        continue; // Skip this snake and try again
                    }
                    break;
                case BLUE:
                    // Ensure the head is not in the first 4 rows
                    if (headPos / gridSize >= gridSize - 4) {
                        continue; // Skip this snake and try again
                    }
                    break;
                case YELLOW:
                    // Ensure the head is not in the first 2 rows
                    if (headPos / gridSize >= gridSize - 2) {
                        continue; // Skip this snake and try again
                    }
                    break;
                case RED:
                    // Ensure the head is in the center of the board
                    int centerPosition=0;
                    switch (gridSize) {
                        case 7:
                            centerPosition = 24; // Center position for a 7x7 grid
                            break;
                        case 10:
                            centerPosition = 50; // Center position for a 10x10 grid
                            break;
                        case 13:
                            centerPosition = 84; // Center position for a 13x13 grid
                            break;
                        default:
                            return null; // Unsupported grid size
                    }
                    break;
                default:
                    break; // No constraint for other snake colors
            }

            // Check if the head and tail positions are unique and tail position doesn't exceed the grid pane
            if (!usedHeadPositions.contains(headPos) && !usedTailPositions.contains(headPos) &&
                    !usedHeadPositions.contains(tailPos) && !usedTailPositions.contains(tailPos) &&
                    !(tailPos % gridSize == 0 && tailPos != headPos - 1)) {
                return snake;
            }
        }
    }

    private int determineNumberOfSnakes(String selectedLevel) {
        if (selectedLevel.equals("Easy")) {
            return 4;
        } else if (selectedLevel.equals("Medium")) {
            return 6;
        } else if (selectedLevel.equals("Hard")) {
            return 8;
        } else {
            return 0; // Default case
        }
    }


    private int[] getSnakeCounts(String selectedLevel) {
        if (selectedLevel.equals("Easy")) {
            return new int[]{1, 1, 1, 1}; // 1 snake of each color
        } else if (selectedLevel.equals("Medium")) {
            return new int[]{1, 1, 2, 2}; // 2 red, 2 green, 1 blue, 1 yellow
        } else if (selectedLevel.equals("Hard")) {
            return new int[]{2, 2, 2, 2}; // 2 red, 2 green, 2 blue, 2 yellow
        } else {
            return new int[4]; // Default to an array of length 4 (all zeros)
        }
    }




    private void placeLadders(String selectedLevel, Set<Integer> usedHeadPositions, Set<Integer> usedTailPositions) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                GameBoard gameBoard = new GameBoard(selectedLevel);
                int gridSize = gameBoard.getSize();
                tileSize = calculateTileSize(gridSize); // Assign tileSize here

                // Determine the number of ladders and their counts based on the selected level
                int numberOfLadders = determineNumberOfLadders(selectedLevel);

                Set<Integer> usedTopPositions = new HashSet<>();
                Set<Integer> usedBottomPositions = new HashSet<>();
                Set<Integer> usedColumns = new HashSet<>(); // Track used columns

                int ladderIndex = 0; // Initialize ladder index

                // Loop to place ladders
                for (int i = 0; i < numberOfLadders; i++) {
                    // Generate a unique ladder and check for valid positions
                    Ladder ladder = generateUniqueLadder(ladderIndex + 1, usedTopPositions, usedBottomPositions, usedHeadPositions, usedTailPositions, gridSize, usedColumns);
                    // Add ladder to the UI
                    addLadderToUI(ladder, gameBoard);
                    // Update used positions with new ladder top and bottom positions
                    usedTopPositions.add(ladder.getTopPosition());
                    usedBottomPositions.add(ladder.getBottomPosition());
                    // Increment ladder index for the next iteration
                    ladderIndex++;
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    private void addLadderToUI(Ladder ladder, GameBoard gameBoard) {
        // Get positions
        int topPosition = ladder.getTopPosition();
        int bottomPosition = ladder.getBottomPosition();
        int gridSize = gameBoard.getSize();
        int topRow = gridSize - 1 - topPosition / gridSize;
        int topCol = topPosition % gridSize;
        int bottomRow = gridSize - 1 - bottomPosition / gridSize;
        int bottomCol = bottomPosition % gridSize;

        // Create ladder image view and stack pane
        ImageView ladderImageView = createLadderImageView(tileSize, gameBoard.getCellHeight());
        StackPane ladderStackPane = new StackPane(ladderImageView);
        //ladderStackPane.setAlignment(Pos.CENTER);
        ladderStackPane.setAlignment(Pos.TOP_LEFT);

        // Set size constraints for ladder stack pane
        ladderStackPane.setMaxSize(tileSize, tileSize * 2);
        ladderStackPane.setPrefSize(tileSize, tileSize * 2);
        ladderStackPane.setMinSize(tileSize, tileSize * 2);

        // Add ladder image to the grid pane
        Platform.runLater(() -> dynamicGridPane.add(ladderStackPane, topCol, topRow));

        // Calculate positions of ladder within the cell
        double ladderX = topCol * tileSize + (tileSize - ladderImageView.getImage().getWidth()) / 2;
        double ladderY = topRow * tileSize + tileSize - ladderImageView.getImage().getHeight(); // Adjusted for ladder height

        // Set layout position for ladder
        ladderStackPane.setLayoutX(ladderX);
        ladderStackPane.setLayoutY(ladderY);
    }


    private ImageView createLadderImageView(double tileSize, double cellHeight) {
        // Construct the image path for ladder
        String imagePath = "/View/Photos/ladder1.png";

        // Load the image with the calculated dimensions
        Image ladderImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)), tileSize, cellHeight*2 , true, true);

        // Create the ImageView with the scaled image
        ImageView ladderImageView = new ImageView(ladderImage);
        ladderImageView.setFitWidth(tileSize); // Set the width to match the initial tile size
        ladderImageView.setFitHeight(cellHeight *1.5); // Set the height to match the adjusted cell height
        return ladderImageView;
    }

    private Ladder generateUniqueLadder(int ladderIndex, Set<Integer> usedTopPositions, Set<Integer> usedBottomPositions, Set<Integer> usedHeadPositions, Set<Integer> usedTailPositions, int gridSize, Set<Integer> usedColumns) {
        while (true) {
            Ladder ladder = Ladder.generateRandomLadder(ladderIndex, gridSize);

            // Check if the ladder's end position coincides with the start position of another ladder
            if (usedTopPositions.contains(ladder.getBottomPosition())) {
                continue;
            }

            // Check if the ladder's end position coincides with the head position of a snake
            if (usedHeadPositions.contains(ladder.getBottomPosition())) {
                continue;
            }

            // Check if the ladder's start position coincides with the end position of a snake
            if (usedTailPositions.contains(ladder.getTopPosition())) {
                continue;
            }

            // Check if the ladder's start position coincides with the head position of a snake
            if (usedHeadPositions.contains(ladder.getTopPosition())) {
                continue;
            }

            // Check if the ladder's start position coincides with the end position of another ladder
            if (usedBottomPositions.contains(ladder.getTopPosition())) {
                continue;
            }

            // Check if the ladder's column is already used
            int ladderColumn = ladder.getTopPosition() % gridSize;
            if (usedColumns.contains(ladderColumn)) {
                continue;
            }

            // Check if the ladder's top position is not in the same cell as the bottom position of another ladder
            if (usedBottomPositions.contains(ladder.getTopPosition())) {
                continue;
            }

            // Check if the ladder's top and bottom positions are not in the last row of the grid
            int bottomRow = gridSize - 1 - ladder.getBottomPosition() / gridSize;
            if (bottomRow == gridSize - 1) {
                continue; // Bottom of the ladder cannot be in the last row
            }

            // If none of the conditions are met, return the ladder
            return ladder;
        }
    }

    private int determineNumberOfLadders(String selectedLevel) {
        if (selectedLevel.equals("Easy")) {
            return 4;
        } else if (selectedLevel.equals("Medium")) {
            return 6;
        } else if (selectedLevel.equals("Hard")) {
            return 8;
        } else {
            return 0; // Default case
        }
    }


    private double calculateTileSize(int gridSize) {
        double gridWidth = dynamicGridPane.getWidth();

        int cols = dynamicGridPane.getColumnConstraints().size(); // Get the number of columns
        double cellSpacing = (gridWidth - (gridSize * tileSize)) / (gridSize - 1); // Calculate the space between cells

        // Adjust the tile size based on the number of rows and columns and the spacing between them
        double adjustedTileSize = (gridWidth - (cellSpacing * (cols - 1))) / cols;
        tileSize = Math.min(adjustedTileSize, gridWidth / gridSize); // Ensure that tileSize fits within the grid width

        return tileSize;
    }


    private void initializeBoardUI(GameBoard gameBoard)  {
        Tile[][] tiles = gameBoard.getTiles();
        for (int i = 0; i < gameBoard.getSize(); i++) {
            for (int j = 0; j < gameBoard.getSize(); j++) {
                tiles[i][j].setFill(Color.WHITE);
                tiles[i][j].setStroke(Color.BLACK);
                tiles[i][j].setStrokeWidth(1);
                // Assign unique IDs to each cell based on row and column indices
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
}
