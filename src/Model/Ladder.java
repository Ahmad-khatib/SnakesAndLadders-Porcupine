package Model;

import java.util.Random;

public class Ladder {
    private int topPosition;
    private int bottomPosition;

    public Ladder(int topPosition, int bottomPosition) {
        this.topPosition = topPosition;
        this.bottomPosition = bottomPosition;
    }

    public int getTopPosition() {
        return topPosition;
    }

    public int getBottomPosition() {
        return bottomPosition;
    }

    public static Ladder generateRandomLadder(int ladderIndex, int gridSize) {
        Random random = new Random();
        int top = random.nextInt((gridSize * gridSize) - 1) + 1; // Random top position excluding the first cell
        int bottom = random.nextInt(top); // Random bottom position below the top position
        return new Ladder(top, bottom);
    }
}
