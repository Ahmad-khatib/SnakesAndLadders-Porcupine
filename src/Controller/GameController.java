package Controller;

import Model.*;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    public static void Start(Game game , GridPane dynamicGridPane){
        ArrayList<Player> players = game.getPlayers();
        GameBoard gameBoard = game.getGameBoard();
        HashMap<Difficulty, ArrayList<Question>> Questions = game.getQuestions();
        int i = 0;
        while(i < players.size()){
            players.get(i).getIcon().setFitWidth(gameBoard.getCellWidth()/2);
            players.get(i).getIcon().setFitHeight(gameBoard.getCellWidth()/2);
            dynamicGridPane.add(players.get(i).getIcon(),0,gameBoard.getSize()-1);
            i++;
        }
        while(!(game.isGameFinished())){
            for(i =0 ;i  < players.size(); i++){


                game.setGameFinished(true);
            }
        }



    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }



}
