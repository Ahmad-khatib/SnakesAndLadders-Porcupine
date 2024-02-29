package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Question {

    private int questionId;
    private String text;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String correctAnswer;
    private Difficulty level;
    private List<Question> questionList;

    public Question(String text, String answer1, String answer2, String answer3, String answer4, String correctAnswer, Difficulty level) {
        this.questionId = questionId;
        this.text = text;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.correctAnswer = correctAnswer;
        this.level = level;
        this.questionList = new ArrayList<>();
    }
    public Question() {
        this.questionList = new ArrayList<>();
    }

    // Getters and Setters for all fields
    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
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

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }
    @Override
    public String toString() {
        return text;
    }

    private final List<QuestionObserver> observers = new ArrayList<>();


    public void registerObserver(QuestionObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers(Question question) {
        for (QuestionObserver observer : observers) {
            observer.onQuestionAdded(question);
        }
    }

    private void notifyObserversForEdit(Question oldQuestion, Question newQuestion) {
        for (QuestionObserver observer : observers) {
            observer.onQuestionEdited(oldQuestion, newQuestion);
        }
    }

    private void notifyObserversForDelete(Question deletedQuestion) {
        for (QuestionObserver observer : observers) {
            observer.onQuestionDeleted(deletedQuestion);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;

        if (!Objects.equals(text, question.text)) return false;
        if (!Objects.equals(answer1, question.answer1)) return false;
        if (!Objects.equals(answer2, question.answer2)) return false;
        if (!Objects.equals(answer3, question.answer3)) return false;
        if (!Objects.equals(answer4, question.answer4)) return false;
        return Objects.equals(correctAnswer, question.correctAnswer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, answer1, answer2, answer3, answer4, correctAnswer);
    }

    public Boolean checkCorrect(String answer) {
        return answer.equals(this.correctAnswer);
    }

    private static int lastGeneratedId = 0;

    public static int generateUniqueId() {
        return ++lastGeneratedId;
    }

    // function to add questions
    public void addQuestion(Question newQuestion) {
        int uniqueId = generateUniqueId();
        newQuestion.setQuestionId(uniqueId);
        questionList.add(newQuestion);
        notifyObservers(newQuestion); // Notify observers that a new question has been added
    }


    public Question findQuestionById(int questionId) {
        for (Question question : questionList) {
            if (question.getQuestionId() == questionId) {
                return question;
            }
        }
        return null;
    }


    // function to edit question
    public void editQuestion(int questionId, Question editedQuestion) {
        Question existingQuestion = findQuestionById(questionId);
        if (existingQuestion != null) {
            // Create a copy of the existing question
            Question oldQuestion = new Question();
            oldQuestion.setQuestionId(existingQuestion.getQuestionId());
            oldQuestion.setText(existingQuestion.getText());
            oldQuestion.setAnswer1(existingQuestion.getAnswer1());
            oldQuestion.setAnswer2(existingQuestion.getAnswer2());
            oldQuestion.setAnswer3(existingQuestion.getAnswer3());
            oldQuestion.setAnswer4(existingQuestion.getAnswer4());
            oldQuestion.setCorrectAnswer(existingQuestion.getCorrectAnswer());
            oldQuestion.setLevel(existingQuestion.getLevel());

            existingQuestion.setText(editedQuestion.getText());
            existingQuestion.setAnswer1(editedQuestion.getAnswer1());
            existingQuestion.setAnswer2(editedQuestion.getAnswer2());
            existingQuestion.setAnswer3(editedQuestion.getAnswer3());
            existingQuestion.setAnswer4(editedQuestion.getAnswer4());
            existingQuestion.setCorrectAnswer(editedQuestion.getCorrectAnswer());
            existingQuestion.setLevel(editedQuestion.getLevel());

            notifyObserversForEdit(oldQuestion, existingQuestion); // Notify observers that a question has been edited
        } else {
            throw new IllegalArgumentException("No question found with this questionId.");
        }
    }


    // function to delete question
    public void deleteQuestion(int questionId) {
        Question deletedQuestion = findQuestionById(questionId);
        if (deletedQuestion != null) {
            questionList.remove(deletedQuestion);
            notifyObserversForDelete(deletedQuestion); // Notify observers that a question has been deleted
        } else {
            throw new IllegalArgumentException("No question found with this questionId.");
        }
    }

    public static String getQuestion(Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return "Easy question";
            case MEDIUM:
                return "Medium question";
            case HARD:
                return "Hard question";
            default:
                throw new IllegalArgumentException("Invalid difficulty");
        }
    }


}
