package Model;

import java.util.ArrayList;
import java.util.HashMap;

public class Game {
    private  int Id;
    private  GameBoard gameBoard;
    private  ArrayList<Player> Players;
    private  HashMap<Difficulty, ArrayList<Question>> Questions;
    private boolean gameFinished = new Boolean(false);

    public Game(int id,GameBoard gameBoard,ArrayList<Player> players,HashMap<Difficulty, ArrayList<Question>> questions){
        this.Id=id;
        this.gameBoard=gameBoard;
        this.Players=players;
        this.Questions=questions;

    }

    public Game() {
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