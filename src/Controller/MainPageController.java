package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainPageController {


    @FXML
    private Button ManageQuestionsButton;
    @FXML Button ViewHistoryButton;



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
