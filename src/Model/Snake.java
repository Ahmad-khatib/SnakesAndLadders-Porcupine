package Model;

import java.util.Objects;
import java.util.Random;

public class Snake {
    public enum SnakeColor {
        YELLOW,
        GREEN,
        BLUE,
        RED
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
    private SnakeColor color;
    private int headPosition;
    private int tailPosition;
    private static final int GRID_SIZE = 13; // Example grid size

    public Snake(int snakeId, SnakeColor color, int headPosition,int tailPosition) {
        this.snakeId = snakeId;
        this.color = color;
        this.headPosition = headPosition;
        this.tailPosition=tailPosition;
    }

    // Getters and setters

    public int getSnakeId() {
        return snakeId;
    }

    public void setSnakeId(int snakeId) {
        this.snakeId = snakeId;
    }

    public SnakeColor getColor() {
        return color;
    }

    public void setColor(SnakeColor color) {
        this.color = color;
    }

    public int getHeadPosition() {
        return headPosition;
    }

    public void setHeadPosition(int headPosition) {
        this.headPosition = validatePosition(headPosition);
    }

    // Validate position to ensure it's within the bounds of the game board
    private int validatePosition(int position) {
        return Math.min(GRID_SIZE * GRID_SIZE - 1, Math.max(0, position));
    }

    // Calculate the tail position based on the head position and snake length
    public int getTailPosition() {
        return tailPosition;
    }


    // Calculate the snake length based on the selected game level
    private int getSnakeLength(String selectedLevel) {
        switch (selectedLevel) {
            case "Easy":
                return 1; // Minimum snake length for Easy level
            case "Medium":
                return 2; // Fixed snake length for Medium level
            case "Hard":
                return 3; // Fixed snake length for Hard level
            default:
                throw new IllegalArgumentException("Invalid difficulty level: " + selectedLevel);
        }
    }

    // Generate a random snake based on the selected game level
  /* public static Snake generateRandomSnake(int snakeId, String selectedLevel) {
        Random random = new Random();
        SnakeColor color = generateRandomColor(random);
        int validRange = GRID_SIZE * (GRID_SIZE - 2); // Exclude the first and last rows
        int headPosition = random.nextInt(validRange) + GRID_SIZE; // Add an offset of GRID_SIZE for the first row
        return new Snake(snakeId, color, headPosition);
    }*/

    // Generate a random color for the snake
    private static SnakeColor generateRandomColor(Random random) {
        return SnakeColor.values()[random.nextInt(SnakeColor.values().length)];
    }

    // Get the effect of the snake based on its color
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
//    public void handleSnakeEncounter(Player player) {
//        player.movePlayerBackward(this);
//    }


    public String toString(String selectedLevel) {
        return "Snake{" +
                "snakeId=" + snakeId +
                ", color=" + color +
                ", headPosition=" + headPosition +
                ", tailPosition=" + getTailPosition() +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Snake snake = (Snake) o;
        return snakeId == snake.snakeId &&
                headPosition == snake.headPosition &&
                color == snake.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(snakeId, color, headPosition);
}
}