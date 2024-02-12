package Model;

import java.util.Objects;

public class Player {
    private int playerId;
    private String playerName;
    private String playerColor;
    private int playerPosition;


    public Player(int playerId, String playerName, int playerPosition) {
        super();
        this.playerId = playerId;
        this.playerName = playerName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return playerId == player.playerId && Objects.equals(playerId, player.playerId) && Objects.equals(playerName, player.playerName) && Objects.equals(playerPosition, player.playerPosition);
    }
    @Override
    public int hashCode() {
        return Objects.hash(playerId, playerName, playerColor, playerPosition);
    }

    @Override
    public String toString() {
        return "Player: " +
                "'" + playerName + '\'' +
                ", Score: " + playerColor;
    }


}
