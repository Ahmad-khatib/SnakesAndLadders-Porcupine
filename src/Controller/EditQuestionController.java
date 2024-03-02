package Controller;

import Model.Difficulty;
import Model.Question;
import Model.SystemData;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class EditQuestionController {
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

    private Question questionToEdit;
    private boolean questionEdited = false;

    public void setQuestion(Question question) {
        this.questionToEdit = question;
        // Set the initial values in the UI fields based on the provided question
        questionTextArea.setText(question.getText());
        answer1TextArea.setText(question.getAnswer1());
        answer2TextArea.setText(question.getAnswer2());
        answer3TextArea.setText(question.getAnswer3());
        answer4TextArea.setText(question.getAnswer4());
        levelChoiceBox.setValue(question.getLevel().toString());
        correctAnswerChoiceBox.setValue(question.getCorrectAnswer());

    }

    @FXML
    private void initialize() {
        // Initialize choice boxes
        levelChoiceBox.getItems().addAll("EASY", "MEDIUM", "HARD");
        correctAnswerChoiceBox.getItems().addAll("1", "2", "3", "4");

        // Set action for save button
        saveButton.setOnAction(event -> saveEditedQuestion());
    }

    private void saveEditedQuestion() {
        // Retrieve edited data
        String newText = questionTextArea.getText();
        String newAnswer1 = answer1TextArea.getText();
        String newAnswer2 = answer2TextArea.getText();
        String newAnswer3 = answer3TextArea.getText();
        String newAnswer4 = answer4TextArea.getText();
        String newLevel = levelChoiceBox.getValue();
        String newCorrectAnswer = correctAnswerChoiceBox.getValue();

        // Update the question object with edited data
        questionToEdit.setText(newText);
        questionToEdit.setAnswer1(newAnswer1);
        questionToEdit.setAnswer2(newAnswer2);
        questionToEdit.setAnswer3(newAnswer3);
        questionToEdit.setAnswer4(newAnswer4);
        questionToEdit.setLevel(Difficulty.valueOf(newLevel));
        questionToEdit.setCorrectAnswer(newCorrectAnswer);

        // Update the question in the data model
        SystemData.getInstance().editQuestion(questionToEdit);

        // Set the flag indicating that the question was edited
        questionEdited = true;

        // Close the edit question dialog
        closeWindow();
    }

    private void closeWindow() {
        // Get a reference to the stage and close it
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    public boolean isQuestionEdited() {
        return questionEdited;
    }
}
