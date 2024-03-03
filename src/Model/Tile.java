package Model;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.Random;

public class Tile extends StackPane {
    private int tileId;
    private TileType tileType;
    private double row;
    private double col;
    private Rectangle tile;

    public enum TileType {
        NORMAL,
        QUESTION,
        SURPRISE_JUMP
    }

    public Tile(int tileId, TileType tileType, double col, double row, double width, double height) {
        super();
        this.tileId = tileId;
        this.tileType = tileType;
        this.row = row;
        this.col = col;

        // Create the rectangle representing the tile
        this.tile = new Rectangle(width, height);
        // Set the fill and stroke properties for the rectangle
        this.tile.setFill(Color.WHITE);
        this.tile.setStroke(Color.BLACK);
        // Add the rectangle to the StackPane
        getChildren().add(this.tile);
    }



    public Tile() {
        // Default constructor with default values
        this(0, TileType.NORMAL, 0, 0, 0, 0); // You can adjust default values as needed
    }

    // Getters and setters for all fields

    public int getTileId() {
        return tileId;
    }

    public void setTileId(int tileId) {
        this.tileId = tileId;
    }

    public TileType getTileType() {
        return tileType;
    }

    public void setTileType(TileType tileType) {
        this.tileType = tileType;
    }

    public double getRow() {
        return row;
    }

    public void setRow(double row) {
        this.row = row;
    }

    public double getCol() {
        return col;
    }

    public void setCol(double col) {
        this.col = col;
    }

    // Method to handle special actions for each tile type
    public void handleSpecialAction(Player player) {
        switch (tileType) {
            case NORMAL:
                // Normal tile, no special action
                break;
            case QUESTION:
                String randomQuestion;
                Difficulty randomDifficulty = getRandomDifficulty();  //Get a random difficulty level
                randomQuestion = Question.getQuestion(randomDifficulty);
                break;
            case SURPRISE_JUMP:
                // Special action for surprise tile - move player forward 10 steps
                player.movePlayerForward(10);
                break;
            default:
                throw new IllegalArgumentException("Invalid tile type");
        }
    }

    // Method to check if the tile is a special tile (question or surprise jump)
    public boolean isSpecialTile() {
        return tileType == TileType.QUESTION || tileType == TileType.SURPRISE_JUMP;
    }

    // Method to add snake head image to the tile
    public void addSnakeHeadImage(ImageView imageView) {
        // Add the snake head image to the StackPane
        getChildren().add(imageView);
    }

    // Method to add snake tail image to the tile
    public void addSnakeTailImage(ImageView imageView) {
        // Add the snake tail image to the StackPane
        getChildren().add(imageView);
    }

    public void addLadderImage(ImageView imageView) {
        // Add the snake tail image to the StackPane
        getChildren().add(imageView);
    }

    public Rectangle getTileRectangle() {
        return tile;
    }

    private Difficulty getRandomDifficulty() {
        Random random = new Random();
        int randomDifficultyIndex = random.nextInt(Difficulty.values().length);
        return Difficulty.values()[randomDifficultyIndex];
    }}