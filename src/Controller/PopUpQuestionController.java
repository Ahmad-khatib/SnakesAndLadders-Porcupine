package Controller;

import Model.Difficulty;
import Model.Question;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;

import java.util.concurrent.CountDownLatch;

public class PopUpQuestionController {
    @FXML
    private Button Submit;
    @FXML
    private Label questionLabel;
    @FXML
    private Label answerLabel;

    @FXML
    private RadioButton radioButton1;

    @FXML
    private RadioButton radioButton2;

    @FXML
    private RadioButton radioButton3;

    @FXML
    private RadioButton radioButton4;
    private CountDownLatch latch;
    private String correctAnswer;
    private Runnable submitCallback;

    private int stepsToMove = 0; // Variable to store the steps to move
    private Difficulty d = null;
    private boolean answerSubmitted = false;
    // ToggleGroup to group the radio buttons
    private ToggleGroup toggleGroup;

    public void initialize() {

        // Initialize the toggle group
        toggleGroup = new ToggleGroup();
        Submit.setDisable(false);

        // Assign toggle group to radio buttons
        radioButton1.setToggleGroup(toggleGroup);
        radioButton2.setToggleGroup(toggleGroup);
        radioButton3.setToggleGroup(toggleGroup);
        radioButton4.setToggleGroup(toggleGroup);


    }

    // Method to set the question and answer options
    public void setQuestion(Question q) {
        questionLabel.setText(q.getText());
        radioButton1.setText(q.getAnswer1());
        radioButton2.setText(q.getAnswer2());
        radioButton3.setText(q.getAnswer3());
        radioButton4.setText(q.getAnswer4());
        d = q.getLevel();
        // Set the correct answer

        // Set the correct answer based on the text of the correct radio button
        switch (q.getCorrectAnswer()) {
            case "1":
                correctAnswer = radioButton1.getText();
                break;
            case "2":
                correctAnswer = radioButton2.getText();
                break;
            case "3":
                correctAnswer = radioButton3.getText();
                break;
            case "4":
                correctAnswer = radioButton4.getText();
                break;
            default:
                // Handle the case where the correct answer is not 1, 2, 3, or 4
                correctAnswer = "";
                break;
        }
    }

    // Method to handle submitting the answer
    @FXML
    int submitAnswer() {
        // Get the selected radio button from the toggle group
        RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
        if (selectedRadioButton != null) {
            Submit.setDisable(true);
            String selectedAnswer = selectedRadioButton.getText();

            // Check if the selected answer is correct
            boolean isCorrect = correctAnswer.equals(selectedAnswer);
            System.out.print(correctAnswer +" ======= "+selectedAnswer+"\n" );

            // Determine the steps to move based on the correctness of the answer and difficulty level
            if (isCorrect) {// Handle correct answer logic

                switch (d) {
                    case EASY:
                        // For easy level, do nothing
                        stepsToMove = 0;
                        break;
                    case MEDIUM:
                        // For medium level, do nothing
                        stepsToMove = 0;
                        break;
                    case HARD:
                        // For hard level, move forward 1 step
                        stepsToMove = 1;
                        break;
                }
                answerLabel.setText("Correct Answer!!\n you will be moved "+stepsToMove+ "steps forward");
                answerLabel.setTextFill(Color.GREEN);


            } else {// Handle incorrect answer logic

                switch (d) {
                    case EASY:
                        // For easy level, GO 1 STEP BACK
                        stepsToMove = -1;
                        break;
                    case MEDIUM:
                        // For medium level, go back 2 steps BACK
                        stepsToMove = -2;
                        break;
                    case HARD:
                        // For hard level, move forward 3 step BACK
                        stepsToMove = -3;
                        break;
                }
                answerLabel.setText("Wrong Answer!!\n you will be moved "+-stepsToMove+" steps backward");
                answerLabel.setTextFill(Color.RED);

            }
            submitCallback.run();
        }
        return stepsToMove;
    }

    public void setSubmitCallback(Runnable submitCallback) {
        this.submitCallback = submitCallback;
    }
    // Method to get the steps to move
    public int getStepsToMove() {
        return stepsToMove;
    }
}
