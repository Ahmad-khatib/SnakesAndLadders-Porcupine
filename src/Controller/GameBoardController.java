package Controller;

import Model.GameBoard;
import Model.Snake;
import Model.Tile;
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
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import java.io.File;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.shape.Rectangle;


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
    private GameBoard gameBoard;
    private int gridSize;
    private static int snakeIdCounter = 0;

    public void initialize(String selectedLevel) {
        gameBoard = new GameBoard(selectedLevel);
        initializeBoardUI();
        placeSnakes(selectedLevel);
        initializeTimer();
      //  placeLadders(selectedLevel);
    }

    private void initializeBoardUI() {
        Tile[][] tiles = gameBoard.getTiles();
        this.gridSize = gameBoard.getSize();

        for (int i = 0; i < gameBoard.getSize(); i++) {
            for (int j = 0; j < gameBoard.getSize(); j++) {
                // Access the Rectangle object within each Tile
                Rectangle tileRectangle = tiles[i][j].getTileRectangle();
                // Set properties of the Rectangle object
                tileRectangle.setFill(Color.WHITE);
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

    private void placeSnakes(String selectedLevel) {
        int gridSize = gameBoard.getSize();
        tileSize = gameBoard.getPreferredTileSize();

        Set<Integer> usedHeadPositions = new HashSet<>();
        Set<Integer> usedTailPositions = new HashSet<>();

        int[] snakeCounts = getSnakeCounts(selectedLevel);

        // Iterate over the snake counts for each color individually
        for (int yellowCount = 0; yellowCount < 5; yellowCount++) {
            Snake yellowSnake = generateUniqueSnake(Snake.SnakeColor.YELLOW, usedHeadPositions, usedTailPositions, gridSize, selectedLevel);
            if (yellowSnake != null) {
                updateSnakeUI(yellowSnake, selectedLevel);
                usedHeadPositions.add(yellowSnake.getHeadPosition());
                usedTailPositions.add(yellowSnake.getTailPosition());
            }
        }
/*
        for (int greenCount = 0; greenCount < snakeCounts[1]; greenCount++) {
            Snake greenSnake = generateUniqueSnake(Snake.SnakeColor.GREEN, usedHeadPositions, usedTailPositions, gridSize, selectedLevel);
            if (greenSnake != null) {
                updateSnakeUI(greenSnake, selectedLevel);
                usedHeadPositions.add(greenSnake.getHeadPosition());
                usedTailPositions.add(greenSnake.getTailPosition());
            }
        }

        for (int blueCount = 0; blueCount < snakeCounts[2]; blueCount++) {
            Snake blueSnake = generateUniqueSnake(Snake.SnakeColor.BLUE, usedHeadPositions, usedTailPositions, gridSize, selectedLevel);
            if (blueSnake != null) {
                updateSnakeUI(blueSnake, selectedLevel);
                usedHeadPositions.add(blueSnake.getHeadPosition());
                usedTailPositions.add(blueSnake.getTailPosition());
            }
        }

        for (int redCount = 0; redCount < snakeCounts[3]; redCount++) {
            Snake redSnake = generateUniqueSnake(Snake.SnakeColor.RED, usedHeadPositions, usedTailPositions, gridSize, selectedLevel);
            if (redSnake != null) {
                updateSnakeUI(redSnake, selectedLevel);
                usedHeadPositions.add(redSnake.getHeadPosition());
                usedTailPositions.add(redSnake.getTailPosition());
            }
        }
*/
        System.out.print("Heads" +usedHeadPositions+"\n");
        System.out.print("Tails" +usedTailPositions);
    }


    private int determineNumberOfSnakes(String selectedLevel) {
        switch (selectedLevel) {
            case "Easy":
                return 4;
            case "Medium":
                return 6;
            case "Hard":
                return 8;
            default:
                throw new IllegalArgumentException("Invalid game level: " + selectedLevel);
        }
    }

    private Snake generateUniqueSnake(Snake.SnakeColor color, Set<Integer> usedHeadPositions, Set<Integer> usedTailPositions, int gridSize, String selectedLevel) {
        Random random = new Random();
        int k = 0;
        while (true) {
            k++;
            int headPosition = random.nextInt(gridSize * gridSize);
            int tailPosition = 0;
            int snakeId = generateUniqueSnakeId();
            int upperBound = 0;
            int lowerBound = 0;
            int headRow = headPosition % gridSize == 0 ? headPosition / gridSize  : (headPosition / gridSize) +1; // calulates the head position row while the botton row is 1


            // Check if the head position meets the color-specific criteria
            switch (color) {
                case YELLOW:
                    if (headPosition <= gridSize) {  // Yellow snake head cannot be in the first row, and its tail can be only one row apart from below
                        System.out.print("Yellow\n");
                        continue;
                    }
                    if (headRow == 2) {
                        lowerBound = gridSize - 1;
                    } else
                        lowerBound = ((headRow - 2) * gridSize) + 1;       //formula to calulate the valid range for the yellow snake tail (1 row below the head)
                    upperBound = ((headRow - 1) * gridSize);
                    tailPosition = random.nextInt(upperBound - lowerBound) + lowerBound;
                    break;
                 /* case GREEN:
                    if (headPosition <= gridSize * 2) {
                        System.out.print("Green\n");
                        continue;
                    }
                    if (headRow == 3) {
                        lowerBound = gridSize - 1;
                    } else
                        lowerBound = ((headRow - 3) * gridSize) + 1; //formula to calulate the valid range for the green snake tail (2 row below the head)
                    upperBound = ((headRow - 2) * gridSize);
                    tailPosition = random.nextInt(upperBound - lowerBound) + lowerBound;
                    break;
                case BLUE:
                    if (headPosition <= gridSize * 3) {
                        System.out.print("Blue\n");// Blue snake head cannot be in the first three rows
                        continue;
                    }
                    if (headRow == 4) {
                        lowerBound = gridSize - 1;
                    } else
                        lowerBound = ((headRow - 4) * gridSize) + 1; //formula to calulate the valid range for the blue snake tail (3 row below the head)
                    upperBound = ((headRow - 3) * gridSize);
                    tailPosition = random.nextInt(upperBound - lowerBound) + lowerBound;
                    break;
                case RED:
                    tailPosition = headPosition;

                    if (headPosition == gridSize || headPosition == 0) {
                        System.out.print("Im the error\n");
                        break;
                    }
                    break;
*/
                default:
                    break;
            }


            Snake snake = new Snake(snakeId, color, headPosition, tailPosition);
            if (color == Snake.SnakeColor.RED) {
                if (!usedHeadPositions.contains(headPosition) &&
                        !usedTailPositions.contains(tailPosition))
                    return snake;

            } else if (!usedHeadPositions.contains(headPosition) &&
                    !usedTailPositions.contains(tailPosition) &&
                    !usedHeadPositions.contains(tailPosition) &&
                    !usedTailPositions.contains(headPosition) &&
                    headPosition != tailPosition &&
                    isValidSnakePosition(snake, usedHeadPositions, usedTailPositions, gridSize, selectedLevel)) {
                return snake;
            }
        }
       // return null;
    }


        private int generateUniqueSnakeId () {
            return snakeIdCounter++;
        }

        private boolean isValidSnakePosition (Snake
        snake, Set < Integer > usedHeadPositions, Set < Integer > usedTailPositions,int gridSize, String selectedLevel){
            if (!(snake.getColor().equals("RED"))) {
                if (usedHeadPositions.contains(snake.getHeadPosition()) ||
                        usedTailPositions.contains(snake.getHeadPosition()) ||
                        usedHeadPositions.contains(snake.getTailPosition()) ||
                        usedTailPositions.contains(snake.getTailPosition())) {
                    return false;
                }
            }

            if (snake.getHeadPosition() >= gridSize * gridSize ||
                    snake.getTailPosition() >= gridSize * gridSize) {
                return false;
            }

            return true;
        }

    private void updateSnakeUI(Snake snake, String selectedLevel) {
        // Get the positions of the snake head and tail, row 0 is the first row
        int headRow = snake.getHeadPosition() % gridSize ==0 ? (gridSize - (snake.getHeadPosition()/gridSize)):gridSize - ((snake.getHeadPosition()/gridSize)+1);
        int headCol = snake.getHeadPosition() % gridSize == 0 ? gridSize-1 : ((snake.getHeadPosition() % gridSize)-1);
        int tailRow = snake.getTailPosition() % gridSize ==0 ? (gridSize - (snake.getTailPosition()/gridSize)):gridSize - ((snake.getTailPosition()/gridSize)+1);
        int tailCol = snake.getTailPosition() % gridSize == 0 ? gridSize-1 : ((snake.getTailPosition() % gridSize)-1);

        // Calculate the height of the snake image based on the number of rows it occupies
        double cellHeight = gameBoard.getPreferredTileSize();
        double snakeHeight = 1.0;

        // Determine the height of the snake image based on its color
       /* switch (snake.getColor()) {
            case YELLOW:
                snakeHeight = 2 * cellHeight;
                break;
            case GREEN:
                snakeHeight = 3 * cellHeight;
                break;
            case BLUE:
                snakeHeight = 4 * cellHeight;
                break;
            case RED:
                snakeHeight = cellHeight;
                break;
            default:
                break;
        }*/

        // Create custom tiles for the snake head and tail
        Tile headTile = new Tile();
        double distance = 1.0;
        if (!(snake.getColor().equals("RED"))) {
            distance = calculateDistance(headRow, headCol, tailRow, tailCol) ;
            System.out.print("This is the Head row " +headRow +"\n");
            System.out.print("This is the Head col " +headCol +"\n");
            System.out.print("This is the Tail row " +tailRow +"\n");
            System.out.print("This is the Tail col " +tailCol +"\n");
            snakeHeight = ((Math.abs(distance)) * cellHeight);
        }

        //Tile tailTile = new Tile();
        // Load the snake image based on the snake color
        String imagePath = "/View/Photos/" + snake.getColor().toString().toLowerCase() + "Snake.png";
        Image snakeImage = new Image(getClass().getResourceAsStream(imagePath));
        ImageView snakeHeadImageView = new ImageView(snakeImage);
        ImageView adjustSnakesnakeImage  =(adjustSnake(snakeHeadImageView,snake, cellHeight, headRow, headCol,  tailRow,  tailCol)) ;
        // Create ImageView objects for the snake head and tail
        //   ImageView snakeHeadImageView = new ImageView(snakeImage);
        // ImageView snakeTailImageView = new ImageView(snakeImage);
        // Set the scaled width and height for the snake images
        snakeHeadImageView.setFitWidth(cellHeight);
        snakeHeadImageView.setFitHeight(snakeHeight);
        System.out.print(snakeHeadImageView.getFitWidth()+"\n");
        System.out.print(snakeHeadImageView.getFitHeight()+" LALA");
        snakeHeadImageView.setPreserveRatio(false);
        snakeHeadImageView.setSmooth(false);
        snakeHeadImageView.smoothProperty();

//        snakeTailImageView.setFitWidth(cellHeight);
//        snakeTailImageView.setFitHeight(snakeHeight);

        // Add snake head and tail images to custom tiles
         headTile.addSnakeHeadImage(adjustSnakesnakeImage);


        //tailTile.addSnakeTailImage(snakeTailImageView);

// Add the rotation to the ImageView


        // Add custom tiles to the grid pane at the specified row and column indices
        // Rotate the image based on the calculated angle
      //  snakeHeadImageView.setTranslateX(0);
       // snakeHeadImageView.setTranslateY(0);
        int colDiffernces = Math.abs(headCol-tailCol);
        int rowDiffernces = Math.abs(headRow-headRow);

        // Rotate the image based on the calculated angle
        System.out.print("This is the distance" +distance +"\n");

        /*if (headCol > tailCol) {
            snakeHeadImageView.setRotate(15 * colDiffernces);
        } else if(headCol < tailCol) {
            snakeHeadImageView.setRotate(-15 * colDiffernces);
        }*/
        dynamicGridPane.add(headTile,headCol,headRow);
       // dynamicGridPane.add(headTile, colDiffernces/2, (headRow+tailRow)/2);
        // dynamicGridPane.add(headTile, headCol, headRow);
        //dynamicGridPane.add(tailTile, tailCol, tailRow);

        // Ensure the row spans properly to accommodate the snake height
         GridPane.setRowSpan(headTile, (int) Math.ceil(snakeHeight / cellHeight));
        //GridPane.setRowSpan(tailTile, (int) Math.ceil(snakeHeight / cellHeight));
    }
    public static double calculateDistance(int headRow, int headCol, int tailRow, int tailCol) {
        return Math.sqrt(Math.pow(tailRow - headRow, 2) + Math.pow(tailCol - headCol, 2));
    }


    private int[] getSnakeCounts(String selectedLevel) {
        switch (selectedLevel) {
            case "Easy":
                return new int[]{1, 1, 1, 1};
            case "Medium":
                return new int[]{2, 1, 1, 2}; // One snake for each color, except for yellow which has two
            case "Hard":
                return new int[]{2, 2, 2, 2};
            default:
                throw new IllegalArgumentException("Invalid game level: " + selectedLevel);
        }
    }


    private void placeLadders(String selectedLevel) {
        int gridSize = gameBoard.getSize();
        tileSize = gameBoard.getPreferredTileSize();

        int[] ladderCounts = getLadderCounts(selectedLevel);

        // Initialize sets to track used ladder positions
        Set<Integer> usedTopPositions = new HashSet<>();
        Set<Integer> usedBottomPositions = new HashSet<>();

        // Iterate over ladder counts for each level
        for (int i = 0; i < ladderCounts.length; i++) {
            // Generate ladders for each ladder count
            for (int j = 0; j < ladderCounts[i]; j++) {
                int ladderTop = generateUniqueLadderTopPosition(usedTopPositions, gridSize, i);
                int ladderBottom = generateUniqueLadderBottomPosition(usedBottomPositions, ladderTop, gridSize, i);
                updateLadderUI(ladderTop, ladderBottom);
                usedTopPositions.add(ladderTop);
                usedBottomPositions.add(ladderBottom);
            }
        }
    }

    private int[] getLadderCounts(String selectedLevel) {
        switch (selectedLevel) {
            case "Easy":
                return new int[]{1,1,1,1,0,0,0,0}; // Easy level has 4 ladders
            case "Medium":
                return new int[]{1,1,1,1,1,1,0,0}; // Medium level has 6 ladders
            case "Hard":
                return new int[]{1,1,1,1,1,1,1,1}; // Hard level has 8 ladders
            default:
                throw new IllegalArgumentException("Invalid game level: " + selectedLevel);
        }
    }

    private int generateUniqueLadderTopPosition(Set<Integer> usedTopPositions, int gridSize, int ladderIndex) {
        Random random = new Random();
        int ladderTop;
        do {
            ladderTop = random.nextInt(gridSize * gridSize);
        } while (usedTopPositions.contains(ladderTop) || isInvalidLadderTopPosition(ladderTop, ladderIndex, gridSize));
        return ladderTop;
    }

    private boolean isInvalidLadderTopPosition(int ladderTop, int ladderIndex, int gridSize) {
        // Implement specific rules for ladder top position based on ladder index and game level
        switch (ladderIndex) {
            case 0:
                // Additional rules for the first ladder type (if any)
                break;
            case 1:
                // Additional rules for the second ladder type (if any)
                break;
            case 2:
                // Additional rules for the third ladder type (if any)
                break;
            default:
                break;
        }
        return false;
    }

    private int generateUniqueLadderBottomPosition(Set<Integer> usedBottomPositions, int ladderTop, int gridSize, int ladderIndex) {
        Random random = new Random();
        int ladderBottom;
        do {
            int ladderHeight = calculateLadderHeight(ladderIndex);
            ladderBottom = ladderTop - ladderHeight;
        } while (ladderBottom < 0 || usedBottomPositions.contains(ladderBottom) || isInvalidLadderBottomPosition(ladderBottom, ladderIndex, gridSize));
        return ladderBottom;
    }

    private int calculateLadderHeight(int ladderIndex) {
        // Calculate ladder height based on ladder index
        // Adjust the height as needed for each ladder type
        switch (ladderIndex) {
            case 0:
                return 2; // Example: First ladder type has a height of 2 rows
            case 1:
                return 3; // Example: Second ladder type has a height of 3 rows
            case 2:
                return 4; // Example: Third ladder type has a height of 4 rows
            case 3:
                return 5;
            case 4:
                return 6;
            case 5:
                return 7;
            case 6:
                return 8;
            default:
                return 0;
        }
    }

    private boolean isInvalidLadderBottomPosition(int ladderBottom, int ladderIndex, int gridSize) {
        // Implement specific rules for ladder bottom position based on ladder index and game level
        switch (ladderIndex) {
            case 0:
                // Additional rules for the first ladder type (if any)
                break;
            case 1:
                // Additional rules for the second ladder type (if any)
                break;
            case 2:
                // Additional rules for the third ladder type (if any)
                break;
            case 3:
                // Additional rules for the third ladder type (if any)
                break;
            case 4:
                // Additional rules for the third ladder type (if any)
                break;
            case 5:
                // Additional rules for the third ladder type (if any)
                break;
            case 6:
                // Additional rules for the third ladder type (if any)
                break;
            case 7:
                // Additional rules for the third ladder type (if any)
                break;
            default:
                break;
        }
        return false;
    }


    ImageView adjustSnake(ImageView snakeImage, Snake snake, double cellSize, int headRow, int headCol, int tailRow, int tailCol) {
        switch (snake.getColor()) {
            case YELLOW:
                    int colDiffirence = Math.abs(headCol-tailCol);
                int rowDiffirence = Math.abs(headRow-tailRow);
                    if(headCol >= tailCol) {
                        if (colDiffirence == 0) { ///done
                            snakeImage.setRotate(25);
                            snakeImage.setTranslateY(50);

                        }
                        if (colDiffirence == 1) { // done
                            snakeImage.setRotate(82.1);
                            snakeImage.setTranslateX(-((cellSize * (colDiffirence))/2));
                            snakeImage.setTranslateY(-5);

                        }
                        if (colDiffirence == 2 ) {
                            snakeImage.setRotate(85.2);
                            snakeImage.setTranslateY(-(cellSize / 2));
                            snakeImage.setTranslateX(-(cellSize * (colDiffirence - 1)));
                        }
                        if ( colDiffirence == 3) {
                            snakeImage.setRotate(85.2);
                            snakeImage.setTranslateY(-(cellSize / 5));
                            snakeImage.setTranslateX(-((cellSize * (colDiffirence - 1))));
                        }
                        if (colDiffirence == 4) {
                            snakeImage.setRotate(85.2);
                            snakeImage.setTranslateY(cellSize);
                            snakeImage.setTranslateX(-((cellSize/2) * (colDiffirence - 1)));
                        }


                        if (colDiffirence == 5) {
                            snakeImage.setRotate(85);
                            snakeImage.setTranslateY(-(2*cellSize)); //sample
                            snakeImage.setTranslateX(-((cellSize * (colDiffirence - 1))/2));
                        }
                        if (colDiffirence == 6) {
                            snakeImage.setRotate(90);
                            snakeImage.setTranslateY(-(cellSize / 2));
                            snakeImage.setTranslateX(-(cellSize * (colDiffirence - 1))/2);
                        }
                        if (colDiffirence == 7) {
                            snakeImage.setRotate(40);
                        }
                        if (colDiffirence == 8) {
                            snakeImage.setRotate(70);
                        }
                        if (colDiffirence == 9) {
                            snakeImage.setRotate(75);
                        }
                        if (colDiffirence == 10) {
                            snakeImage.setRotate(-80);
                        }
                        if (colDiffirence == 11) {
                            snakeImage.setRotate(85);
                        }
                        if (colDiffirence == 12) {
                            snakeImage.setRotate(90);
                        }
                    }
                    else if (headCol < tailCol){

                        if (colDiffirence == 1) {
                            snakeImage.setRotate(-25);
                            snakeImage.setTranslateX((cellSize * (colDiffirence)/2));

                        }
                        if (colDiffirence == 2 ) {
                            snakeImage.setRotate(-40);
                            snakeImage.setTranslateY(1.5*cellSize);
                            snakeImage.setTranslateX((cellSize * (colDiffirence - 1))/2);
                        }
                        if ( colDiffirence == 3 ) { //finla
                            snakeImage.setRotate(-55);
                            snakeImage.setTranslateY(25);
                            snakeImage.setTranslateX((cellSize * (colDiffirence - 1)));
                        }
                        if (colDiffirence == 4) { //sample
                            snakeImage.setRotate(-60);
                            snakeImage.setTranslateY(-(cellSize));
                            snakeImage.setTranslateX((cellSize * (colDiffirence - 1)));
                        }
                        if (colDiffirence == 5) {
                            snakeImage.setRotate(-70);
                            snakeImage.setTranslateY((cellSize / 4));

                            snakeImage.setTranslateX((cellSize * (colDiffirence - 1)));
                        }
                        if (colDiffirence == 6) {
                            snakeImage.setRotate(-71);
                            snakeImage.setTranslateY(-(cellSize / 2));
                            snakeImage.setTranslateX((cellSize * (colDiffirence - 1)/2));
                        }
                        if (colDiffirence == 7) {
                            snakeImage.setRotate(-72);
                            snakeImage.setTranslateY(-(cellSize / 2));
                            snakeImage.setTranslateX((cellSize * (colDiffirence - 1)/4));
                        }
                        if (colDiffirence == 8) {
                            snakeImage.setRotate(-73);
                            snakeImage.setTranslateY(-(cellSize / 2));
                            snakeImage.setTranslateX((cellSize * (colDiffirence - 1)/2));
                        }
                        if (colDiffirence == 9) {
                            snakeImage.setRotate(-74);
                            snakeImage.setTranslateY(-(cellSize / 2));
                            snakeImage.setTranslateX((cellSize * (colDiffirence - 1)/2));
                        }
                        if (colDiffirence == 10) {
                            snakeImage.setRotate(-75);
                            snakeImage.setTranslateY(-(cellSize / 2));
                            snakeImage.setTranslateX((cellSize * (colDiffirence - 1)/2));
                        }
                        if (colDiffirence == 11) {
                            snakeImage.setRotate(-76);
                            snakeImage.setTranslateY(-(cellSize / 2));
                            snakeImage.setTranslateX((cellSize * (colDiffirence - 1)/2));
                        }
                        if (colDiffirence == 12) {
                            snakeImage.setRotate(-77);
                            snakeImage.setTranslateY(-(cellSize / 2));
                            snakeImage.setTranslateX((cellSize * (colDiffirence - 1)/2));
                        }
                    }
                break;
            case GREEN:
                // Add adjustments for green snake if needed
                break;
            case BLUE:
                // Add adjustments for blue snake if needed
                break;
            case RED:
                // Add adjustments for red snake if needed
                break;
            default:
                break;
        }

        return snakeImage;
    }


    private void updateLadderUI(int ladderTop, int ladderBottom) {
        int gridSize = gameBoard.getSize();
        double cellHeight = gameBoard.getPreferredTileSize();

        // Calculate grid row and column indices for ladder top and bottom
        int topRow = gridSize - 1 - ladderTop / gridSize;
        int topCol = ladderTop % gridSize;
        int bottomRow = gridSize - 1 - ladderBottom / gridSize;
        int bottomCol = ladderBottom % gridSize;

        // Calculate ladder height based on the difference between top and bottom positions
        double ladderHeight = Math.abs(bottomRow - topRow + 1) * cellHeight;

        // Load ladder image
        String imagePath = "/View/Photos/ladder1.png"; // Assuming ladder.png is in the specified path
        Image ladderImage = new Image(getClass().getResourceAsStream(imagePath));

        // Create ImageView for ladder
        ImageView ladderImageView = new ImageView(ladderImage);

        // Set width and height of ladder image
        ladderImageView.setFitWidth(cellHeight);
        ladderImageView.setFitHeight(ladderHeight);

        // Add ladder image to custom tile
        Tile ladderTile = new Tile();
        ladderTile.addLadderImage(ladderImageView);

        // Add custom tile to grid pane at ladder top position
        dynamicGridPane.add(ladderTile, topCol, topRow);

        // Ensure ladder spans multiple rows
        GridPane.setRowSpan(ladderTile, (int) Math.ceil(ladderHeight / cellHeight));
    }




}
