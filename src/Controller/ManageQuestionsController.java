package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class ManageQuestionsController {
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
    private ObservableList<String> questions = FXCollections.observableArrayList();

    @FXML
    private void initialize() {

            // Load questions from JSON file and populate the ListView
            loadQuestionsFromJSONFile();
            questionListView.setItems(questions);
            System.out.println("Initialized with " + questions.size() + " questions.");


    }
    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("View/MainPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void addQuestion() {
        // Add functionality to add a new question
        System.out.println("Add question button clicked!");
    }

    @FXML
    private void editQuestion() {
        // Add functionality to edit the selected question
        String selectedQuestion = questionListView.getSelectionModel().getSelectedItem();
        if (selectedQuestion != null) {
            System.out.println("Edit question button clicked for: " + selectedQuestion);
        }
    }

    @FXML
    private void deleteQuestion() {
        // Add functionality to delete the selected question
        String selectedQuestion = questionListView.getSelectionModel().getSelectedItem();
        if (selectedQuestion != null) {
            System.out.println("Delete question button clicked for: " + selectedQuestion);
            questions.remove(selectedQuestion);
        }
    }

    private void loadQuestionsFromJSONFile() {
        // Read questions from JSON file and populate the questions list
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("C:\\Users\\noura\\Documents\\GitHub\\SnakesAndLadders-Porcupine\\src\\Controller\\questions_scheme.json"));
            JSONObject jsonObject = (JSONObject) obj; // Cast to JSONObject
            JSONArray jsonArray = (JSONArray) jsonObject.get("questions");
            int questionNumber = 1;
            for (Object o : jsonArray) {
                JSONObject questionObject = (JSONObject) o;
                String question = (String) questionObject.get("question");
                String level = (String) questionObject.get("difficulty");
                JSONArray answersArray = (JSONArray) questionObject.get("answers");
                String[] answers = new String[answersArray.size()];
                for (int i = 0; i < answersArray.size(); i++) {
                    answers[i] = (String) answersArray.get(i);
                }
                String correctAnswer = (String) questionObject.get("correct_ans");
                String formattedQuestion = String.format("%d. %s%nLevel: %s%nAnswers: %s%nCorrect Answer: %s",
                        questionNumber, question, level, Arrays.toString(answers), correctAnswer);
                questions.add(formattedQuestion);
                questionNumber++;
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }



}
