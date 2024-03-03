package Controller;

import Model.*;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
public class GameController {
    private static final double ANIMATION_DURATION = 500;
    @FXML
    private ImageView diceImage;
    @FXML
    private ImageView diceImage2;


    public static void Start(Game game, GridPane dynamicGridPane) throws IOException {
        ArrayList<Player> players = game.getPlayers();
        GameBoard gameBoard = game.getGameBoard();
        HashMap<Difficulty, ArrayList<Question>> questions = game.getQuestions();
        int i = 0;
        while (i < players.size()) {
            players.get(i).getIcon().setFitWidth(gameBoard.getCellWidth() / 2);
            players.get(i).getIcon().setFitHeight(gameBoard.getCellWidth() / 2);
            players.get(i).setColIndex(0);
            players.get(i).setRowIndex(gameBoard.getSize() - 1);
            dynamicGridPane.add(players.get(i).getIcon(), 0, gameBoard.getSize() - 1);
            i++;
        }
        while (!(game.isGameFinished())) {
            for (i = 0; i < players.size(); i++) {
                int rollResult = 23 ;
                performPlayerTurn(players.get(i), rollResult, gameBoard, game);

            }
        }

    }
    public static void performPlayerTurn(Player player, int diceResult, GameBoard board,Game game) {
        System.out.print("\n"+player.getPlayerPosition()+"\n"+board.getSize()+"ASDASD\n");
        int newRow = player.getPlayerPosition() % board.getSize() == 0 ? (board.getSize() - (player.getPlayerPosition()/board.getSize())):board.getSize() - ((player.getPlayerPosition()/board.getSize())+1);
        int newCol = player.getPlayerPosition() % board.getSize() == 0 ? board.getSize()-1 : ((player.getPlayerPosition() % board.getSize())-1);
        movePlayerWithAnimation(player, newCol, newRow , board.getCellHeight());
        player.setColIndex(newCol);
        player.setRowIndex(newRow);
        if(player.getPlayerPosition() == board.getSize()* board.getSize()){
            game.setGameFinished(true);
        }
        player.movePlayerTo(player.getPlayerPosition()+diceResult,board.getSize());
    }
    private static void movePlayerWithAnimation(Player player, int newColumnIndex, int newRowIndex, double move) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(ANIMATION_DURATION), player.getIcon());
        transition.setToX((newColumnIndex - player.getColIndex()) * move);
        transition.setToY((newRowIndex - player.getRowIndex()) * move);

        transition.play();
    }

}
/*
        // Update player positions on the board
        game.getGameBoard().updatePlayerPositions(dynamicGridPane);

        // Check if the game is finished or continue with the next player's turn
        if (game.isGameFinished()) {
            System.out.println("Game Over! Winner: " + game.getWinner().getName());
        } else {
            System.out.println("Next player's turn!");
        }
    }

    private boolean askQuestion(Question question) {
        // Implement logic to display the question to the player and get their answer
        // You can use a popup window or any other UI element for this
        // For simplicity, assume the player always answers correctly
        System.out.println("Question: " + question.getQuestionText());
        System.out.println("Options: " + question.getOptions());
        return true; // For simplicity, always return true for correct answer
    }

    public boolean isGameOver() {
        // Check if any end-game conditions are met
        // For example, if a player reaches a specific position on the board
        // or if a certain number of turns have passed
        // Update the 'gameFinished' flag accordingly
    }
    public Player getCurrentPlayer() {
        // Return the player whose turn it is
        // You might want to keep track of the current player index
    }
    public void switchToNextPlayer() {
        // Logic to switch to the next player's turn
    }
    public void updatePlayerPositions() {
        // Logic to update player positions on the board
        // Check for snakes, ladders, or other special tiles
    }

*/


