package Model;

import java.util.Objects;

public class Question {
    private int questionId;
    private int difficultLevel;
    private String text;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String correctAnswer;

    public Question ( String text, String answer1, String answer2, String answer3, String answer4, String correctAnswer, int difficultLevel ) {
        this.text = text;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.correctAnswer = correctAnswer;
        this.difficultLevel = difficultLevel;

    }


    // Getters and Setters for all fields
    public int getQuestionId () {

        return questionId;
    }

    public Question ( String text, int correctAnswer, Difficulty level ) {

    }

    public void setQuestionId ( int questionId ) {
        this.questionId = questionId;
    }

    public int getDifficultLevel () {
        return difficultLevel;
    }

    public void setDifficultLevel ( int difficultLevel ) {
        this.difficultLevel = difficultLevel;
    }

    public String getText () {
        return text;
    }

    public void setText ( String text ) {
        this.text = text;
    }


    public String getAnswer1 () {
        return answer1;
    }

    public void setAnswer1 ( String answer1 ) {
        this.answer1 = answer1;
    }

    public String getAnswer2 () {
        return answer2;
    }

    public void setAnswer2 ( String answer2 ) {
        this.answer2 = answer2;
    }

    public String getAnswer3 () {
        return answer3;
    }

    public void setAnswer3 ( String answer3 ) {
        this.answer3 = answer3;
    }

    public String getAnswer4 () {
        return answer4;
    }

    public void setAnswer4 ( String answer4 ) {
        this.answer4 = answer4;
    }

    public String getCorrectAnswer () {
        return correctAnswer;
    }

    public void setCorrectAnswer ( String correctAnswer ) {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public String toString () {
        return "Question: {" + text + '\'' +
                ", correctAnswer=" + correctAnswer +
                ", DifficultLevel=" + difficultLevel +
                '}';
    }

    @Override
    public boolean equals ( Object o ) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;

        if (!Objects.equals(text, question.text)) return false;
        if (!Objects.equals(answer1, question.answer1)) return false;
        if (!Objects.equals(answer2, question.answer2)) return false;
        if (!Objects.equals(answer3, question.answer3)) return false;
        if (!Objects.equals(answer4, question.answer4)) return false;
        if (!Objects.equals(correctAnswer, question.correctAnswer)) return false;
        return true;

    }

    @Override
    public int hashCode () {
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + (answer1 != null ? answer1.hashCode() : 0);
        result = 31 * result + (answer2 != null ? answer2.hashCode() : 0);
        result = 31 * result + (answer3 != null ? answer3.hashCode() : 0);
        result = 31 * result + (answer4 != null ? answer4.hashCode() : 0);
        result = 31 * result + (correctAnswer != null ? correctAnswer.hashCode() : 0);
        result = 31 * result + (difficultLevel != 0 ? difficultLevel : 0);

        return result;
    }

    public Boolean checkCorrect ( String answer ) {
        if (answer.equals(this.correctAnswer))
            return true;
        return false;
    }


    public boolean addAnswer ( String answerToAdd ) {

        return true;
    }
}


