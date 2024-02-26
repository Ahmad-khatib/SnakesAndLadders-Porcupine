package Model;

import java.util.List;
import java.util.Objects;
public class Player {
    private int playerId;
    private String playerName;
    private String playerColor;
    private int playerPosition;
    private List<Player> playersList;

    public Player ( int playerId, String playerName, String playerColor, int playerPosition ) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.playerColor = playerColor;
        this.playerPosition = playerPosition;

    }

    // Getters and Setters for all fields
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

    public String getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }

    public int getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(int playerPosition) {
        this.playerPosition = playerPosition;
    }
    public void setPlayersList(List<Player> playersList) {
        this.playersList = playersList;
    }

    @Override
    public String toString () {
        return "Player: {" + playerId + '\'' +
                ", playerName" + playerName +
                ", playerColor=" + playerColor +
                '}';
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return playerId == player.playerId && Objects.equals(playerName, player.playerName) && Objects.equals(playerColor, player.playerColor) && Objects.equals(playerPosition, player.playerPosition);
    }
    public int hashCode() {
        return Objects.hash(playerId, playerName, playerColor, playerPosition);
    }
    // move player to a new position
    public void movePlayerTo(int newPosition) {
        // Checking that the new position is not negative
        if (newPosition >= 0) {
            // Updating the player's position field
            this.playerPosition = newPosition;
        } else {
            throw new IllegalArgumentException("Invalid position");
        }
    }

    public boolean isEndOfGame(int BoardSize, Difficulty difficulty) {
        // Checking if the player has reached the end of the board
        if (this.playerPosition >= BoardSize) {
            // Getting the winning player based on the difficulty level
            Player winningPlayer = getWinningPlayer(playersList, BoardSize, difficulty);
            return winningPlayer != null; // The game is over if there is a player who has reached the end of the game
        }
        return false;
    }

    public Player getWinningPlayer(List<Player> players, int winningPosition, Difficulty difficulty) {
        for (Player player : players) {
            if (player.getPlayerPosition() >= winningPosition) {
                return player;
            }
        }
        return null; // No player reached the end of the board
    }

    // move player backward because a snake
    public void movePlayerBackward(Snake snake) {
        int newPosition = 0;

        switch (snake.getSnakeEffect()) {
            case YELLOW:
                newPosition = snake.getTailPosition();
                break;
            case GREEN:
                newPosition = snake.getTailPosition();
                break;
            case BLUE:
                newPosition = snake.getTailPosition();
                break;
            case RED:
                newPosition = 1; // Place the player at the beginning of the game
                break;
            default:
                throw new IllegalArgumentException("Unexpected snake effect: " + snake.getSnakeEffect());
        }

        setPlayerPosition(newPosition);
    }

    // move player forward because a ladder
    public void movePlayerForward(Ladder ladder) {
        if (playerPosition == ladder.getBottomPosition()) {
            playerPosition = ladder.getTopPosition();
        }

    }

    // check if player is on a ladder
    public boolean isOnLadder(Ladder ladder) {
        return playerPosition == ladder.getBottomPosition();
    }

    // Method to move the player forward because a dice
    public void movePlayerForward(int steps) {
        // Checking that the new position is not negative
        if (steps >= 0) {
            // Updating the player's position field
            this.playerPosition += steps;
        } else {
            throw new IllegalArgumentException("Invalid number of steps");
        }
    }
}




