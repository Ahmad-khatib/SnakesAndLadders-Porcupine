package Model;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GameBoard {
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
        preferredTileSize = Math.min(823 / size, 800 / size);
        List<Integer> supriseJumpTiles = new ArrayList<>();
        List<Integer> questionTiles = new ArrayList<>();
        while( supriseJumpTiles.size() < supriseJump){
           int random =new Random().nextInt(size*size) + 1;
            if(random != size*size){
                supriseJumpTiles.add(random);
            }
            while(questionTiles.size()<3){
                random =new Random().nextInt(size*size) + 1;
                if(!(supriseJumpTiles.contains(random)) && random !=1 ){
                    questionTiles.add(random);
                }
            }
        }
            questionTiles = generateRandomNumbers(size);
        tiles = new Tile[size][size];
        int count = 1;

        for (int row = size - 1; row >= 0; row--) {
            for (int col = 0; col < size; col++) {
                if (supriseJumpTiles.contains(count)) {
                    tiles[row][col] = new Tile(count, Tile.TileType.SURPRISE_JUMP, col * preferredTileSize, row * preferredTileSize, preferredTileSize, preferredTileSize);
                    System.out.print("this is the row "+row + " this is the col :" +col + " \n");
                }
                else if (questionTiles.contains(count)) {
                    tiles[row][col] = new Tile(count, Tile.TileType.QUESTION, col * preferredTileSize, row * preferredTileSize, preferredTileSize, preferredTileSize);
                    System.out.print("this is the row "+row + " this is the col :" +col + " \n");
                } else {
                    tiles[row][col] = new Tile(count, Tile.TileType.NORMAL, col * preferredTileSize, row * preferredTileSize, preferredTileSize, preferredTileSize);
                }
                count++;
            }
        }
        displayBoard();
    }
    private static List<Integer> generateRandomNumbers(int gridSize) {
        return new Random().ints(1, gridSize * gridSize + 1).distinct().limit(3).boxed().collect(Collectors.toList());
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
        this.size = size;
    }
}
