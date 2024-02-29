package Controller;

import Model.Question;
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

public class ManageQuestionsController {
    @FXML
    private ListView<Question> questionListView;


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

    private final ObservableList<Question> questions = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        System.out.println("ManageQuestionsController initialized.");


        // No need to convert Question objects to strings

        questionListView.setItems(questions);
        System.out.println("Initialized with " + questions.size() + " questions.");
    }


    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/MainPage.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) backButton.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Main Page");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void addQuestion() {
        System.out.println("Add question button clicked!");
    }

    @FXML
    private void editQuestion() {

    }

    @FXML
    private void deleteQuestion() {

    }

    @FXML
    private void sortByLevel() {

    }


}
