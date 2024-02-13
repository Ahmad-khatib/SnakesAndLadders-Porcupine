public class Player extends GameBoard {
    private String playername;
    private String color;
    private int position;

    // Getter and Setter for player name
    public String getPlayername() {
        return playername;
    }
    public void setPlayername(String playername) {
        this.playername = playername;
    }

    // Getter and Setter for player color
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

    // Getter and Setter for player position
    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
    }

}
