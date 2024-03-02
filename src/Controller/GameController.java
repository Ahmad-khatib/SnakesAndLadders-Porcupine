package Controller;

import Model.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    @FXML
    private ImageView diceImage;
    @FXML
    private ImageView diceImage2;
    @FXML
    private static Button rollButton;

    public static void Start(Game game, GridPane dynamicGridPane) {
        ArrayList<Player> players = game.getPlayers();
        GameBoard gameBoard = game.getGameBoard();
        HashMap<Difficulty, ArrayList<Question>> questions = game.getQuestions();
        int currentPosition = 1;
        int i = 0;
        while (i < players.size()) {
            players.get(i).getIcon().setFitWidth(gameBoard.getCellWidth() / 2);
            players.get(i).getIcon().setFitHeight(gameBoard.getCellWidth() / 2);
            dynamicGridPane.add(players.get(i).getIcon(), 0, gameBoard.getSize() - 1);
            i++;
        }

        while (!(game.isGameFinished())) {
            for (i = 0; i < players.size(); i++) {
                currentPosition = players.get(i).getPlayerPosition();
                int rollResult = 1;
                players.get(i).movePlayerTo(currentPosition + rollResult);
                // Simulate some game logic to determine whether the game is finished
                // You need to replace this with your actual game logic

                    game.setGameFinished(true);

            }
        }
    }


    @FXML
    public void roll() {
        rollButton.setDisable(true);

        Task<Integer> task = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                int totalSum = 0;

                int dice1Value = 0;
                int dice2Value = 0;
                for (int i = 0; i < 20; i++) {
                    Random random = new Random();
                    dice1Value = (random.nextInt(5) + 1);
                    dice2Value = (random.nextInt(5) + 1);

                    File file = new File("src/View/photos/dice/dice" + dice1Value + ".png");
                    File file2 = new File("src/View/photos/dice/dice" + dice2Value + ".png");
                    Image image1 = new Image(file.toURI().toString());
                    Image image2 = new Image(file2.toURI().toString());

                    Platform.runLater(() -> {
                        diceImage.setImage(image1);
                        diceImage2.setImage(image2);
                    });

                    Thread.sleep(50);
                }

                totalSum = dice1Value + dice2Value;

                Platform.runLater(() -> rollButton.setDisable(false));

                return totalSum;
            }
        };

        task.setOnSucceeded(event -> {
            int result = task.getValue();
            // Now you can handle the result as needed, for example, update UI
            System.out.println("Total Sum: " + result);
        });

        new Thread(task).start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }



}
