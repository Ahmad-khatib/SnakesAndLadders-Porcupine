package Model;

import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class GameBoard extends GridPane {
    private int gameId;
    private int difficultyLevel;
    private Tile[][] tiles;
    private ArrayList<Snake> snakes;
    private ArrayList<Ladder> ladders;
    private int size;
    private double preferredTileSize;

    public GameBoard(String level) {
        int supriseJump = 0;
        switch (level) {
            case "Easy":
                size = 7; // Easy level, number of rows/cols in the grid
                difficultyLevel = 1;
                break;
            case "Medium":
                size = 10; // Medium level, number of rows/cols in the grid
                difficultyLevel = 2;
                supriseJump = 1;
                break;
            case "Hard":
                size = 13; // Hard level, number of rows/cols in the grid
                difficultyLevel = 3;
                supriseJump = 2 ;
                break;
            default:
                throw new IllegalArgumentException("Invalid difficulty level");
        }
        initializeBoard(supriseJump);
        snakes = new ArrayList<>(); // Initialize snakes list
        ladders = new ArrayList<>(); // Initialize ladders list
    }

    private void initializeBoard(int supriseJump) {
        // Calculate preferred dimensions for the tiles
        preferredTileSize = Math.min(600 / size, 700 / size);
        int[] supriseJumpIndices = TileFactory.generateRandomNumbers(size, supriseJump);
        int[] questionIndices = TileFactory.generateRandomNumbers(size, 3);

        tiles = new Tile[size][size];
        int count = 1;

        for (int row = size - 1; row >= 0; row--) {
            for (int col = 0; col < size; col++) {
                tiles[row][col] = TileFactory.createTile(count, col * preferredTileSize,
                        row * preferredTileSize, preferredTileSize, preferredTileSize, supriseJumpIndices, questionIndices);

                count++;
            }
        }
        displayBoard();
    }

    public void displayBoard() {
        for (int row = size - 1; row >= 0; row--) {
            for (int col = 0; col < size; col++) {
            }

        }
    }

    // Getters for preferredTileSize and other fields
    public double getPreferredTileSize() {
        return preferredTileSize;
    }

    public double getCellWidth() {
        return preferredTileSize;
    }

    public double getCellHeight() {
        return preferredTileSize;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public List<Snake> getSnakes() {
        return snakes;
    }

    public List<Ladder> getLadders() {
        return ladders;
    }

    public void setSnakes(ArrayList<Snake> snakes) {
        this.snakes = snakes;
    }

    public void setLadders(ArrayList<Ladder> ladders) {
        this.ladders = ladders;
    }

    public void setPreferredTileSize(double preferredTileSize) {
        this.preferredTileSize = preferredTileSize;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size =size;
}

}
