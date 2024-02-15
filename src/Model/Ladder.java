package Model;

import java.util.Objects;
public class Ladder {
    private int ladderId;
    private int ladderLength;
    private int startPosition;
    private int endPosition;

    public Ladder(int ladderId, int ladderLength, int startPosition) {
        this.ladderId = ladderId;
        this.ladderLength = ladderLength;
        this.startPosition = startPosition;
        this.endPosition = startPosition + ladderLength;
    }

    // Getters and Setters for all fields
    public int getLadderId() {
        return ladderId;
    }

    public void setLadderId(int ladderId) {
        this.ladderId = ladderId;
    }

    public int getLadderLength() {
        return ladderLength;
    }

    public void setLadderLength(int ladderLength) {
        this.ladderLength = ladderLength;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    @Override
    public String toString() {
        return "Ladder{" +
                "ladderId=" + ladderId +
                ", ladderLength=" + ladderLength +
                ", startPosition=" + startPosition +
                ", endPosition=" + endPosition +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ladder ladder = (Ladder) o;
        return ladderId == ladder.ladderId &&
                ladderLength == ladder.ladderLength &&
                startPosition == ladder.startPosition &&
                endPosition == ladder.endPosition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ladderId, ladderLength, startPosition, endPosition);
    }

}
