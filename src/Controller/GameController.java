package Controller;

import Model.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    @FXML
    private ImageView diceImage;
    @FXML
    private ImageView diceImage2;


    public static void Start(Game game, GridPane dynamicGridPane) throws IOException {
        ArrayList<Player> players = game.getPlayers();
        GameBoard gameBoard = game.getGameBoard();
        HashMap<Difficulty, ArrayList<Question>> questions = game.getQuestions();
        int currentPosition = 1;
        int i = 0;
        while (i < players.size()) {
            players.get(i).getIcon().setFitWidth(gameBoard.getCellWidth() / 2);
            players.get(i).getIcon().setFitHeight(gameBoard.getCellWidth() / 2);
            dynamicGridPane.add(players.get(i).getIcon(), 0, gameBoard.getSize() - 1);
            i++;
        }
        try {
            FXMLLoader loader = new FXMLLoader(GameController.class.getResource("/View/BoardGame.fxml"));
            Parent root = loader.load();
            GameBoardController boardGameController = loader.getController();

            while (!(game.isGameFinished())) {
                for (i = 0; i < players.size(); i++) {
                    boardGameController.rollButton.setOnAction(event -> boardGameController.roll());
                    int rollResult = boardGameController.roll();
                    System.out.print(rollResult);
                    players.get(i).movePlayerTo(currentPosition + rollResult);

                    game.setGameFinished(true);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }



}
