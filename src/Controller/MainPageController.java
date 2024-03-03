package Controller;

import Model.Difficulty;
import Model.Question;
import Model.SystemData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainPageController {

    @FXML
    private Button startGameButton;
    @FXML
    private Button ManageQuestionsButton;
    @FXML Button ViewHistoryButton;

    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorMessage;


    @FXML
    private void initialize() {

    }



    @FXML
    private void startGame(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/PlayerSelections.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Get the current stage from the event's source (the start game button)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.setTitle("Player Selections");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void manageQuestions() {
        // Add functionality to navigate to the ManageQuestions.fxml screen
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ManageQuestions.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ManageQuestionsButton.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setTitle("Manage Questions");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void viewHistory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/GamesHistory.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage)  ViewHistoryButton.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setTitle("Games History");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
