package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.util.Arrays;
import java.util.List;

public class PopUpQuestionController {
    @FXML
    private Button submit;
    @FXML
    private Label questionLabel;

    @FXML
    private ToggleGroup answerGroup;

    @FXML
    private RadioButton radioButton1;

    @FXML
    private RadioButton radioButton2;

    @FXML
    private RadioButton radioButton3;

    @FXML
    private RadioButton radioButton4;

    private String correctAnswer;

    private int stepsToMove = 0; // Variable to store the steps to move

    public void initialize() {
        // Initialize the answer group
        answerGroup = new ToggleGroup();
        // Assign toggle group to radio buttons
        radioButton1.setToggleGroup(answerGroup);
        radioButton2.setToggleGroup(answerGroup);
        radioButton3.setToggleGroup(answerGroup);
        radioButton4.setToggleGroup(answerGroup);
    }

    // Method to set the question and answer options
    public void setQuestion(String questionText, String answer1, String answer2, String answer3, String answer4, String correctAnswer) {
        questionLabel.setText(questionText);
        radioButton1.setText(answer1);
        radioButton2.setText(answer2);
        radioButton3.setText(answer3);
        radioButton4.setText(answer4);

        // Set the correct answer
        this.correctAnswer = correctAnswer;
    }

    // Method to handle submitting the answer
    @FXML
    private void submitAnswer() {
        // Get the selected radio button
        RadioButton selectedRadioButton = (RadioButton) answerGroup.getSelectedToggle();
        if (selectedRadioButton != null) {
            String selectedAnswer = selectedRadioButton.getText();

            // Check if the selected answer is correct
            boolean isCorrect = correctAnswer.equals(selectedAnswer);

            // Determine the steps to move based on the correctness of the answer and difficulty level
            if (isCorrect) {
                switch (correctAnswer) {
                    case "1":
                        // For easy level, do nothing
                        stepsToMove = 0;
                        break;
                    case "2":
                        // For medium level, go back 2 steps
                        stepsToMove = -2;
                        break;
                    case "3":
                        // For hard level, move forward 1 step
                        stepsToMove = 1;
                        break;
                }
            } else {
                switch (correctAnswer) {
                    case "1":
                        // For easy level, go back 1 step
                        stepsToMove = -1;
                        break;
                    case "2":
                        // For medium level, go back 2 steps
                        stepsToMove = -2;
                        break;
                    case "3":
                        // For hard level, go back 3 steps
                        stepsToMove = -3;
                        break;
                }
            }
        }
    }

    // Method to get the steps to move
    public int getStepsToMove() {
        return stepsToMove;
    }
}
