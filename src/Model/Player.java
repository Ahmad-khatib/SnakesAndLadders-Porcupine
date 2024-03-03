package Model;

import javafx.scene.image.ImageView;

import java.util.List;
import java.util.Objects;

public class Player {
    private int playerId;
    private String playerName;
    private ImageView Icon;
    private int playerPosition=1;
    private int rowIndex =0;
    private int colIndex =0;

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public int getColIndex() {
        return colIndex;
    }

    public void setColIndex(int colIndex) {
        this.colIndex = colIndex;
    }


    public Player(int playerId, String playerName, ImageView icon, int playerPosition) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.Icon = icon;
        this.playerPosition = playerPosition;
    }

    public ImageView getIcon() {
        return Icon;
    }

    public void setIcon(ImageView icon) {
        Icon = icon;
    }
// Getters and Setters

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }



    public int getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(int playerPosition) {
        this.playerPosition = playerPosition;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerId=" + playerId +
                ", playerName='" + playerName + '\'' +

                ", playerPosition=" + playerPosition +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return playerId == player.playerId &&
                playerPosition == player.playerPosition &&
                Objects.equals(playerName, player.playerName) &&
                Objects.equals(Icon, player.Icon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId, playerName, Icon, playerPosition);
    }

    // Move player to a new position
    public void movePlayerTo(int newPosition, int gridSize) {
        if(newPosition<gridSize*gridSize){
        if (newPosition >= 0) {
            this.playerPosition = newPosition;
        }}
        else if (newPosition>=gridSize*gridSize) {
            this.playerPosition=gridSize*gridSize;

        } else {
            throw new IllegalArgumentException("Invalid position");
        }
    }

    // Check if player has reached the end of the game
    public boolean isEndOfGame(int boardSize, Difficulty difficulty) {
        return playerPosition >= boardSize;
    }

    // Method to move the player backward because of a snake
//    public void movePlayerBackward(Snake snake) {
//        int newPosition = snake.getTailPosition();
//        setPlayerPosition(newPosition);
//    }

    // Method to move the player forward because of a ladder
    public void movePlayerForward(Ladder ladder) {
        if (playerPosition == ladder.getBottomPosition()) {
            playerPosition = ladder.getTopPosition();
        }
    }

    // Check if player is on a ladder
    public boolean isOnLadder(Ladder ladder) {
        return playerPosition == ladder.getBottomPosition();
    }

    // Method to move the player forward by a certain number of steps
    public void movePlayerForward(int steps) {
        if (steps >= 0) {
            this.playerPosition += steps;
        } else {
            throw new IllegalArgumentException("Invalid number of steps");
        }
}
}