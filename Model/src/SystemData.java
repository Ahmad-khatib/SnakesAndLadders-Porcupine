public class SystemData {
    private int currentPlayerIndex;
    private Player[] players;
    private GameBoard gameBoard;
    private Snake snake;
    private Ladder ladder;
    private Dice dice;
    private Question question;
    private Tile tile;


    public SystemData() {


    }

    // Getters and setters for currentPlayerIndex
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    // Getters and setters for players array
    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    // Getters and setters for gameBoard
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    // Getters and setters for snake
    public Snake getSnake() {
        return snake;
    }

    public void setSnake(Snake snake) {
        this.snake = snake;
    }

    // Getters and setters for ladder
    public Ladder getLadder() {
        return ladder;
    }

    public void setLadder(Ladder ladder) {
        this.ladder = ladder;
    }

    // Getters and setters for Dice
    public Dice getDice() {
        return dice;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }

    // Getters and setters for Question
    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    // Getters and setters for tile
    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    // Methods for saving and loading the data if needed
    public void saveDataToFile(String Games) {
        // Save data to file
    }

    public void loadDataFromFile(String Games) {
        // Load data from file
    }
}
