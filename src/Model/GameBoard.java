package Model;

import java.util.List;

public class GameBoard {
    private int gameId;
    private int difficultyLevel;
    private int[][] board;
    private List<Snake> snakes;
    private List<Ladder> ladders;
    private int size;

    public GameBoard(int difficultyLevel) {
        switch (difficultyLevel) {
            case 1:
                size = 7; // Easy level
                difficultyLevel = 1;
                break;
            case 2:
                size = 10; // Medium level
                difficultyLevel = 2;
                break;
            case 3:
                size = 13; // Hard level
                difficultyLevel = 3;
                break;
            default:
                throw new IllegalArgumentException("Invalid difficulty level");
        }

        initializeBoard();
    }

    private void initializeBoard() {
        board = new int[size][size];
        // Initialize the board with sequential numbers
        int count = 1;
        for (int row = size - 1; row >= 0; row--) {
            for (int col = 0; col < size; col++) {
                board[row][col] = count++;
            }
        }

    }

    public void displayBoard() {
        for (int row = size - 1; row >= 0; row--) {
            for (int col = 0; col < size; col++) {
                System.out.print(board[row][col] + "\t");
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

    public int[][] getCells() {
        return board;
    }


    public void setCells(int[][] cells) {
        this.board = cells;
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

}
