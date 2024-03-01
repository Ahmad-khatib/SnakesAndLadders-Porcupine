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
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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


    private SystemData systemData; // Reference to the SystemData instance

    private ObservableList<Question> questions = FXCollections.observableArrayList();

    // Method to set the SystemData instance
    public void setSystemData(SystemData systemData) {
        this.systemData = systemData;
    }

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

        // Populate the questions list with the retrieved questions
        questions.addAll(allQuestions);

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
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Question");
        dialog.setHeaderText("Enter the new question:");
        dialog.setContentText("Question:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(question -> {
            // Add the question to the list
            questionListView.getItems().add(question);
            // Save the updated list to the JSON file
            SystemData.getInstance().saveQuestions();
        });
    }

    @FXML
    private void editQuestion() {
        ListView<String> listView = questionListView;
        int selectedIndex = listView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedQuestion = listView.getSelectionModel().getSelectedItem();

            TextInputDialog dialog = new TextInputDialog(selectedQuestion);
            dialog.setTitle("Edit Question");
            dialog.setHeaderText(null);
            dialog.setContentText("Question:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(question -> {
                // Update the question in the list
                listView.getItems().set(selectedIndex, question);
                // Save the updated list to the JSON file
                SystemData.getInstance().saveQuestions();
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Question Selected");
            alert.setContentText("Please select a question to edit.");
            alert.showAndWait();
        }
    }

    @FXML
    private void deleteQuestion() {
        ListView<String> listView = questionListView;
        int selectedIndex = listView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Question");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this question?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Remove the selected question from the list
                listView.getItems().remove(selectedIndex);
                // Save the updated list to the JSON file
                SystemData.getInstance().saveQuestions();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Question Selected");
            alert.setContentText("Please select a question to delete.");
            alert.showAndWait();
        }
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

