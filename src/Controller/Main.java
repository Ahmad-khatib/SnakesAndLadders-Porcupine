package Controller;

import Model.GameBoard;

public class Main {
    public static void main(String[] args) {
        // Create a game board for the easy difficulty level
        GameBoard easyGameBoard = new GameBoard(1);

        // Display the easy game board
        System.out.println("Easy Game Board:");
        easyGameBoard.displayBoard();

        // Create a game board for the medium difficulty level
        GameBoard mediumGameBoard = new GameBoard(2);

        // Display the medium game board
        System.out.println("\nMedium Game Board:");
        mediumGameBoard.displayBoard();

        // Create a game board for the hard difficulty level
        GameBoard hardGameBoard = new GameBoard(3);

        // Display the hard game board
        System.out.println("\nHard Game Board:");
        hardGameBoard.displayBoard();
    }
}
