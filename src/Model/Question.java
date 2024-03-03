package Model;


import Controller.ManageQuestionsController;

import java.util.ArrayList;
import java.util.Objects;

public class Question {


    private final ArrayList<Question> questionList;

    private String text;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String correctAnswer;
    private Difficulty level;

    public Question(String text, String answer1, String answer2, String answer3, String answer4, String correctAnswer, Difficulty level) {
        this.text = text;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.correctAnswer = correctAnswer;
        this.level = level;
        this.questionList = new ArrayList<>();
    }



    public static String getQuestion(Difficulty randomDifficulty) {
        return null;
    }



    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Difficulty getLevel() {
        return level;
    }

    public void setLevel(Difficulty level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(text, question.text) && Objects.equals(answer1, question.answer1) && Objects.equals(answer2, question.answer2) && Objects.equals(answer3, question.answer3) && Objects.equals(answer4, question.answer4) && Objects.equals(correctAnswer, question.correctAnswer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, answer1, answer2, answer3, answer4, correctAnswer);
    }

    public Boolean checkCorrect(String answer) {
        return answer.equals(this.correctAnswer);
    }

    public void registerObserver(ManageQuestionsController manageQuestionsController) {
    }
}