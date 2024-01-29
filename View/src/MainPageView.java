import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

class MainPage extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Snake and Ladders Game");

        // Create buttons
        Button startGameButton = new Button("Start Game");
        Button viewHistoryButton = new Button("View History");
        Button manageQuestionsButton = new Button("Manage Questions");

        // Set event handlers for the buttons
        startGameButton.setOnAction(e -> startGame());
        viewHistoryButton.setOnAction(e -> viewHistory());
        manageQuestionsButton.setOnAction(e -> manageQuestions());

        // Create instruction label
        String instructions = "Instructions:\nYour instructions here...";
        javafx.scene.control.Label instructionLabel = new javafx.scene.control.Label(instructions);

        // Create layout and add buttons and instruction label
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(startGameButton, viewHistoryButton, manageQuestionsButton, instructionLabel);

        // Create scene and set it in the stage
        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }

    private void startGame() {
        // Code to start the game
        System.out.println("Game started!");
    }

    private void viewHistory() {
        // Code to view game history
        System.out.println("Viewing game history");
    }

    private void manageQuestions() {
        // Code to manage game questions
        System.out.println("Managing questions");
    }
}
