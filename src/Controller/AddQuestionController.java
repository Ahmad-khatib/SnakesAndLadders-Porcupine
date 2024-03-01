package Controller;

import Model.Difficulty;
import Model.Question;
import Model.SystemData;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class AddQuestionController {
    @FXML
    private TextArea questionTextArea;

    @FXML
    private TextArea answer1TextArea;

    @FXML
    private TextArea answer2TextArea;

    @FXML
    private TextArea answer3TextArea;

    @FXML
    private TextArea answer4TextArea;

    @FXML
    private ChoiceBox<String> levelChoiceBox;

    @FXML
    private ChoiceBox<String> correctAnswerChoiceBox;

    @FXML
    private Button saveButton;

    private boolean success = false; // Track success status
    private Question existingQuestion;
    private Question newQuestion;
    // Track existing question for editing

    @FXML
    private void initialize() {
        // Initialize choice boxes
        levelChoiceBox.getItems().addAll("EASY", "MEDIUM", "HARD");
        correctAnswerChoiceBox.getItems().addAll("1", "2", "3", "4");

        // Set default values
        levelChoiceBox.setValue("EASY");
        correctAnswerChoiceBox.setValue("1");

        // Set action for save button
        saveButton.setOnAction(event -> saveQuestion());
    }

    public void setExistingQuestion(Question question) {
        // Set existing question data into the fields for editing
        existingQuestion = question;
        questionTextArea.setText(question.getText());
        answer1TextArea.setText(question.getAnswer1());
        answer2TextArea.setText(question.getAnswer2());
        answer3TextArea.setText(question.getAnswer3());
        answer4TextArea.setText(question.getAnswer4());
        levelChoiceBox.setValue(question.getLevel().toString());
        correctAnswerChoiceBox.setValue(question.getCorrectAnswer());
    }

    private void saveQuestion() {
        // Retrieve entered data
        String questionText = questionTextArea.getText();
        String answer1 = answer1TextArea.getText();
        String answer2 = answer2TextArea.getText();
        String answer3 = answer3TextArea.getText();
        String answer4 = answer4TextArea.getText();
        String selectedLevel = levelChoiceBox.getValue();
        String correctAnswer = correctAnswerChoiceBox.getValue();

        // Validate input
        if (questionText.isEmpty() || answer1.isEmpty() || answer2.isEmpty() || answer3.isEmpty() || answer4.isEmpty()) {
            // Show error message if any field is empty
            System.out.println("Please fill in all fields.");
            return;
        }

        // Create a new Question object
        Question newQuestion = new Question(questionText, answer1, answer2, answer3, answer4, correctAnswer, Difficulty.valueOf(selectedLevel));

        // Check if it's an existing question being edited

            // Save the question using SystemData model
            success = SystemData.getInstance().addQuestion(newQuestion);


        if (success) {
            System.out.println("Question saved successfully.");
            // Clear fields after saving
            clearFields();
        } else {
            System.out.println("Failed to save question.");
        }

    }

    private void clearFields() {
        questionTextArea.clear();
        answer1TextArea.clear();
        answer2TextArea.clear();
        answer3TextArea.clear();
        answer4TextArea.clear();
        levelChoiceBox.setValue("EASY");
        correctAnswerChoiceBox.setValue("1");
    }

    public boolean isQuestionAdded() {
        return success;
    }

    public Question getNewQuestion() {
        return newQuestion;
    }
}
