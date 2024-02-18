package Model;

import java.util.List;

public class GameBoard {
    private int gameId;
    private int difficultyLevel;
    private Tile[][] tiles;
    private List<Snake> snakes;
    private List<Ladder> ladders;
    private int size;

    public GameBoard(String Level) {
        double preferredTileSize=00.0;
        switch (Level) {
            case "Easy":
                size = 7; // Easy level
                difficultyLevel = 1;
                preferredTileSize=114.285714;
                break;
            case "Medium":
                size = 10; // Medium level
                difficultyLevel = 2;
                preferredTileSize=80;
                break;
            case "Hard":
                size = 13; // Hard level
                difficultyLevel = 3;
                preferredTileSize=60.5;
                break;
            default:
                throw new IllegalArgumentException("Invalid difficulty level");
        }
        tiles= initializeBoard(preferredTileSize);
    }

    private Tile[][] initializeBoard(double preferredTileSize) {
        tiles = new Tile[size][size];
        int count = 1;

        for (int row = size - 1; row >= 0; row--) {
            for (int col = 0; col < size; col++) {
                tiles[row][col] = new Tile(count, Tile.TileType.NORMAL, col, row, preferredTileSize, preferredTileSize);
                count++;
            }
        }
        displayBoard();
        return tiles;

    }
    public void placeSnakeOnTile(int col, int row) {
        if (col >= 0 && col < size && row >= 0 && row < size) {
            tiles[row][col].setTileType(Tile.TileType.NORMAL);
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

    // Getters and Setters for all fields
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
