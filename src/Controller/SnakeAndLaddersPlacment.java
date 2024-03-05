package Controller;

import Model.GameBoard;
import Model.Snake;
import Model.Tile;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SnakeAndLaddersPlacment {
    static Set<Integer> usedHeadPositions = new HashSet<>();
    static Set<Integer> usedTailPositions = new HashSet<>();
    static Set<Integer> usedTopPositions = new HashSet<>();
    static Set<Integer> usedBottomPositions = new HashSet<>();
    private static double tileSize;
    private static int snakeIdCounter = 0;
    private static GridPane dynamicGridPane;
    private static GameBoard gameBoard;

    static void placeSnakes(String selectedLevel, GameBoard gameBoard1, GridPane gridPane) {
        dynamicGridPane = gridPane;
        gameBoard = gameBoard1;
        int gridSize = gameBoard.getSize();
        tileSize = gameBoard.getPreferredTileSize();


        int[] snakeCounts = getSnakeCounts(selectedLevel);

        // Iterate over the snake counts for each color individually

        for (int yellowCount = 0; yellowCount < snakeCounts[0]; yellowCount++) {
            Snake yellowSnake = generateUniqueSnake(Snake.SnakeColor.YELLOW,  gridSize, selectedLevel);
            if (yellowSnake != null) {
                updateSnakeUI(yellowSnake, selectedLevel, gameBoard.getSize(), gameBoard, gridPane);
                usedHeadPositions.add(yellowSnake.getHeadPosition());
                usedTailPositions.add(yellowSnake.getTailPosition());
            }
        }

        for (int greenCount = 0; greenCount < snakeCounts[1]; greenCount++) {
            Snake greenSnake = generateUniqueSnake(Snake.SnakeColor.GREEN, gridSize, selectedLevel);
            if (greenSnake != null) {
                updateSnakeUI(greenSnake, selectedLevel, gameBoard.getSize(), gameBoard, gridPane);
                usedHeadPositions.add(greenSnake.getHeadPosition());
                usedTailPositions.add(greenSnake.getTailPosition());
            }
        }

        for (int blueCount = 0; blueCount < snakeCounts[2]; blueCount++) {
            Snake blueSnake = generateUniqueSnake(Snake.SnakeColor.BLUE, gridSize, selectedLevel);
            if (blueSnake != null) {
                updateSnakeUI(blueSnake, selectedLevel, gameBoard.getSize(), gameBoard, gridPane);
                usedHeadPositions.add(blueSnake.getHeadPosition());
                usedTailPositions.add(blueSnake.getTailPosition());
            }
        }

        for (int redCount = 0; redCount < snakeCounts[3]; redCount++) {
            Snake redSnake = generateUniqueSnake(Snake.SnakeColor.RED, gridSize, selectedLevel);
            if (redSnake != null) {
                updateSnakeUI(redSnake, selectedLevel, gameBoard.getSize(), gameBoard, gridPane);
                usedHeadPositions.add(redSnake.getHeadPosition());
                usedTailPositions.add(redSnake.getTailPosition());
            }
        }

        System.out.print("Heads" + usedHeadPositions + "\n");
        System.out.print("Tails" + usedTailPositions + "\n");
    }

    private static Snake generateUniqueSnake(Snake.SnakeColor color, int gridSize, String selectedLevel) {
        Random random = new Random();
        while (true) {
            int headPosition = random.nextInt(gridSize * gridSize);
            int tailPosition = 0;
            int snakeId = generateUniqueSnakeId();
            int upperBound ;
            int lowerBound ;
            int headRow = headPosition % gridSize == 0 ? headPosition / gridSize : (headPosition / gridSize) + 1; // calulates the head position row while the botton row is 1


            // Check if the head position meets the color-specific criteria
            switch (color) {
                case YELLOW:
                    if (headPosition <= gridSize) {  // Yellow snake head cannot be in the first row, and its tail can be only one row apart from below
                        continue;
                    }
                    if (headRow == 2) {
                        lowerBound = gridSize - 1;
                    } else
                        lowerBound = ((headRow - 2) * gridSize) + 1;       //formula to calulate the valid range for the yellow snake tail (1 row below the head)
                    upperBound = ((headRow - 1) * gridSize);
                    tailPosition = random.nextInt(upperBound - lowerBound) + lowerBound;
                    break;
                case GREEN:
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
                    if (headPosition == gridSize || headPosition == 1 || usedHeadPositions.contains(headPosition) || usedTailPositions.contains(tailPosition) ||
                    usedHeadPositions.contains(tailPosition) || usedTailPositions.contains(headPosition)) {
                        System.out.print("Im the error\n");
                        break;
                    }
                    break;

                default:
                    break;
            }
            int snakeHeadRow = headPosition % gridSize == 0 ? (gridSize - (headPosition / gridSize)) : gridSize - ((headPosition / gridSize) + 1);   // formuals to clacualte the row and column in the 2D array (Grid)
            int snakeHeadCol = headPosition % gridSize == 0 ? gridSize - 1 : ((headPosition % gridSize) - 1);
            int snakeTailRow = tailPosition % gridSize == 0 ? (gridSize - (tailPosition / gridSize)) : gridSize - ((tailPosition / gridSize) + 1);
            int snakeTailCol = tailPosition % gridSize == 0 ? gridSize - 1 : ((tailPosition % gridSize) - 1);

            Tile[][] tiles = gameBoard.getTiles();
            if (snakeHeadCol - snakeTailCol < 7 || snakeTailCol - snakeHeadCol < 7) {
                if (!(tiles[snakeHeadRow][snakeHeadCol].isSpecialTile() || tiles[snakeTailRow][snakeTailCol].isSpecialTile())) {

                    Snake snake = new Snake(snakeId, color, headPosition, tailPosition);
                    if (color == Snake.SnakeColor.RED) {
                        if (!usedHeadPositions.contains(headPosition) &&
                                !usedTailPositions.contains(tailPosition) &&
                        !usedHeadPositions.contains(tailPosition) &&
                        !usedTailPositions.contains(headPosition))
                            gameBoard.getSnakes().add(snake);
                        return snake;

                    } else if (!usedHeadPositions.contains(headPosition) &&
                            !usedTailPositions.contains(tailPosition) &&
                            !usedHeadPositions.contains(tailPosition) &&
                            !usedTailPositions.contains(headPosition) &&
                            headPosition != tailPosition &&
                            isValidSnakePosition(snake, gridSize)) {
                        gameBoard.getSnakes().add(snake);
                        return snake;
                    }
                }
            }
        }
        // return null;
    }

    private static int generateUniqueSnakeId() {
        return snakeIdCounter++;
    }

    private static boolean isValidSnakePosition(Snake snake, int gridSize) {
        if (!(snake.getColor().equals(Snake.SnakeColor.RED))) {
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

    private static void updateSnakeUI(Snake snake, String selectedLevel, int gridSize, GameBoard gameBoard, GridPane dynamicGridPane) {
        // Get the positions of the snake head and tail, row 0 is the first row
        int headRow = snake.getHeadPosition() % gridSize == 0 ? (gridSize - (snake.getHeadPosition() / gridSize)) : gridSize - ((snake.getHeadPosition() / gridSize) + 1);
        int headCol = snake.getHeadPosition() % gridSize == 0 ? gridSize - 1 : ((snake.getHeadPosition() % gridSize) - 1);
        int tailRow = snake.getTailPosition() % gridSize == 0 ? (gridSize - (snake.getTailPosition() / gridSize)) : gridSize - ((snake.getTailPosition() / gridSize) + 1);
        int tailCol = snake.getTailPosition() % gridSize == 0 ? gridSize - 1 : ((snake.getTailPosition() % gridSize) - 1);

        // Calculate the height of the snake image based on the number of rows it occupies
        double cellHeight = gameBoard.getPreferredTileSize();
        double snakeHeight = cellHeight / 2;


        // Create custom tiles for the snake head and tail
        Tile headTile = new Tile();
        double distance = 1.0;
        if (!(snake.getColor().equals(Snake.SnakeColor.RED))) {
            distance = calculateDistance(headRow, headCol, tailRow, tailCol);
            snakeHeight = ((Math.abs(distance)) * cellHeight);
        }

        //Tile tailTile = new Tile();
        // Load the snake image based on the snake color
        String imagePath = "/View/Photos/" + snake.getColor().toString().toLowerCase() + "Snake.png";
        Image snakeImage = new Image(SnakeAndLaddersPlacment.class.getResourceAsStream(imagePath));
        ImageView snakeHeadImageView = new ImageView(snakeImage);
        ImageView adjustSnakesnakeImage = (adjustSnake(snakeHeadImageView, snake, cellHeight, headRow, headCol, tailRow, tailCol));
        snakeHeadImageView.setFitWidth(cellHeight);
        snakeHeadImageView.setFitHeight(snakeHeight);
        snakeHeadImageView.setPreserveRatio(false);
        snakeHeadImageView.setSmooth(false);
        snakeHeadImageView.smoothProperty();

        // Add snake head and tail images to custom tiles
        headTile.addSnakeHeadImage(adjustSnakesnakeImage);
        // Rotate the image based on the calculated angle

        dynamicGridPane.add(headTile, headCol, headRow);

        // Ensure the row spans properly to accommodate the snake height
        if (!(snake.getColor().equals(Snake.SnakeColor.RED)))
            GridPane.setRowSpan(headTile, (int) Math.ceil(snakeHeight / cellHeight));
    }

    public static double calculateDistance(int headRow, int headCol, int tailRow, int tailCol) {
        return Math.sqrt(Math.pow(tailRow - headRow, 2) + Math.pow(tailCol - headCol, 2));
    }

    private static int[] getSnakeCounts(String selectedLevel) {
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

    static ImageView adjustSnake(ImageView snakeImage, Snake snake, double cellSize, int headRow, int headCol, int tailRow, int tailCol) {
        switch (snake.getColor()) {
            case YELLOW:
                int colDiffirence = Math.abs(headCol - tailCol);
                if (headCol >= tailCol) {
                    if (colDiffirence == 0) {     // Done
                        snakeImage.setRotate(25);
                        snakeImage.setTranslateY(cellSize / 2);
                    }
                    if (colDiffirence == 1) { // Done
                        snakeImage.setRotate(82.1);
                        snakeImage.setTranslateX(-cellSize / 2);
                        snakeImage.setTranslateY(-cellSize / 8);
                    }
                    if (colDiffirence == 2) {  // Done
                        snakeImage.setRotate(85.2);
                        snakeImage.setTranslateX(-cellSize);
                        snakeImage.setTranslateY(-cellSize / 2);

                    }
                    if (colDiffirence == 3) {  // Done
                        snakeImage.setRotate(85.2);
                        snakeImage.setTranslateX(-cellSize * 1.5);
                        snakeImage.setTranslateY(-cellSize);


                    }
                    if (colDiffirence == 4) {   // Done
                        snakeImage.setRotate(86);
                        snakeImage.setTranslateX(-cellSize * 1.8);
                        snakeImage.setTranslateY(-cellSize * 1.4);
                    }


                    if (colDiffirence == 5) {   // Done
                        snakeImage.setRotate(85);
                        snakeImage.setTranslateX(-cellSize * 2.4);
                        snakeImage.setTranslateY(-cellSize * 1.7);

                    }
                    if (colDiffirence == 6) {   // Done
                        snakeImage.setRotate(88);
                        snakeImage.setTranslateX(-cellSize * 3);
                        snakeImage.setTranslateY(-cellSize * 2.2);

                    }
                /*    if (colDiffirence == 7) {   // Done
                        snakeImage.setRotate(88);
                        snakeImage.setTranslateX(-cellSize * 3.4);
                        snakeImage.setTranslateY(-cellSize * 2.8);
                    }
                    if (colDiffirence == 8) {  // Done
                        snakeImage.setRotate(89);
                        snakeImage.setTranslateX(-cellSize * 4);
                        snakeImage.setTranslateY(-cellSize * 3.4);
                    }
                    if (colDiffirence == 9) {  // Done
                        snakeImage.setRotate(90);
                        snakeImage.setTranslateX(-cellSize * 4.15);
                        snakeImage.setTranslateY(-cellSize * 3.7);
                    }
                    if (colDiffirence == 10) {
                        snakeImage.setRotate(85);
                        snakeImage.setTranslateX(-cellSize * 4.3);
                        snakeImage.setTranslateY(-cellSize * 3.7);
                    }
                    if (colDiffirence == 11) {
                        snakeImage.setRotate(85);
                        snakeImage.setTranslateX(-cellSize * 4.3);
                        snakeImage.setTranslateY(-cellSize * 3.7);
                    }
                    if (colDiffirence == 12) {
                        snakeImage.setRotate(85);
                        snakeImage.setTranslateX(-cellSize * 4.3);
                        snakeImage.setTranslateY(-cellSize * 3.7);
                    }*/
                } else {

                    if (colDiffirence == 1) { // Done
                        snakeImage.setRotate(-25);
                        snakeImage.setTranslateX(cellSize / 2);
                        snakeImage.setTranslateY(cellSize / 8);

                    }
                    if (colDiffirence == 2) {  // Done
                        snakeImage.setRotate(-40);
                        snakeImage.setTranslateX(cellSize);
                        snakeImage.setTranslateY(-cellSize / 3);

                    }
                    if (colDiffirence == 3) { // Done
                        snakeImage.setRotate(-55);
                        snakeImage.setTranslateX(cellSize * 1.5);
                        snakeImage.setTranslateY(-cellSize);
                    }
                    if (colDiffirence == 4) { //  Done
                        snakeImage.setRotate(-60);
                        snakeImage.setTranslateX(cellSize * 1.8);
                        snakeImage.setTranslateY(-cellSize * 1.4);
                    }
                    if (colDiffirence == 5) { // Done
                        snakeImage.setRotate(-70);
                        snakeImage.setTranslateX(cellSize * 2.4);
                        snakeImage.setTranslateY(-cellSize * 1.7);
                    }
                    if (colDiffirence == 6) {
                        snakeImage.setRotate(-71);
                        snakeImage.setTranslateX(cellSize * 3);
                        snakeImage.setTranslateY(-cellSize * 2.2);
                    }
              /*     if (colDiffirence == 7) {
                        snakeImage.setRotate(-72);
                        snakeImage.setTranslateY((cellSize / 2));
                        snakeImage.setTranslateX((cellSize * (colDiffirence - 1) / 4));
                    }
                    if (colDiffirence == 8) {
                        snakeImage.setRotate(-73);
                        snakeImage.setTranslateY(-(cellSize / 2));
                        snakeImage.setTranslateX((cellSize * (colDiffirence - 1) / 2));
                    }
                    if (colDiffirence == 9) {
                        snakeImage.setRotate(-74);
                        snakeImage.setTranslateY(-(cellSize / 2));
                        snakeImage.setTranslateX((cellSize * (colDiffirence - 1) / 2));
                    }
                    if (colDiffirence == 10) {
                        snakeImage.setRotate(-75);
                        snakeImage.setTranslateY(-(cellSize / 2));
                        snakeImage.setTranslateX((cellSize * (colDiffirence - 1) / 2));
                    }
                    if (colDiffirence == 11) {
                        snakeImage.setRotate(-76);
                        snakeImage.setTranslateY(-(cellSize / 2));
                        snakeImage.setTranslateX((cellSize * (colDiffirence - 1) / 2));
                    }
                    if (colDiffirence == 12) {
                        snakeImage.setRotate(-77);
                        snakeImage.setTranslateY(-(cellSize / 2));
                        snakeImage.setTranslateX((cellSize * (colDiffirence - 1) / 2));
                    }
                    */
                }
                break;
            case GREEN:
                colDiffirence = Math.abs(headCol - tailCol);
                if (headCol >= tailCol) {
                    if (colDiffirence == 0) {  // Done
                        snakeImage.setRotate(25);
                        snakeImage.setTranslateY(cellSize / 2);
                    }
                    if (colDiffirence == 1) {
                        snakeImage.setRotate(65);   // Done
                        snakeImage.setTranslateX(-cellSize / 2);
                        snakeImage.setTranslateY(-cellSize / 8);
                    }
                    if (colDiffirence == 2) {
                        snakeImage.setRotate(68);  // Done
                        snakeImage.setTranslateX(-cellSize);
                        snakeImage.setTranslateY(-cellSize / 30);

                    }
                    if (colDiffirence == 3) {
                        snakeImage.setRotate(69);   // Done
                        snakeImage.setTranslateX(-cellSize / 0.8);
                        snakeImage.setTranslateY(-cellSize / 1.4);


                    }
                    if (colDiffirence == 4) {  // Done
                        snakeImage.setRotate(68);
                        snakeImage.setTranslateX(-cellSize * 2);
                        snakeImage.setTranslateY(-cellSize * 1);
                    }


                    if (colDiffirence == 5) {
                        snakeImage.setRotate(77);  // Done
                        snakeImage.setTranslateX(-cellSize / 0.4);
                        snakeImage.setTranslateY(-cellSize / 0.8);

                    }
                    if (colDiffirence == 6) {
                        snakeImage.setRotate(73);  // Done
                        snakeImage.setTranslateX(-cellSize * 2.5);
                        snakeImage.setTranslateY(-cellSize * 1.5);

                    }
                  /*  if (colDiffirence == 7) {
                        snakeImage.setRotate(80);  //  done
                        snakeImage.setTranslateX(-cellSize * 3.5);
                        snakeImage.setTranslateY(-cellSize * 2.4);
                    }
                    if (colDiffirence == 8) {//done
                        snakeImage.setRotate(80);
                        snakeImage.setTranslateX(-cellSize * 3.9);
                        snakeImage.setTranslateY(-cellSize * 2.55);
                    }
                    if (colDiffirence == 9) { //done
                        snakeImage.setRotate(85);
                        snakeImage.setTranslateX(-cellSize * 4.4);
                        snakeImage.setTranslateY(-cellSize * 3.22);
                    }
                    if (colDiffirence == 10) {//done
                        snakeImage.setRotate(82);
                        snakeImage.setTranslateX(-cellSize * 4.79);
                        snakeImage.setTranslateY(-cellSize * 3.88);
                    }
                    if (colDiffirence == 11) {//done
                        snakeImage.setRotate(83);
                        snakeImage.setTranslateX(-cellSize * 5.1);
                        snakeImage.setTranslateY(-cellSize * 3.80);
                    }
                    if (colDiffirence == 12) {//done
                        snakeImage.setRotate(85);
                        snakeImage.setTranslateX(-cellSize * 6);
                        snakeImage.setTranslateY(-cellSize * 4.2);
                    }*/
                } else {

                    if (colDiffirence == 1) {
                        snakeImage.setRotate(-25); // done
                        snakeImage.setTranslateX(cellSize / 2);
                        snakeImage.setTranslateY(cellSize / 8);

                    }
                    if (colDiffirence == 2) {
                        snakeImage.setRotate(-30); // done
                        snakeImage.setTranslateX(cellSize);
                        snakeImage.setTranslateY(-cellSize / 3);

                    }
                    if (colDiffirence == 3) {
                        snakeImage.setRotate(-35); // done
                        snakeImage.setTranslateX(cellSize * 1.3);
                        snakeImage.setTranslateY(-cellSize * 0.8);
                    }
                    if (colDiffirence == 4) {
                        snakeImage.setRotate(-60); // done
                        snakeImage.setTranslateX(cellSize * 1.8);
                        snakeImage.setTranslateY(-cellSize * 0.8);
                    }
                    if (colDiffirence == 5) {
                        snakeImage.setRotate(-60);  // done
                        snakeImage.setTranslateX(cellSize * 2.2);
                        snakeImage.setTranslateY(-cellSize * 1.5);
                    }
                    if (colDiffirence == 6) {
                        snakeImage.setRotate(-50);  // done
                        snakeImage.setTranslateX(cellSize * 1.8);
                        snakeImage.setTranslateY(-cellSize * 1.2);
                    }
                  /*  if (colDiffirence == 7) {
                        snakeImage.setRotate(-80);  //  done
                        snakeImage.setTranslateX(cellSize * 3.5);
                        snakeImage.setTranslateY(cellSize * 2.4);
                    }
                    if (colDiffirence == 8) {
                        snakeImage.setRotate(-73);
                        snakeImage.setTranslateY(-(cellSize / 2));
                        snakeImage.setTranslateX((cellSize * (colDiffirence - 1) / 2));
                    }
                    if (colDiffirence == 9) {
                        snakeImage.setRotate(-74);
                        snakeImage.setTranslateY(-(cellSize / 2));
                        snakeImage.setTranslateX((cellSize * (colDiffirence - 1) / 2));
                    }
                    if (colDiffirence == 10) {
                        snakeImage.setRotate(-75);
                        snakeImage.setTranslateY(-(cellSize / 2));
                        snakeImage.setTranslateX((cellSize * (colDiffirence - 1) / 2));
                    }
                    if (colDiffirence == 11) {
                        snakeImage.setRotate(-76);
                        snakeImage.setTranslateY(-(cellSize / 2));
                        snakeImage.setTranslateX((cellSize * (colDiffirence - 1) / 2));
                    }
                    if (colDiffirence == 12) {
                        snakeImage.setRotate(-77);
                        snakeImage.setTranslateY(-(cellSize / 2));
                        snakeImage.setTranslateX((cellSize * (colDiffirence - 1) / 2));
                    }
              */  }
                break;
            case BLUE:
                colDiffirence = Math.abs(headCol - tailCol);
                if (headCol >= tailCol) {
                    if (colDiffirence == 0) {//done
                        snakeImage.setRotate(30);
                        snakeImage.setTranslateY(cellSize / 2);
                    }
                    if (colDiffirence == 1) {//done
                        snakeImage.setRotate(45);
                        snakeImage.setTranslateX(-cellSize / 2);
                        snakeImage.setTranslateY(-cellSize / 8);
                    }
                    if (colDiffirence == 2) {//done
                        snakeImage.setRotate(55);
                        snakeImage.setTranslateX(-cellSize);
                        snakeImage.setTranslateY(-cellSize / 30);

                    }
                    if (colDiffirence == 3) { //done
                        snakeImage.setRotate(60);
                        snakeImage.setTranslateX(-cellSize / 0.8);
                        snakeImage.setTranslateY(-cellSize / 1.7);


                    }
                    if (colDiffirence == 4) { //done
                        snakeImage.setRotate(68);
                        snakeImage.setTranslateX(-cellSize * 2);
                        snakeImage.setTranslateY(-cellSize * 0.5);
                    }


                    if (colDiffirence == 5) { //done
                        snakeImage.setRotate(70);
                        snakeImage.setTranslateX(-cellSize / 0.4);
                        snakeImage.setTranslateY(-cellSize);

                    }
                    if (colDiffirence == 6) { //done
                        snakeImage.setRotate(73);
                        snakeImage.setTranslateX(-cellSize * 3);
                        snakeImage.setTranslateY(-cellSize * 1.5);

                    }
                  /*  if (colDiffirence == 7) {
                        snakeImage.setRotate(60);
                        snakeImage.setTranslateX(-cellSize * 0.4);
                        snakeImage.setTranslateY(-cellSize * 0.8);
                    }
                    if (colDiffirence == 8) {
                        snakeImage.setRotate(70);
                        snakeImage.setTranslateX(-cellSize * 1);
                        snakeImage.setTranslateY(-cellSize * 0.8);
                    }
                    if (colDiffirence == 9) {
                        snakeImage.setRotate(90);
                        snakeImage.setTranslateX(-cellSize * 4.15);
                        snakeImage.setTranslateY(-cellSize * 3.7);
                    }
                    if (colDiffirence == 10) {
                        snakeImage.setRotate(85);
                        snakeImage.setTranslateX(-cellSize * 4.3);
                        snakeImage.setTranslateY(-cellSize * 3.7);
                    }
                    if (colDiffirence == 11) {
                        snakeImage.setRotate(85);
                        snakeImage.setTranslateX(-cellSize * 4.3);
                        snakeImage.setTranslateY(-cellSize * 3.7);
                    }
                    if (colDiffirence == 12) {
                        snakeImage.setRotate(85);
                        snakeImage.setTranslateX(-cellSize * 4.3);
                        snakeImage.setTranslateY(-cellSize * 3.7);
                    }*/
                } else {

                    if (colDiffirence == 1) {
                        snakeImage.setRotate(-20); // done
                        snakeImage.setTranslateX(cellSize / 2);
                        snakeImage.setTranslateY(cellSize / 8);

                    }
                    if (colDiffirence == 2) {
                        snakeImage.setRotate(-30); // done
                        snakeImage.setTranslateX(cellSize);
                        snakeImage.setTranslateY(-cellSize / 7);

                    }
                    if (colDiffirence == 3) {
                        snakeImage.setRotate(-35); // done
                        snakeImage.setTranslateX(cellSize * 1.3);
                        snakeImage.setTranslateY(-cellSize * 0.3);
                    }
                    if (colDiffirence == 4) {
                        snakeImage.setRotate(-40); // done
                        snakeImage.setTranslateX(cellSize * 1.8);
                        snakeImage.setTranslateY(-cellSize * 0.8);
                    }
                    if (colDiffirence == 5) {
                        snakeImage.setRotate(-50);  // done
                        snakeImage.setTranslateX(cellSize * 2.2);
                        snakeImage.setTranslateY(-cellSize * 1);
                    }
                    if (colDiffirence == 6) {
                        snakeImage.setRotate(-50);  // done
                        snakeImage.setTranslateX(cellSize * 1.8);
                        snakeImage.setTranslateY(-cellSize * 1.2);
                    }
                 /*   if (colDiffirence == 7) {
                        snakeImage.setRotate(-50);  // not done
                        snakeImage.setTranslateY((cellSize / 2));
                        snakeImage.setTranslateX((cellSize * (colDiffirence - 1) / 4));
                    }
                    if (colDiffirence == 8) {
                        snakeImage.setRotate(-73);
                        snakeImage.setTranslateY(-(cellSize / 2));
                        snakeImage.setTranslateX((cellSize * (colDiffirence - 1) / 2));
                    }
                    if (colDiffirence == 9) {
                        snakeImage.setRotate(-74);
                        snakeImage.setTranslateY(-(cellSize / 2));
                        snakeImage.setTranslateX((cellSize * (colDiffirence - 1) / 2));
                    }
                    if (colDiffirence == 10) {
                        snakeImage.setRotate(-75);
                        snakeImage.setTranslateY(-(cellSize / 2));
                        snakeImage.setTranslateX((cellSize * (colDiffirence - 1) / 2));
                    }
                    if (colDiffirence == 11) {
                        snakeImage.setRotate(-76);
                        snakeImage.setTranslateY(-(cellSize / 2));
                        snakeImage.setTranslateX((cellSize * (colDiffirence - 1) / 2));
                    }
                    if (colDiffirence == 12) {
                        snakeImage.setRotate(-77);
                        snakeImage.setTranslateY(-(cellSize / 2));
                        snakeImage.setTranslateX((cellSize * (colDiffirence - 1) / 2));
                    }
                */}
                break;
            case RED:
                // Add adjustments for red snake if needed
                break;
            default:
                break;
        }

        return snakeImage;
    }

    static void placeLadders(String selectedLevel, GameBoard gameBoard1, GridPane gridPane) {
        dynamicGridPane = gridPane;
        gameBoard = gameBoard1;
        int gridSize = gameBoard.getSize();
        tileSize = gameBoard.getPreferredTileSize();

        int[] ladderCounts = getLadderCounts(selectedLevel);

        // Initialize sets to track used ladder positions


        // Iterate over ladder counts for each level
        for (int i = 0; i < ladderCounts.length; i++) {
            // Generate ladders for each ladder count
            for (int j = 0; j <ladderCounts[i]; j++) {

                int ladderTop = generateUniqueLadderTopPosition(gridSize, i);
                int ladderBottom = generateUniqueLadderBottomPosition(ladderTop, gridSize, i);
                updateLadderUI(ladderTop, ladderBottom);
                usedTopPositions.add(ladderTop);
                usedBottomPositions.add(ladderBottom);

            }
        }
        System.out.print(" top = " + usedTopPositions);
        System.out.print("Bottom = "  + usedBottomPositions);
    }

    private static int[] getLadderCounts(String selectedLevel) {
        switch (selectedLevel) {
            case "Easy":
                return new int[]{1, 1, 1, 1, 0, 0, 0, 0}; // Easy level has 4 ladders
            case "Medium":
                return new int[]{1, 1, 1, 1, 1, 1, 0, 0}; // Medium level has 6 ladders
            case "Hard":
                return new int[]{1, 1, 1, 1, 1, 1, 1, 1}; // Hard level has 8 ladders
            default:
                throw new IllegalArgumentException("Invalid game level: " + selectedLevel);
        }
    }

    private static int generateUniqueLadderTopPosition(int gridSize, int ladderIndex) {
        Random random = new Random();
        int ladderTop;
        Tile[][] tiles = gameBoard.getTiles();

        int ladderTopRow;
        int ladderTopCol;
        do {
            ladderTop = random.nextInt(gridSize * gridSize);
            ladderTopRow = ladderTop % gridSize == 0 ? (gridSize - (ladderTop / gridSize)) : gridSize - ((ladderTop / gridSize) + 1);
            ladderTopCol = ladderTop % gridSize == 0 ? gridSize - 1 : ((ladderTop % gridSize) - 1);
        } while (usedTopPositions.contains(ladderTop) || usedBottomPositions.contains(ladderTop) || isInvalidLadderTopPosition(ladderTop, ladderIndex, gridSize) || usedHeadPositions.contains(ladderTop)
                || usedTailPositions.contains(ladderTop) || tiles[ladderTopRow][ladderTopCol].isSpecialTile());
        return ladderTop;
    }

    private static boolean isInvalidLadderTopPosition(int ladderTop, int ladderIndex, int gridSize) {
        // Implement specific rules for ladder top position based on ladder index and game level
        switch (ladderIndex) {
            case 0:
                if (ladderTop <= gridSize) {
                    return true;
                }
                break;
            case 1:
                if (ladderTop <= gridSize * 2) {
                    return true;
                }
                break;
            case 2:
                if (ladderTop <= gridSize * 3) {
                    return true;
                }
                break;
            case 3:
                if (ladderTop <= gridSize * 4) {
                    return true;
                }
                break;
            case 4:
                if (ladderTop <= gridSize * 5) {
                    return true;
                }
                break;
            case 5:
                if (ladderTop <= gridSize * 6) {
                    return true;
                }

                break;
            case 6:
                if (ladderTop <= gridSize * 7) {
                    return true;
                }
                break;
            case 7:
                if (ladderTop <= gridSize * 8) {
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }

    private static int generateUniqueLadderBottomPosition(int ladderTop, int gridSize, int ladderIndex) {
        Random random = new Random();
        Tile[][] tiles = gameBoard.getTiles();
        int ladderBottom;
        int lower_bound;
        int upper_bound;
        int topCol;
        int i = 0;
        int bottomCol;
        int ladderBottomRow;
        do {
            i++;
            int ladderHeight = calculateLadderHeight(ladderIndex);
            int ladderTopRow = ladderTop % gridSize == 0 ? ladderTop / gridSize : (ladderTop / gridSize) + 1;
            if (ladderTopRow - ladderHeight == 0) {
                lower_bound = 1;
            } else
                lower_bound = ((ladderTopRow - ladderHeight) - 1) * gridSize + 1;
            upper_bound = (ladderTopRow - ladderHeight) * gridSize;
            ladderBottom = lower_bound + random.nextInt(upper_bound - lower_bound + 1);
            bottomCol = ladderBottom % gridSize == 0 ? gridSize - 1 : ((ladderBottom % gridSize) - 1);
            ladderBottomRow = ladderBottom % gridSize == 0 ? (gridSize - (ladderBottom / gridSize)) : gridSize - ((ladderBottom / gridSize) + 1);

        } while (ladderBottom <= 1 || usedBottomPositions.contains(ladderBottom) || usedTopPositions.contains(ladderBottom) || isInvalidLadderBottomPosition(ladderBottom, ladderIndex, gridSize)
                || usedHeadPositions.contains(ladderBottom) || usedTailPositions.contains(ladderBottom)/*||
                (!(topCol - bottomCol == 4))  /*|| (!(bottomCol- topCol == 4))  */|| tiles[ladderBottomRow][bottomCol].isSpecialTile());
        return ladderBottom;
    }

    private static int calculateLadderHeight(int ladderIndex) {
        // Calculate ladder height based on ladder index
        // Adjust the height as needed for each ladder type
        switch (ladderIndex) {
            case 0:
                return 1; // Example: First ladder type has a height of 2 rows
            case 1:
                return 2; // Example: Second ladder type has a height of 3 rows
            case 2:
                return 3; // Example: Third ladder type has a height of 4 rows
            case 3:
                return 4;
            case 4:
                return 5;
            case 5:
                return 6;
            case 6:
                return 7;
            default:
                return 1;
        }
    }

    private static boolean isInvalidLadderBottomPosition(int ladderBottom, int ladderIndex, int gridSize) {
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

    private static void updateLadderUI(int ladderTop, int ladderBottom) {
        int gridSize = gameBoard.getSize();
        double cellHeight = gameBoard.getPreferredTileSize();

        // Calculate grid row and column indices for ladder top and bottom
        int topRow = ladderTop % gridSize == 0 ? (gridSize - (ladderTop / gridSize)) : gridSize - ((ladderTop / gridSize) + 1);
        int topCol = ladderTop % gridSize == 0 ? gridSize - 1 : ((ladderTop % gridSize) - 1);
        int bottomRow = ladderBottom % gridSize == 0 ? (gridSize - (ladderBottom / gridSize)) : gridSize - ((ladderBottom / gridSize) + 1);
        int bottomCol = ladderBottom % gridSize == 0 ? gridSize - 1 : ((ladderBottom % gridSize) - 1);
        //int topRow = gridSize - 1 - ladderTop / gridSize;
        //int topCol = ladderTop % gridSize;
        //int bottomRow = gridSize - 1 - ladderBottom / gridSize;
        //int bottomCol = ladderBottom % gridSize;

        // Calculate ladder height based on the difference between top and bottom positions
        double distance = calculateDistance(topRow, topCol, bottomRow, bottomCol);
        double ladderHeight = Math.abs(distance * cellHeight);

        // Load ladder image
        String imagePath = "/View/Photos/ladder1.png";
        Image ladderImage = new Image(SnakeAndLaddersPlacment.class.getResourceAsStream(imagePath));


        // Create ImageView for ladder
        ImageView ladderImageView = new ImageView(ladderImage);

        // Set width and height of ladder image
        ladderImageView.setFitWidth(cellHeight);
        ladderImageView.setFitHeight(ladderHeight);
        ImageView adjustedLadder = (adjustLadder(ladderImageView, cellHeight, topCol, bottomCol, topRow, bottomRow));

        // Add ladder image to custom tile
        Tile ladderTile = new Tile();
        ladderTile.addLadderImage(adjustedLadder);

        // Add custom tile to grid pane at ladder top position
        dynamicGridPane.add(ladderTile, topCol, topRow);

        // Ensure ladder spans multiple rows
        GridPane.setRowSpan(ladderTile, (int) Math.ceil(ladderHeight / cellHeight));
    }

    static ImageView adjustLadder(ImageView ladderImage, double cellSize, int topCol, int bottomCol, int topRow, int bottomRow) {
        int colDifferenceTopBottom = topCol - bottomCol;
        int colDifferenceBottomTop = bottomCol - topCol;
        int rowDiffrence = Math.abs(topRow - bottomRow);
        switch (rowDiffrence) {
            case 1:                                                  // 1 step ladder
                if (topCol >= bottomCol) {
                    switch (colDifferenceTopBottom) {
                        case 0:               // Done
                            ladderImage.setRotate(0);
                            ladderImage.setTranslateY(cellSize / 2);
                            break;
                        case 1:                        // Done
                            ladderImage.setRotate(45);
                            ladderImage.setTranslateX(-cellSize / 2);
                            break;

                        case 2:
                            ladderImage.setRotate(60);       // Done
                            ladderImage.setTranslateX(-cellSize);
                            ladderImage.setTranslateY(-cellSize / 2);
                            break;

                        case 3:
                            ladderImage.setRotate(70); // Done
                            ladderImage.setTranslateX(-cellSize*1.4);
                            ladderImage.setTranslateY(-cellSize);
                            break;

                        case 4:
                            ladderImage.setRotate(78);       // Done
                            ladderImage.setTranslateX(-cellSize * 2);
                            ladderImage.setTranslateY(-cellSize * 1.18);
                            break;

                        case 5:
                            System.out.println("TopRow - BottomRow difference is 5");
                            // Your logic for topRow - bottomRow difference of 5
                            break;

                        case 6:
                            System.out.println("TopRow - BottomRow difference is 6");
                            // Your logic for topRow - bottomRow difference of 6
                            break;

                        default:
                            System.out.println("TopRow - BottomRow difference is not within the specified cases");
                            // Your default logic
                            break;
                    }
                    return ladderImage;
                } else
                    switch (colDifferenceBottomTop) {
                        case 1:
                            ladderImage.setRotate(-45);      // Done
                            ladderImage.setTranslateX(cellSize / 2);
                            break;

                        case 2:
                            ladderImage.setRotate(-60);          // Done
                            ladderImage.setTranslateX(cellSize);
                            ladderImage.setTranslateY(-cellSize / 2);
                            break;

                        case 3:
                            ladderImage.setRotate(-70); // Done
                            ladderImage.setTranslateX(cellSize*1.4);
                            ladderImage.setTranslateY(-cellSize);
                            break;

                        case 4:
                            ladderImage.setRotate(-78);       // Done
                            ladderImage.setTranslateX(cellSize * 2);
                            ladderImage.setTranslateY(-cellSize * 1.18);
                            break;

                        case 5:
                            System.out.println("BottomRow - TopRow difference is 5");
                            // Your logic for bottomRow - topRow difference of 5
                            break;

                        case 6:
                            System.out.println("BottomRow - TopRow difference is 6");
                            // Your logic for bottomRow - topRow difference of 6
                            break;


                        default:
                            System.out.println("BottomRow - TopRow difference is not within the specified cases");
                            // Your default logic
                            break;
                    }
                return ladderImage;
            case 2:                                             //  2 step ladder
                if (topCol >= bottomCol) {
                    switch (colDifferenceTopBottom) {
                        case 0:
                            ladderImage.setTranslateY(cellSize / 2);
                        case 1:
                            ladderImage.setRotate(45);
                            ladderImage.setTranslateX(-cellSize / 2);
                            break;

                        case 2:
                            ladderImage.setRotate(30);
                            ladderImage.setTranslateX(-cellSize / 2);
                            break;

                        case 3:
                            System.out.println("TopRow - BottomRow difference is 3");
                            // Your logic for topRow - bottomRow difference of 3
                            break;

                        case 4:
                            ladderImage.setRotate(78);       // Done
                            ladderImage.setTranslateX(-cellSize * 2);
                            ladderImage.setTranslateY(-cellSize * 1.18);
                            break;

                        case 5:
                            System.out.println("TopRow - BottomRow difference is 5");
                            // Your logic for topRow - bottomRow difference of 5
                            break;

                        case 6:
                            System.out.println("TopRow - BottomRow difference is 6");
                            // Your logic for topRow - bottomRow difference of 6
                            break;

                        default:
                            System.out.println("TopRow - BottomRow difference is not within the specified cases");
                            // Your default logic
                            break;
                    }
                    return ladderImage;
                } else
                    switch (colDifferenceBottomTop) {
                        case 1:
                            System.out.println("BottomRow - TopRow difference is 1");
                            // Your logic for bottomRow - topRow difference of 1
                            break;

                        case 2:
                            System.out.println("BottomRow - TopRow difference is 2");
                            // Your logic for bottomRow - topRow difference of 2
                            break;

                        case 3:
                            System.out.println("BottomRow - TopRow difference is 3");
                            // Your logic for bottomRow - topRow difference of 3
                            break;

                        case 4:
                            System.out.println("BottomRow - TopRow difference is 4");
                            // Your logic for bottomRow - topRow difference of 4
                            break;

                        case 5:
                            System.out.println("BottomRow - TopRow difference is 5");
                            // Your logic for bottomRow - topRow difference of 5
                            break;

                        case 6:
                            System.out.println("BottomRow - TopRow difference is 6");
                            // Your logic for bottomRow - topRow difference of 6
                            break;


                        default:
                            System.out.println("BottomRow - TopRow difference is not within the specified cases");
                            // Your default logic
                            break;
                    }
                return ladderImage;
            case 3:                                       // 3 step Ladder
                if (topCol >= bottomCol) {
                    switch (colDifferenceTopBottom) {
                        case 0:
                            ladderImage.setTranslateY(cellSize / 2);
                        case 1:
                            ladderImage.setRotate(45);
                            ladderImage.setTranslateX(-cellSize / 2);
                            break;

                        case 2:
                            ladderImage.setRotate(30);
                            ladderImage.setTranslateX(-cellSize / 2);
                            break;

                        case 3:
                            System.out.println("TopRow - BottomRow difference is 3");
                            // Your logic for topRow - bottomRow difference of 3
                            break;

                        case 4:
                            ladderImage.setRotate(75);       // Done
                            ladderImage.setTranslateX(-cellSize * 2);
                            ladderImage.setTranslateY(-cellSize * 1.18);
                            break;

                        case 5:
                            System.out.println("TopRow - BottomRow difference is 5");
                            // Your logic for topRow - bottomRow difference of 5
                            break;

                        case 6:
                            ladderImage.setRotate(85);
                            ladderImage.setTranslateX(-cellSize * 3);
                            ladderImage.setTranslateY(-cellSize * 2.8);
                            break;

                        default:
                            System.out.println("TopRow - BottomRow difference is not within the specified cases");
                            // Your default logic
                            break;
                    }
                    return ladderImage;
                } else
                    switch (colDifferenceBottomTop) {
                        case 1:
                            System.out.println("BottomRow - TopRow difference is 1");
                            // Your logic for bottomRow - topRow difference of 1
                            break;

                        case 2:
                            System.out.println("BottomRow - TopRow difference is 2");
                            // Your logic for bottomRow - topRow difference of 2
                            break;

                        case 3:
                            System.out.println("BottomRow - TopRow difference is 3");
                            // Your logic for bottomRow - topRow difference of 3
                            break;

                        case 4:
                            System.out.println("BottomRow - TopRow difference is 4");
                            // Your logic for bottomRow - topRow difference of 4
                            break;

                        case 5:
                            System.out.println("BottomRow - TopRow difference is 5");
                            // Your logic for bottomRow - topRow difference of 5
                            break;

                        case 6:
                            ladderImage.setRotate(-85);
                            ladderImage.setTranslateX(cellSize * 3);
                            ladderImage.setTranslateY(-cellSize * 2.8);
                            break;

                        default:
                            System.out.println("BottomRow - TopRow difference is not within the specified cases");
                            // Your default logic
                            break;
                    }
                return ladderImage;
            case 4:                       // 4 step Ladder
                if (topCol >= bottomCol) {
                    switch (colDifferenceTopBottom) {
                        case 0:
                            ladderImage.setTranslateY(cellSize / 2);
                        case 1:
                            ladderImage.setRotate(45);
                            ladderImage.setTranslateX(-cellSize / 2);
                            break;

                        case 2:
                            ladderImage.setRotate(30);
                            ladderImage.setTranslateX(-cellSize / 2);
                            break;

                        case 3:
                            System.out.println("TopRow - BottomRow difference is 3");
                            // Your logic for topRow - bottomRow difference of 3
                            break;

                        case 4:
                            ladderImage.setRotate(75);       // Done
                            ladderImage.setTranslateX(-cellSize * 2);
                            ladderImage.setTranslateY(-cellSize * 1.18);
                            break;

                        case 5:
                            System.out.println("TopRow - BottomRow difference is 5");
                            // Your logic for topRow - bottomRow difference of 5
                            break;

                        case 6:
                            ladderImage.setRotate(85);
                            ladderImage.setTranslateX(-cellSize * 3);
                            ladderImage.setTranslateY(-cellSize * 2.8);
                            break;

                        default:
                            System.out.println("TopRow - BottomRow difference is not within the specified cases");
                            // Your default logic
                            break;
                    }
                    return ladderImage;
                } else
                    switch (colDifferenceBottomTop) {
                        case 1:
                            System.out.println("BottomRow - TopRow difference is 1");
                            // Your logic for bottomRow - topRow difference of 1
                            break;

                        case 2:
                            System.out.println("BottomRow - TopRow difference is 2");
                            // Your logic for bottomRow - topRow difference of 2
                            break;

                        case 3:
                            System.out.println("BottomRow - TopRow difference is 3");
                            // Your logic for bottomRow - topRow difference of 3
                            break;

                        case 4:
                            System.out.println("BottomRow - TopRow difference is 4");
                            // Your logic for bottomRow - topRow difference of 4
                            break;

                        case 5:
                            System.out.println("BottomRow - TopRow difference is 5");
                            // Your logic for bottomRow - topRow difference of 5
                            break;

                        case 6:
                            ladderImage.setRotate(-85);
                            ladderImage.setTranslateX(cellSize * 3);
                            ladderImage.setTranslateY(-cellSize * 2.8);
                            break;

                        default:
                            System.out.println("BottomRow - TopRow difference is not within the specified cases");
                            // Your default logic
                            break;
                    }
                return ladderImage;
            case 5:                  // 5 step Ladder
                if (topCol >= bottomCol) {
                    switch (colDifferenceTopBottom) {
                        case 0:
                            ladderImage.setTranslateY(cellSize / 2);
                        case 1:
                            ladderImage.setRotate(45);
                            ladderImage.setTranslateX(-cellSize / 2);
                            break;

                        case 2:
                            ladderImage.setRotate(30);
                            ladderImage.setTranslateX(-cellSize / 2);
                            break;

                        case 3:
                            System.out.println("TopRow - BottomRow difference is 3");
                            // Your logic for topRow - bottomRow difference of 3
                            break;

                        case 4:
                            ladderImage.setRotate(75);       // Done
                            ladderImage.setTranslateX(-cellSize * 2);
                            ladderImage.setTranslateY(-cellSize * 1.18);
                            break;

                        case 5:
                            System.out.println("TopRow - BottomRow difference is 5");
                            // Your logic for topRow - bottomRow difference of 5
                            break;

                        case 6:
                            ladderImage.setRotate(85);
                            ladderImage.setTranslateX(-cellSize * 3);
                            ladderImage.setTranslateY(-cellSize * 2.8);
                            break;

                        default:
                            System.out.println("TopRow - BottomRow difference is not within the specified cases");
                            // Your default logic
                            break;
                    }
                    return ladderImage;
                } else
                    switch (colDifferenceBottomTop) {
                        case 1:
                            System.out.println("BottomRow - TopRow difference is 1");
                            // Your logic for bottomRow - topRow difference of 1
                            break;

                        case 2:
                            System.out.println("BottomRow - TopRow difference is 2");
                            // Your logic for bottomRow - topRow difference of 2
                            break;

                        case 3:
                            System.out.println("BottomRow - TopRow difference is 3");
                            // Your logic for bottomRow - topRow difference of 3
                            break;

                        case 4:
                            System.out.println("BottomRow - TopRow difference is 4");
                            // Your logic for bottomRow - topRow difference of 4
                            break;

                        case 5:
                            System.out.println("BottomRow - TopRow difference is 5");
                            // Your logic for bottomRow - topRow difference of 5
                            break;

                        case 6:
                            ladderImage.setRotate(-85);
                            ladderImage.setTranslateX(cellSize * 3);
                            ladderImage.setTranslateY(-cellSize * 2.8);
                            break;

                        default:
                            System.out.println("BottomRow - TopRow difference is not within the specified cases");
                            // Your default logic
                            break;
                    }
                return ladderImage;
            case 6:                              // 6 step Ladder
                if (topCol >= bottomCol) {
                    switch (colDifferenceTopBottom) {
                        case 0:
                            ladderImage.setTranslateY(cellSize / 2);
                        case 1:
                            ladderImage.setRotate(45);
                            ladderImage.setTranslateX(-cellSize / 2);
                            break;

                        case 2:
                            ladderImage.setRotate(30);
                            ladderImage.setTranslateX(-cellSize / 2);
                            break;

                        case 3:
                            System.out.println("TopRow - BottomRow difference is 3");
                            // Your logic for topRow - bottomRow difference of 3
                            break;

                        case 4:
                            ladderImage.setRotate(75);       // Done
                            ladderImage.setTranslateX(-cellSize * 2);
                            ladderImage.setTranslateY(-cellSize * 1.18);
                            break;

                        case 5:
                            System.out.println("TopRow - BottomRow difference is 5");
                            // Your logic for topRow - bottomRow difference of 5
                            break;

                        case 6:
                            ladderImage.setRotate(85);
                            ladderImage.setTranslateX(-cellSize * 3);
                            ladderImage.setTranslateY(-cellSize * 2.8);
                            break;

                        default:
                            System.out.println("TopRow - BottomRow difference is not within the specified cases");
                            // Your default logic
                            break;
                    }
                    return ladderImage;
                } else
                    switch (colDifferenceBottomTop) {
                        case 1:
                            System.out.println("BottomRow - TopRow difference is 1");
                            // Your logic for bottomRow - topRow difference of 1
                            break;

                        case 2:
                            System.out.println("BottomRow - TopRow difference is 2");
                            // Your logic for bottomRow - topRow difference of 2
                            break;

                        case 3:
                            System.out.println("BottomRow - TopRow difference is 3");
                            // Your logic for bottomRow - topRow difference of 3
                            break;

                        case 4:
                            System.out.println("BottomRow - TopRow difference is 4");
                            // Your logic for bottomRow - topRow difference of 4
                            break;

                        case 5:
                            System.out.println("BottomRow - TopRow difference is 5");
                            // Your logic for bottomRow - topRow difference of 5
                            break;

                        case 6:
                            ladderImage.setRotate(-85);
                            ladderImage.setTranslateX(cellSize * 3);
                            ladderImage.setTranslateY(-cellSize * 2.8);
                            break;

                        default:
                            System.out.println("BottomRow - TopRow difference is not within the specified cases");
                            // Your default logic
                            break;
                    }
                return ladderImage;
            case 7:                                           // 7 step Ladder
                if (topCol >= bottomCol) {
                    switch (colDifferenceTopBottom) {
                        case 0:
                            ladderImage.setTranslateY(cellSize / 2);
                        case 1:
                            ladderImage.setRotate(45);
                            ladderImage.setTranslateX(-cellSize / 2);
                            break;

                        case 2:
                            ladderImage.setRotate(30);
                            ladderImage.setTranslateX(-cellSize / 2);
                            break;

                        case 3:
                            System.out.println("TopRow - BottomRow difference is 3");
                            // Your logic for topRow - bottomRow difference of 3
                            break;

                        case 4:
                            ladderImage.setRotate(75);       // Done
                            ladderImage.setTranslateX(-cellSize * 2);
                            ladderImage.setTranslateY(-cellSize * 1.18);
                            break;

                        case 5:
                            System.out.println("TopRow - BottomRow difference is 5");
                            // Your logic for topRow - bottomRow difference of 5
                            break;

                        case 6:
                            ladderImage.setRotate(85);
                            ladderImage.setTranslateX(-cellSize * 3);
                            ladderImage.setTranslateY(-cellSize * 2.8);
                            break;

                        default:
                            System.out.println("TopRow - BottomRow difference is not within the specified cases");
                            // Your default logic
                            break;
                    }
                    return ladderImage;

                } else
                    switch (colDifferenceBottomTop) {
                        case 1:
                            System.out.println("BottomRow - TopRow difference is 1");
                            // Your logic for bottomRow - topRow difference of 1
                            break;

                        case 2:
                            System.out.println("BottomRow - TopRow difference is 2");
                            // Your logic for bottomRow - topRow difference of 2
                            break;

                        case 3:
                            System.out.println("BottomRow - TopRow difference is 3");
                            // Your logic for bottomRow - topRow difference of 3
                            break;

                        case 4:
                            System.out.println("BottomRow - TopRow difference is 4");
                            // Your logic for bottomRow - topRow difference of 4
                            break;

                        case 5:
                            System.out.println("BottomRow - TopRow difference is 5");
                            // Your logic for bottomRow - topRow difference of 5
                            break;

                        case 6:
                            ladderImage.setRotate(-85);
                            ladderImage.setTranslateX(cellSize * 3);
                            ladderImage.setTranslateY(-cellSize * 2.8);
                            break;

                        default:
                            System.out.println("BottomRow - TopRow difference is not within the specified cases");
                            // Your default logic
                            break;
                    }
                return ladderImage;
        }
        return ladderImage;
    }


}
