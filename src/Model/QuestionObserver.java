package Model;

public interface QuestionObserver {
    void onQuestionAdded(Question question);
    void onQuestionEdited(Question oldQuestion, Question newQuestion);
    void onQuestionDeleted(Question deletedQuestion);
}