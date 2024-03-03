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
import java.util.List;

public class ManageQuestionsController implements QuestionObserver {

    @FXML
    private Label totalQuestionsLabel;
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

        List<Question> allQuestions = systemData.getAllQuestions();
        ObservableList<String> questionTexts = FXCollections.observableArrayList();
        for (Question question : allQuestions) {
            StringBuilder questionWithAnswers = new StringBuilder();
            questionWithAnswers.append("Question: ").append(question.getText()).append("\n");
            questionWithAnswers.append("Answers:\n");
            questionWithAnswers.append("1. ").append(question.getAnswer1()).append("\n");
            questionWithAnswers.append("2. ").append(question.getAnswer2()).append("\n");
            questionWithAnswers.append("3. ").append(question.getAnswer3()).append("\n");
            questionWithAnswers.append("4. ").append(question.getAnswer4()).append("\n");
            questionWithAnswers.append("Correct Answer: ").append(question.getCorrectAnswer()).append("\n"); // Add correct answer information
            questionWithAnswers.append("Level: ").append(question.getLevel()).append("\n"); // Add level information
            questionTexts.add(questionWithAnswers.toString());
        }

        // Clear the existing items in the ListView
        questionListView.getItems().clear();

        questionListView.setItems(questionTexts);
        refreshQuestionList();
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
        try {
            // Load the FXML file for adding a question
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddQuestion.fxml"));
            Parent root = loader.load();

            // Create a new stage for adding a question
            Stage stage = new Stage();
            stage.setTitle("Add Question");
            stage.setScene(new Scene(root));

            // Get the controller for adding a question
            AddQuestionController addQuestionController = loader.getController();

            // Show the stage and wait for it to be closed
            stage.showAndWait();

            // After the stage is closed, check if a question was added
            if (addQuestionController.isQuestionAdded()) {
                // If a question was added, add it to the UI immediately
                Question newQuestion = addQuestionController.getNewQuestion();
                questions.add(newQuestion);
                questionListView.getItems().add(newQuestion.getText());
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Conformation");
                alert.setContentText("Question was saved successfully");
                alert.showAndWait();
                refreshQuestionList();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void editQuestion() {
        // Get the selected question text
        String selectedQuestionText = questionListView.getSelectionModel().getSelectedItem();


        // Check if a question is selected
        if (selectedQuestionText == null) {
            // If no question is selected, display an error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Question Selected");
            alert.setContentText("Please select a question to edit.");
            alert.showAndWait();
            return;
        }

        // Find the corresponding question object
        SystemData systemData = SystemData.getInstance();
        List<Question> allQuestions = systemData.getAllQuestions();
        Question selectedQuestion = null;
        for (Question question : allQuestions) {
            StringBuilder questionWithAnswers = new StringBuilder();
            questionWithAnswers.append("Question: ").append(question.getText()).append("\n");
            questionWithAnswers.append("Answers:\n");
            questionWithAnswers.append("1. ").append(question.getAnswer1()).append("\n");
            questionWithAnswers.append("2. ").append(question.getAnswer2()).append("\n");
            questionWithAnswers.append("3. ").append(question.getAnswer3()).append("\n");
            questionWithAnswers.append("4. ").append(question.getAnswer4()).append("\n");
            questionWithAnswers.append("Correct Answer: ").append(question.getCorrectAnswer()).append("\n"); // Add correct answer information
            questionWithAnswers.append("Level: ").append(question.getLevel()).append("\n"); // Add level information
            if (questionWithAnswers.toString().equals(selectedQuestionText)) {
                selectedQuestion = question;
                break;
            }
        }

        // If the selected question is found, open the edit question dialog
        if (selectedQuestion != null) {
            try {
                // Load the FXML file for editing a question
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/EditQuestion.fxml"));
                Parent root = loader.load();

                // Pass the selected question to the controller for editing
                EditQuestionController editQuestionController = loader.getController();
                editQuestionController.setQuestion(selectedQuestion);

                // Create a new stage for editing a question
                Stage stage = new Stage();
                stage.setTitle("Edit Question");
                stage.setScene(new Scene(root));

                // Show the stage and wait for it to be closed
                stage.showAndWait();

                // If the question was edited, refresh the question list
                if (editQuestionController.isQuestionEdited()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Conformation");
                    alert.setContentText("Question was edited successfully");
                    alert.showAndWait();
                    refreshQuestionList();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Selected question not found: " + selectedQuestionText);
        }
    }



    @FXML
    private void deleteQuestion() {

        String selectedQuestionText = questionListView.getSelectionModel().getSelectedItem();

        // Check if a question is selected
        if (selectedQuestionText == null) {
            // If no question is selected, display an error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Question Selected");
            alert.setContentText("Please select a question to delete.");
            alert.showAndWait();
            return;
        }

        System.out.println("Selected question: " + selectedQuestionText);

        // Create a confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Delete Question");
        alert.setContentText("Are you sure you want to delete the selected question?");

        // Customize button labels
        ButtonType buttonYes = new ButtonType("Yes");
        ButtonType buttonNo = new ButtonType("No");

        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        // Show the confirmation dialog and wait for user response
        alert.showAndWait().ifPresent(response -> {
            if (response == buttonYes) {
                SystemData systemData = SystemData.getInstance();
                List<Question> allQuestions = systemData.getAllQuestions();

                for (Question question : allQuestions) {
                    StringBuilder questionWithAnswers = new StringBuilder();
                    questionWithAnswers.append("Question: ").append(question.getText()).append("\n");
                    questionWithAnswers.append("Answers:\n");
                    questionWithAnswers.append("1. ").append(question.getAnswer1()).append("\n");
                    questionWithAnswers.append("2. ").append(question.getAnswer2()).append("\n");
                    questionWithAnswers.append("3. ").append(question.getAnswer3()).append("\n");
                    questionWithAnswers.append("4. ").append(question.getAnswer4()).append("\n");
                    questionWithAnswers.append("Correct Answer: ").append(question.getCorrectAnswer()).append("\n"); // Add correct answer information
                    questionWithAnswers.append("Level: ").append(question.getLevel()).append("\n"); // Add level information
                    if (questionWithAnswers.toString().equals(selectedQuestionText)) {
                        // Remove the question from the data model
                        systemData.deleteQuestion(question);
                        // Notify the observer (SystemData) that a question is deleted
                        systemData.onQuestionDeleted(question);
                        // Remove the question from the UI
                        questions.remove(question);
                        questionListView.getItems().remove(selectedQuestionText);
                        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                        alert1.setTitle("Conformation");
                        alert1.setContentText("Question was deleted successfully");
                        alert1.showAndWait();
                        refreshQuestionList();
                        // Print confirmation
                        System.out.println("Question deleted: " + selectedQuestionText);
                        return;
                    }
                }

                System.out.println("Selected question not found: " + selectedQuestionText);
            }
        });
    }



    @FXML
    private void sortByLevel() {
        SystemData systemData = SystemData.getInstance();
        List<Question> allQuestions = systemData.getAllQuestions();
        allQuestions.sort((q1, q2) -> Integer.compare(q2.getLevel().ordinal(), q1.getLevel().ordinal()));

        ObservableList<String> questionTexts = FXCollections.observableArrayList();
        for (Question question : allQuestions) {
            StringBuilder questionWithAnswers = new StringBuilder();

            questionWithAnswers.append("Question: ").append(question.getText()).append("\n");
            questionWithAnswers.append("Answers:\n");
            questionWithAnswers.append("1. ").append(question.getAnswer1()).append("\n");
            questionWithAnswers.append("2. ").append(question.getAnswer2()).append("\n");
            questionWithAnswers.append("3. ").append(question.getAnswer3()).append("\n");
            questionWithAnswers.append("4. ").append(question.getAnswer4()).append("\n");
            questionWithAnswers.append("Correct Answer: ").append(question.getCorrectAnswer()).append("\n"); // Add correct answer information
            questionWithAnswers.append("Level: ").append(question.getLevel()).append("\n"); // Add level information
            questionTexts.add(questionWithAnswers.toString());
        }

        questionListView.setItems(questionTexts);
    }

    @Override
    public void onQuestionAdded(Question question) {
        questions.add(question);
        questionListView.getItems().add(question.getText());
    }

    @Override
    public void onQuestionEdited(Question oldQuestion, Question newQuestion) {
        int index = questions.indexOf(oldQuestion);
        if (index != -1) {
            questions.set(index, newQuestion);
            questionListView.getItems().set(index, newQuestion.getText());
        }
    }

    @Override
    public void onQuestionDeleted(Question deletedQuestion) {
        questions.remove(deletedQuestion);
        questionListView.getItems().remove(deletedQuestion.getText());
    }
    private void refreshQuestionList() {
        // Reload questions from SystemData
        SystemData systemData = SystemData.getInstance();
        boolean success = systemData.loadQuestions();
        if (!success) {
            System.out.println("Failed to load questions from JSON file.");
            return;
        }


        List<Question> allQuestions = systemData.getAllQuestions();

        // Update the UI with the refreshed question list
        ObservableList<String> questionTexts = FXCollections.observableArrayList();
        for (Question question : allQuestions) {
            StringBuilder questionWithAnswers = new StringBuilder();

            questionWithAnswers.append("Question: ").append(question.getText()).append("\n");
            questionWithAnswers.append("Answers:\n");
            questionWithAnswers.append("1. ").append(question.getAnswer1()).append("\n");
            questionWithAnswers.append("2. ").append(question.getAnswer2()).append("\n");
            questionWithAnswers.append("3. ").append(question.getAnswer3()).append("\n");
            questionWithAnswers.append("4. ").append(question.getAnswer4()).append("\n");
            questionWithAnswers.append("Correct Answer: ").append(question.getCorrectAnswer()).append("\n"); // Add correct answer information
            questionWithAnswers.append("Level: ").append(question.getLevel()).append("\n"); // Add level information
            questionTexts.add(questionWithAnswers.toString());
        }

        // Clear the existing items in the ListView
        questionListView.getItems().clear();

        // Add the refreshed question list to the ListView
        questionListView.setItems(questionTexts);
        totalQuestionsLabel.setText("Total Questions: " + allQuestions.size());

    }

}
