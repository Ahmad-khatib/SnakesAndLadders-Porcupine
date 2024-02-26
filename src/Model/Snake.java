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
    private int snakeLength;
    private SnakeColor color;
    private int headPosition;
    private int tailPosition;


    public Snake(int snakeId, int snakeLength, SnakeColor color, int headPosition) {
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

    public SnakeColor getColor() {
        return color;
    }

    public void setColor(SnakeColor color) {
        this.color = color;
    }

    public int getHeadPosition() {
        return headPosition;
    }

    public void setHeadPosition(int headPosition, int gridSize) {
        // Ensure head position does not exceed grid boundaries
        this.headPosition = Math.min(gridSize * gridSize - 1, Math.max(0, headPosition));

        // Calculate the tail position based on the snake length and new head position
        this.tailPosition = Math.max(0, this.headPosition - snakeLength + 1);
    }



    public int getTailPosition() {
        return tailPosition;
    }

    public void setTailPosition(int tailPosition, int gridSize) {
        // Ensure tail position does not exceed grid boundaries
        this.tailPosition = Math.min(gridSize * gridSize - 1, Math.max(0, tailPosition));

        // Calculate the head position based on the snake length and new tail position
        this.headPosition = Math.min(gridSize * gridSize - 1, this.tailPosition + snakeLength - 1);
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

    public static Snake generateRandomSnake(int snakeId, String selectedLevel, int gridSize) {
        Random random = new Random();
        int snakeLength = generateRandomLength(selectedLevel, random);
        SnakeColor color = generateRandomColor(random);
        //Effect effect = getEffectFromColor(color); // Set effect based on color

        // Calculate the valid range of cells for the head position
        int validRange = gridSize * (gridSize - 2); // Exclude the first and last rows
        int headPosition = random.nextInt(validRange) + gridSize; // Add an offset of gridSize for the first row

        return new Snake(snakeId, snakeLength, color, headPosition);
    }


    private static Effect getEffectFromColor(SnakeColor color) {
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

    private static int generateRandomLength(String selectedLevel, Random random) {
        switch (selectedLevel) {
            case "Easy":
                return random.nextInt(3) + 1; // Snake length between 1 and 3 for Easy level
            case "Medium":
                return random.nextInt(4) + 2; // Snake length between 2 and 5 for Medium level
            case "Hard":
                return random.nextInt(5) + 3; // Snake length between 3 and 7 for Hard level
            default:
                return random.nextInt(5) + 1; // Default snake length between 1 and 5
        }
    }


    private static SnakeColor generateRandomColor(Random random) {
        // Generate a random snake color
        return SnakeColor.values()[random.nextInt(SnakeColor.values().length)];
    }
}
