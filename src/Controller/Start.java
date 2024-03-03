package Controller;

import Model.Difficulty;
import Model.Question;
import Model.SystemData;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static javafx.fxml.FXMLLoader.load;

public class Start extends Application {





    public static void main(String[] args) {
        launch(args);
        SystemData systemData = SystemData.getInstance();
        boolean loadSuccess = systemData.loadQuestions();

        if (loadSuccess) {
            System.out.println("Succsed to load questions from JSON.");
        } else {
            System.out.println("Failed to load questions from JSON.");
        }
    }
    private static void printLoadedQuestions(SystemData systemData) {
        HashMap<Difficulty, ArrayList<Question>> questions = systemData.getQuestions();
        for (ArrayList<Question> questionList : questions.values()) {
            for (Question question : questionList) {
                System.out.println("Text: " + question.getText());
                System.out.println("Answer 1: " + question.getAnswer1());
                System.out.println("Answer 2: " + question.getAnswer2());
                System.out.println("Answer 3: " + question.getAnswer3());
                System.out.println("Answer 4: " + question.getAnswer4());
                System.out.println("Correct Answer: " + question.getCorrectAnswer());
                System.out.println("Difficulty Level: " + question.getLevel());
                System.out.println("-------------------------");
            }
        }
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file
            Parent root = load(getClass().getResource("/View/MainPage.fxml"));

            // Create the scene
            Scene scene = new Scene(root);
            primaryStage.getIcons().add(new Image("src/View/Photos/SnakeLadders2.png", 100, 100, true, true));
            // Set the scene and show the stage
            primaryStage.setScene(scene);
            primaryStage.setTitle("Snake and Ladders Game"); // Set the title of the window
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading MainPage.fxml: " + e.getMessage());
        }
    }

}
