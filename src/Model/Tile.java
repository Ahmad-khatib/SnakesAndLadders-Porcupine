package Model;

import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {
    private int tileId;
    private int row;
    private int col;
    private String tileType;

    public Tile(int row, int col, String tyleType) {
        this.row = row;
        this.col = col;
        this.tileType=tileType;

    }
    // Getters and Setters for all fields
    public int getTileId() {
        return tileId;
    }

    public void setTileId(int tileId) {
        this.tileId = tileId;
    }

    public String getTileType() {
        return tileType;
    }

    public void setTileType(String tileType) {
        this.tileType = tileType;
    }


    public int getCol() {
        return col;
    }

}
