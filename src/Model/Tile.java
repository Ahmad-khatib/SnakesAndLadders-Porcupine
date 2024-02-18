package Model;

import java.util.Random;

public class Tile {
    private int tileId;
    private TileType tileType;
    private int position;

    public enum TileType {
        NORMAL,
        QUESTION,
        SURPRISE_JUMP
    }

    public Tile(int tileId, TileType tileType, int position) {
        this.tileId = tileId;
        this.tileType = tileType;
        this.position = position;
    }

    // Getters and Setters for all fields

    public int getTileId() {
        return tileId;
    }

    public void setTileId(int tileId) {
        this.tileId = tileId;
    }

    public TileType getTileType() {
        return tileType;
    }

    public void setTileType(TileType tileType) {
        this.tileType = tileType;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    // Method to handle special actions for each tile type
    public void handleSpecialAction(Player player) {
        switch (tileType) {
            case NORMAL:
                // Normal tile, no special action
                break;
            case QUESTION:
                String randomQuestion;
                Difficulty randomDifficulty = getRandomDifficulty();  //Get a random difficulty level
                randomQuestion = Question.getQuestion(randomDifficulty);
                break;
            case SURPRISE_JUMP:
                // Special action for surprise tile - move player forward 10 steps
                player.movePlayerForward(10);
                break;
            default:
                throw new IllegalArgumentException("Invalid tile type");
        }
    }

    // Method to check if the tile is a special tile (question or surprise jump)
    public boolean isSpecialTile() {
        return tileType == TileType.QUESTION || tileType == TileType.SURPRISE_JUMP;
    }

    private Difficulty getRandomDifficulty() {
        Random random = new Random();
        int randomDifficultyIndex = random.nextInt(Difficulty.values().length);
        return Difficulty.values()[randomDifficultyIndex];
    }

}
