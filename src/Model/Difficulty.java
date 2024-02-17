package Model;

public enum Difficulty {
    HARD("3"),
    MEDIUM("2"),
    EASY("1");

    private String level;

    Difficulty(String  level) {
        this.level = level;
    }

    public String toString() {
        return level;
    }
}
