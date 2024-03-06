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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


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
        playerNameColumn.setCellValueFactory(new PropertyValueFactory<>("WINNERNAME"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("GAMETIME"));
        levelColumn.setCellValueFactory(new PropertyValueFactory<>("GAMELEVEL"));


        // Load games history from CSV file
        ArrayList<Game> gamesHistory = null;
            gamesHistory = SystemData.loadGamesHistoryFromJson("src/Model/History.json");

        // Convert each Game object to a formatted string and add to the table
        ObservableList<Game> observableGames = FXCollections.observableArrayList(gamesHistory);
        historyTable.setItems(observableGames);
    }


    private String gameToString(Game game) {
        // Format the Game object as a string
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Player: ").append(game.getWINNERNAME()).append("\n");
        stringBuilder.append("Duration: ").append(game.getGAMETIME()).append("\n");
        stringBuilder.append("Level: ").append(game.getGAMELEVEL()).append("\n\n");
        return stringBuilder.toString();
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
