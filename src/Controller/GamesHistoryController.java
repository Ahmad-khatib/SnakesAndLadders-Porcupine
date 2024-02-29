package Controller;

import Model.SystemData;
import Model.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class GamesHistoryController {

    @FXML
    private TableColumn<Player, String> avatarHistory;

    @FXML
    private Button backButton;

    @FXML
    private TableView<Player> historyTable;

    @FXML
    private TableColumn<Player, Integer> scoreHistory;

    @FXML
    private TableColumn<Player, String> userNameHistory;

    private ArrayList<Player> Players;

    @FXML
    void back(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("src/View/GamesHistory.fxml")));
            Scene customerScene = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(customerScene);
            window.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("FXML");
            alert.setHeaderText("Load failure");
            alert.setContentText("Failed to load the FXML file.");
            alert.showAndWait();
        }
    }

    public void initialize(URL location, ResourceBundle resources) {
        userNameHistory.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        avatarHistory.setCellValueFactory(new PropertyValueFactory<>("Icon"));
        scoreHistory.setCellValueFactory(new PropertyValueFactory<>("GameHighScore"));
        setHistoryTable();
    }

    private void setHistoryTable() {
        Players = SystemData.getInstance().getPlayers();
        ObservableList<Player> qs = FXCollections.observableArrayList(Players);
        historyTable.setItems(qs);
        historyTable.refresh();
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

}