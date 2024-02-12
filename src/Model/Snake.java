package Model;

import java.util.Objects;

public class Snake {

    private int snakeId;
    private int snakeLength;
    private String snakeColor;

    public Snake(int snakeId, int snakeLength, String snakeColor) {
        super();
        this.snakeId = snakeId;
        this.snakeLength = snakeLength;
        this.snakeColor = snakeColor;
    }
    // Getters and Setters for all fields
    public int getSnakeId() {
        return snakeId;
    }

    public void setSnakeId(int snakeId) {
        this.snakeId = snakeId;
    }

    public int getSnakeLength() {
        return snakeLength;
    }

    public void setSnakeLength(int snakeLength) {
        this.snakeLength = snakeLength;
    }

    public String getSnakeColor() {
        return snakeColor;
    }

    public void setSnakeColor(String snakeColor) {
        this.snakeColor = snakeColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Snake snake = (Snake) o;
        return snakeId == snake.snakeId && Objects.equals(snakeLength, snake.snakeLength) && Objects.equals(snakeColor, snake.snakeColor) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(snakeId, snakeLength, snakeColor);
    }
    @Override
    public String toString() {
        return "Player: " +
                "'" + snakeId + '\'';

    }

}
