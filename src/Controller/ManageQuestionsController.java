package Controller;

import Model.Question;
import Model.QuestionObserver;
import Model.SystemData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ManageQuestionsController implements QuestionObserver {
    @FXML
    private ListView<String> questionListView;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button backButton;

    @FXML
    private Button sortButton;

    private ObservableList<Question> questions = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        System.out.println("ManageQuestionsController initialized.");

        SystemData systemData = SystemData.getInstance();
        boolean success = systemData.loadQuestions();
        if (!success) {
            System.out.println("Failed to load questions from JSON file.");
            return;
        }

        List<Question> allQuestions = systemData.getAllQuestionsSortedById(); // Sort by ID
        ObservableList<String> questionTexts = FXCollections.observableArrayList();
        for (Question question : allQuestions) {
            questionTexts.add(formatQuestionText(question));
        }

        questionListView.setItems(questionTexts);
        System.out.println("Initialized with " + allQuestions.size() + " questions.");

        for (Question question : allQuestions) {
            question.registerObserver(this);
        }
    }

    private String formatQuestionText(Question question) {
        StringBuilder questionWithAnswers = new StringBuilder();
        questionWithAnswers.append(question.getText()).append("\n");
        questionWithAnswers.append("Answers:\n");
        questionWithAnswers.append("1. ").append(question.getAnswer1()).append("\n");
        questionWithAnswers.append("2. ").append(question.getAnswer2()).append("\n");
        questionWithAnswers.append("3. ").append(question.getAnswer3()).append("\n");
        questionWithAnswers.append("4. ").append(question.getAnswer4()).append("\n");
        questionWithAnswers.append("Correct Answer: ").append(question.getCorrectAnswer()).append("\n"); // Add correct answer information
        questionWithAnswers.append("Level: ").append(question.getLevel()).append("\n"); // Add level information
        return questionWithAnswers.toString();
    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/MainPage.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) backButton.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addQuestion() {
        System.out.println("add question button clicked!");
    }

    @FXML
    private void editQuestion() {
        System.out.println("Edit question button clicked!");
    }

    @FXML
    private void deleteQuestion() {
        String selectedQuestionText = questionListView.getSelectionModel().getSelectedItem();
        System.out.println("Selected question: " + selectedQuestionText);

        SystemData systemData = SystemData.getInstance();
        List<Question> allQuestions = systemData.getAllQuestions();

        for (Question question : allQuestions) {
            if (formatQuestionText(question).equals(selectedQuestionText)) {
                // Remove the question from the data model
                systemData.deleteQuestion(question);
                // Remove the question from the UI
                questionListView.getItems().remove(selectedQuestionText);
                // Print confirmation
                System.out.println("Question deleted: " + selectedQuestionText);
                return;
            }
        }

        System.out.println("Selected question not found: " + selectedQuestionText);
    }

    @FXML
    private void sortByLevel() {
        SystemData systemData = SystemData.getInstance();
        List<Question> allQuestions = systemData.getAllQuestions();
        allQuestions.sort((q1, q2) -> Integer.compare(q2.getLevel().ordinal(), q1.getLevel().ordinal()));

        ObservableList<String> questionTexts = FXCollections.observableArrayList();
        for (Question question : allQuestions) {
            questionTexts.add(formatQuestionText(question));
        }

        questionListView.setItems(questionTexts);
    }

    @Override
    public void onQuestionAdded(Question question) {
        questions.add(question);
        questionListView.getItems().add(formatQuestionText(question));
    }

    @Override
    public void onQuestionEdited(Question oldQuestion, Question newQuestion) {
        int index = questions.indexOf(oldQuestion);
        if (index != -1) {
            questions.set(index, newQuestion);
            questionListView.getItems().set(index, formatQuestionText(newQuestion));
        }
    }

    @Override
    public void onQuestionDeleted(Question deletedQuestion) {
        questions.remove(deletedQuestion);
        questionListView.getItems().remove(formatQuestionText(deletedQuestion));
    }
}
