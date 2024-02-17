package Model;

import java.util.Objects;
public class Snake {
    public enum Color {
        YELLOW, GREEN, BLUE, RED
    }

    public enum Effect {
        YELLOW(-1),  // Move one row back
        GREEN(-2),   // Move two rows back
        BLUE(-3),    // Move three rows back
        RED(0);      // Go back to the starting position

        private final int movement;

        Effect(int movement) {
            this.movement = movement;
        }

        public int getMovement() {
            return movement;
        }
    }
    private int snakeId;
    private int snakeLength;
    private Color color;
    private int headPosition;
    private int tailPosition;

    public Snake(int snakeId, int snakeLength, Color color, int headPosition) {
        this.snakeId = snakeId;
        this.snakeLength = snakeLength;
        this.color = color;
        this.headPosition = headPosition;
        this.tailPosition = headPosition - snakeLength + 1;
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    public int getHeadPosition() {
        return headPosition;
    }

    public void setHeadPosition(int headPosition) {
        this.headPosition = headPosition;
        this.tailPosition = headPosition - snakeLength + 1;
    }

    public int getTailPosition() {
        return tailPosition;
    }
    @Override
    public String toString() {
        return "Snake{" +
                "snakeId=" + snakeId +
                ", snakeLength=" + snakeLength +
                ", snakeColor='" + color + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Snake snake = (Snake) o;

        if (snakeId != snake.snakeId) return false;
        if (snakeLength != snake.snakeLength) return false;
        return Objects.equals(color, snake.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(snakeId, snakeLength, color);
    }
    public Effect getSnakeEffect() {
        switch (color) {
            case YELLOW:
                return Effect.YELLOW;
            case GREEN:
                return Effect.GREEN;
            case BLUE:
                return Effect.BLUE;
            case RED:
                return Effect.RED;
            default:
                throw new IllegalArgumentException("Unsupported snake color: " + color);
        }
    }
    // Method to handle the behavior when a player encounters a snake
    public void handleSnakeEncounter(Player player) {
        player.movePlayerBackward(this);
    }
}
