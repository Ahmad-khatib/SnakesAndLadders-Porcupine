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
import java.util.Comparator;
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

        // Load questions from the JSON file using SystemData
        SystemData systemData = SystemData.getInstance();
        boolean success = systemData.loadQuestions();
        if (!success) {
            System.out.println("Failed to load questions from JSON file.");
            return;
        }

        // Get all loaded questions sorted by ID
        List<Question> allQuestions = systemData.getAllQuestionsSortedById();

        // Convert Question objects to strings with answers
        ObservableList<String> questionTexts = FXCollections.observableArrayList();
        for (Question question : allQuestions) {
            StringBuilder questionWithAnswers = new StringBuilder();
            questionWithAnswers.append(question.getText()).append("\n");
            questionWithAnswers.append("Answers:\n");
            questionWithAnswers.append("1. ").append(question.getAnswer1()).append("\n");
            questionWithAnswers.append("2. ").append(question.getAnswer2()).append("\n");
            questionWithAnswers.append("3. ").append(question.getAnswer3()).append("\n");
            questionWithAnswers.append("4. ").append(question.getAnswer4()).append("\n");
            questionTexts.add(questionWithAnswers.toString());
        }

        questionListView.setItems(questionTexts);
        System.out.println("Initialized with " + allQuestions.size() + " questions.");

        // Register this controller as an observer of questions
        for (Question question : allQuestions) {
            question.registerObserver(this);
        }
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
        // Get the selected question from the list view
        String selectedQuestionText = questionListView.getSelectionModel().getSelectedItem();
        System.out.println("Selected question: " + selectedQuestionText);

        // Print all question texts for comparison
        for (Question question : questions) {
            System.out.println("Question in list: " + question.getText());
        }

        // Find the corresponding Question object
        for (Question question : questions) {
            if (question.getText().equals(selectedQuestionText)) {
                // Delete the question using the deleteQuestion method from the Question class
                int questionId = question.getQuestionId();
                question.deleteQuestion(questionId); // Pass the question ID

                // Update the UI to reflect the deletion
                questions.remove(question);
                questionListView.getItems().remove(selectedQuestionText);

                System.out.println("Question deleted: " + selectedQuestionText);
                return; // Exit the loop after deleting the question
            }
        }

        // If the selected question is not found
        System.out.println("Selected question not found: " + selectedQuestionText);
    }




    @FXML
    private void sortByLevel() {
        SystemData systemData = SystemData.getInstance();
        List<Question> allQuestions = systemData.getAllQuestions();

        // Sort questions by difficulty level
        allQuestions.sort(Comparator.comparing(Question::getLevel));

        // Update UI with sorted questions
        ObservableList<String> questionTexts = FXCollections.observableArrayList();
        for (Question question : allQuestions) {
            StringBuilder questionWithAnswers = new StringBuilder();
            questionWithAnswers.append(question.getText()).append("\n");
            questionWithAnswers.append("Answers:\n");
            questionWithAnswers.append("1. ").append(question.getAnswer1()).append("\n");
            questionWithAnswers.append("2. ").append(question.getAnswer2()).append("\n");
            questionWithAnswers.append("3. ").append(question.getAnswer3()).append("\n");
            questionWithAnswers.append("4. ").append(question.getAnswer4()).append("\n");
            questionTexts.add(questionWithAnswers.toString());
        }

        questionListView.setItems(questionTexts);
    }


    // Observer methods
    @Override
    public void onQuestionAdded(Question question) {
        // Add the new question to the list and update the UI
        questions.add(question);
        questionListView.getItems().add(question.getText());
    }

    @Override
    public void onQuestionEdited(Question oldQuestion, Question newQuestion) {
        // Find the old question in the list, replace it with the new question, and update the UI
        int index = questions.indexOf(oldQuestion);
        if (index != -1) {
            questions.set(index, newQuestion);
            questionListView.getItems().set(index, newQuestion.getText());
        }
    }

    @Override
    public void onQuestionDeleted(Question deletedQuestion) {
        // Remove the deleted question from the list and update the UI
        questions.remove(deletedQuestion);
        questionListView.getItems().remove(deletedQuestion.getText());
    }
}
