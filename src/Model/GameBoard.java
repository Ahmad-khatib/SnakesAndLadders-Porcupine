package Model;

import java.util.ArrayList;
import java.util.List;

public class GameBoard {
    private int gameId;
    private int difficultyLevel;
    private Tile[][] tiles;
    private List<Snake> snakes;
    private List<Ladder> ladders;
    private int size;
    private double preferredTileSize;

    public GameBoard(String level) {
        switch (level) {
            case "Easy":
                size = 7; // Easy level, number of rows/cols in the grid
                difficultyLevel = 1;
                break;
            case "Medium":
                size = 10; // Medium level, number of rows/cols in the grid
                difficultyLevel = 2;
                break;
            case "Hard":
                size = 13; // Hard level, number of rows/cols in the grid
                difficultyLevel = 3;
                break;
            default:
                throw new IllegalArgumentException("Invalid difficulty level");
        }
        initializeBoard();
        snakes = new ArrayList<>(); // Initialize snakes list
        ladders = new ArrayList<>(); // Initialize ladders list
    }

    private void initializeBoard() {
        // Calculate preferred dimensions for the tiles
        preferredTileSize = Math.min(823 / size, 800 / size);

        tiles = new Tile[size][size];
        int count = 1;

        for (int row = size - 1; row >= 0; row--) {
            for (int col = 0; col < size; col++) {
                tiles[row][col] = new Tile(count, Tile.TileType.NORMAL, col * preferredTileSize, row * preferredTileSize, preferredTileSize, preferredTileSize);
                count++;
            }
        }
    }

    public void displayBoard() {
        for (int row = size - 1; row >= 0; row--) {
            for (int col = 0; col < size; col++) {
                System.out.print(tiles[row][col].getTileType() + "\t");
            }
            System.out.println();
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

    public void setSnakes(List<Snake> snakes) {
        this.snakes = snakes;
    }

    public List<Ladder> getLadders() {
        return ladders;
    }

    public void setLadders(List<Ladder> ladders) {
        this.ladders = ladders;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
