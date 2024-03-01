package Controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import Model.Question;

public class AddQuestionController {

    @FXML
    private TextField questionField;

    @FXML
    private TextField answer1Field;

    @FXML
    private TextField answer2Field;

    @FXML
    private TextField answer3Field;

    @FXML
    private TextField answer4Field;

    @FXML
    private TextField correctAnswerField;

    @FXML
    private TextField difficultyField;

    @FXML
    private Button addButton;

    @FXML
    private void initialize() {
        // Initialize method
    }

    @FXML
    private void handleAddButton(ActionEvent event) {
        String questionText = questionField.getText();
        String answer1 = answer1Field.getText();
        String answer2 = answer2Field.getText();
        String answer3 = answer3Field.getText();
        String answer4 = answer4Field.getText();
        String correctAnswer = correctAnswerField.getText();
        String difficultyText = difficultyField.getText();
        // Validate inputs and create a new Question object
        try {
            int difficulty = Integer.parseInt(difficultyText);
            Question question = new QuestionquestionText, answer1, answer2, answer3, answer4, correctAnswer);

            // Add the question to your data structure or perform any other necessary actions
            // For example, you can call a method in your SystemData class to add the question
            // SystemData.getInstance().addQuestion(question);

            // Close the window or perform any other necessary actions
            // You can access the stage using the addButton's scene and then get the window and close it
            // Stage stage = (Stage) addButton.getScene().getWindow();
            // stage.close();
        } catch (NumberFormatException e) {
            // Handle invalid input for difficulty
            e.printStackTrace();
        }
    }
}
