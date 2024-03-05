package  Controller;

import Model.Game;
import Model.SystemData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GamesHistoryController implements Initializable {
    @FXML
    private Button backButton;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<Game> historyTable;

    @FXML
    private TableColumn<Game, String> playerNameColumn;

    @FXML
    private TableColumn<Game, String> durationColumn;

    @FXML
    private TableColumn<Game, String> levelColumn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        playerNameColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        levelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));

        // Load games history from CSV file
        ArrayList<Game> gamesHistory = SystemData.loadGamesHistoryFromCsv("src/Model/GamesHistory.csv");

        // Add loaded games to the table
        historyTable.getItems().addAll(gamesHistory);
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


}