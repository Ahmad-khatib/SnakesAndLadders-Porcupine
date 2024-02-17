package Model;

import java.util.Random;

public class Dice {
    private int diceId;
    private DiceDifficulty diceDifficulty;

    public enum DiceDifficulty {
        EASY(8),
        MEDIUM(9),
        HARD(10);

        private final int possibleOptions;

        DiceDifficulty(int possibleOptions) {
            this.possibleOptions = possibleOptions;
        }

        public int getPossibleOptions() {
            return possibleOptions;
        }
    }

    public Dice(int diceId, DiceDifficulty diceDifficulty) {
        this.diceId = diceId;
        this.diceDifficulty = diceDifficulty;
    }

    public int getDiceId() {
        return diceId;
    }

    public void setDiceId(int diceId) {
        this.diceId = diceId;
    }

    public DiceDifficulty getDiceDifficulty() {
        return diceDifficulty;
    }

    public void setDiceDifficulty(DiceDifficulty diceDifficulty) {
        this.diceDifficulty = diceDifficulty;
    }

    public int rollDice() {
        Random random = new Random();
        return random.nextInt(diceDifficulty.getPossibleOptions());
    }

    // Method to determine the player's action based on the dice roll
    public void determinePlayerAction(int diceRoll, Player player) {
        switch (diceRoll) {
            case 0:
                // Do nothing
                break;
            case 1:
                player.movePlayerForward(1);  // Move one step forward
                break;
            case 2:
                player.movePlayerForward(2);  // Move two steps forward
                break;
            case 3:
                player.movePlayerForward(3);  // Move three steps forward
                break;
            case 4:
                player.movePlayerForward(4);  // Move four steps forward
                break;
            case 5:
                player.movePlayerForward(5);  // Move five steps forward
                break;
            case 6:
                player.movePlayerForward(6);  // Move six steps forward
                break;
            case 7:
                String easyQuestion = Question.getQuestion(Difficulty.EASY);
                break;
            case 8:
                String mediumQuestion = Question.getQuestion(Difficulty.MEDIUM);
                break;
            case 9:
                String hardQuestion = Question.getQuestion(Difficulty.HARD);
                break;
            default:
                throw new IllegalArgumentException("Invalid dice roll");
        }
    }
}