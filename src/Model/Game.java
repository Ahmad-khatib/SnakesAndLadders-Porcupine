package Model;

import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

public class Game {
    private  int Id;
    private  GameBoard gameBoard;
    private  ArrayList<Player> Players;
    private  HashMap<Difficulty, ArrayList<Question>> Questions;
    private boolean gameFinished = new Boolean(false);
    private String WINNERNAME;
    private String GAMETIME;
   private int GAMELEVEL;

    public Game(int id,GameBoard gameBoard,ArrayList<Player> players,HashMap<Difficulty, ArrayList<Question>> questions){
        this.Id=id;
        this.gameBoard=gameBoard;
        this.Players=players;
        this.Questions=questions;

    }

    public Game() {
    }
    public Game(String winnerName, String time, int gameLevel){
        this.WINNERNAME=winnerName;
        this.GAMETIME=time;
        this.GAMELEVEL=gameLevel;

    }

    public String getWINNERNAME() {
        return WINNERNAME;
    }

    public String getGAMETIME() {
        return GAMETIME;
    }

    public int getGAMELEVEL() {
        return GAMELEVEL;
    }

    public void setWINNERNAME(String WINNERNAME) {
        this.WINNERNAME = WINNERNAME;
    }

    public void setGAMETIME(String GAMETIME) {
        this.GAMETIME = GAMETIME;
    }

    public void setGAMELEVEL(int GAMELEVEL) {
        this.GAMELEVEL = GAMELEVEL;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public void setGameFinished(boolean gameFinished) {
        this.gameFinished = gameFinished;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public ArrayList<Player> getPlayers() {
        return Players;
    }

    public void setPlayers(ArrayList<Player> players) {
        Players = players;
    }

    public HashMap<Difficulty, ArrayList<Question>> getQuestions() {
        return Questions;
    }

    public void setQuestions(HashMap<Difficulty, ArrayList<Question>> questions) {
        Questions = questions;
}
}