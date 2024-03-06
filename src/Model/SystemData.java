package Model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import javafx.scene.control.Alert;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

public class SystemData implements QuestionObserver {
    private static SystemData instance;
    private final HashMap<Difficulty, ArrayList<Question>> questions;
    private static ArrayList<Game> GamesHistory = new ArrayList<>();

    private SystemData() {
        questions = new HashMap<>();
    }

    public static SystemData getInstance() {
        if (instance == null)
            instance = new SystemData();
        return instance;
    }

    public boolean loadQuestions() {
        JSONParser parser = new JSONParser();
        questions.clear(); // Clear existing questions

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("src/Model/questions_scheme.json")))) {
            Object obj = parser.parse(reader);
            JSONObject jo = (JSONObject) obj;
            JSONArray questionsArray = (JSONArray) jo.get("questions");

            for (Object questionObj : questionsArray) {
                JSONObject q = (JSONObject) questionObj;
                String text = (String) q.get("question");
                JSONArray answersArray = (JSONArray) q.get("answers");
                String answer1 = (String) answersArray.get(0);
                String answer2 = (String) answersArray.get(1);
                String answer3 = (String) answersArray.get(2);
                String answer4 = (String) answersArray.get(3);
                String correctAnswer = (String) q.get("correct_ans");
                int difficulty = Integer.parseInt(q.get("difficulty").toString());

                Difficulty enumDifficulty = getQuestionDifficulty(difficulty);



                Question questionToAdd = new Question( text, answer1, answer2, answer3, answer4, correctAnswer, enumDifficulty);
                questions.computeIfAbsent(enumDifficulty, k -> new ArrayList<>()).add(questionToAdd);

            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addGameToHistory(Game game) {
        GamesHistory.add(game);
        saveGamesHistoryToJson("src/Model/History.json",game);
    }


    public void saveGamesHistoryToJson(String filename, Game newGame) {
        try {
            // Load existing games from the JSON file
            ArrayList<Game> existingGames = loadGamesHistoryFromJson(filename);

            // Add the new game to the existing list
            existingGames.add(newGame);

            // Convert the list of games to a JSON array
            JSONArray gamesArray = new JSONArray();
            for (Game game : existingGames) {
                JSONObject gameObj = new JSONObject();
                gameObj.put("winnerName", game.getWINNERNAME());
                gameObj.put("duration", game.getGAMETIME());
                gameObj.put("level", game.getGAMELEVEL());
                gamesArray.add(gameObj);
            }

            // Write the JSON array to the file
            try (FileWriter fileWriter = new FileWriter(filename)) {
                fileWriter.write(gamesArray.toJSONString());
                System.out.println("Games history saved to " + filename);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Difficulty getQuestionDifficulty(int difficulty) {
        switch (difficulty) {
            case 1:
                return Difficulty.EASY;
            case 2:
                return Difficulty.MEDIUM;
            case 3:
                return Difficulty.HARD;
            default:
                throw new IllegalArgumentException("Invalid difficulty level: " + difficulty);
        }
    }

    public void saveQuestions() {
        try {
            JSONArray JSONQuestions = new JSONArray();

            for (ArrayList<Question> list : questions.values()) {
                if (list == null)
                    continue;

                for (Question q : list) {
                    JSONObject questionObj = new JSONObject();
                    questionObj.put("question", q.getText());

                    JSONArray answersArray = new JSONArray();
                    answersArray.add(q.getAnswer1());
                    answersArray.add(q.getAnswer2());
                    answersArray.add(q.getAnswer3());
                    answersArray.add(q.getAnswer4());

                    questionObj.put("answers", answersArray);
                    questionObj.put("correct_ans", q.getCorrectAnswer());
                    questionObj.put("difficulty", String.valueOf(q.getLevel().ordinal() + 1));

                    JSONQuestions.add(questionObj);
                }
            }

            JSONObject toWrite = new JSONObject();
            toWrite.put("questions", JSONQuestions);

            // Specify the full path to the JSON file
            FileWriter file = new FileWriter("src/Model/questions_scheme.json");
            file.write(toWrite.toJSONString());
            file.flush();
            System.out.println("JSON Question was saved successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<Difficulty, ArrayList<Question>> getQuestions() {

        return questions;
    }

    public List<Question> getAllQuestions() {
        List<Question> allQuestions = new ArrayList<>();
        for (ArrayList<Question> list : questions.values()) {
            if (list != null) {
                allQuestions.addAll(list);
            }
        }
        return allQuestions;
    }



    public void deleteQuestion(Question question) {
        for (ArrayList<Question> list : questions.values()) {
            if (list != null) {
                list.removeIf(q -> q.equals(question));
            }
        }
        saveQuestions(); // Save changes after deleting a question
    }

    @Override
    public void onQuestionAdded(Question question) {
        // Not used in this context
    }

    @Override
    public void onQuestionEdited(Question oldQuestion, Question newQuestion) {
        // Not used in this context
    }

    @Override
    public void onQuestionDeleted(Question deletedQuestion) {
        // Implement logic to handle deletion of questions
        // Remove the deleted question from the HashMap and save the changes to the JSON file
        deleteQuestion(deletedQuestion);
    }

    public boolean addQuestion(Question newQuestion) {
        // Check if the new question's text already exists in any of the existing questions
        for (ArrayList<Question> questionList : questions.values()) {
            if (questionList != null) {
                for (Question existingQuestion : questionList) {
                    // Check if the question text is the same
                    if (existingQuestion.getText().equals(newQuestion.getText())) {
                        // If the question text already exists, show an alert and return false
                        showAlert("Question Already Exists", "This question already exists. Please try another one.");
                        return false;
                    }
                }
            }
        }

        // Check if any of the answers are the same
        if (newQuestion.getAnswer1().equals(newQuestion.getAnswer2()) ||
                newQuestion.getAnswer1().equals(newQuestion.getAnswer3()) ||
                newQuestion.getAnswer1().equals(newQuestion.getAnswer4()) ||
                newQuestion.getAnswer2().equals(newQuestion.getAnswer3()) ||
                newQuestion.getAnswer2().equals(newQuestion.getAnswer4()) ||
                newQuestion.getAnswer3().equals(newQuestion.getAnswer4())) {
            // If any of the answers are the same, show an alert and return false
            showAlert("Duplicate Answers", "There are duplicate answers. Please try another one.");
            return false;
        }

        // If the question text and answers do not exist, proceed with adding the question
        // Get the difficulty of the new question
        Difficulty difficulty = newQuestion.getLevel();

        // Get the list of questions for the given difficulty level
        ArrayList<Question> questionList = questions.getOrDefault(difficulty, new ArrayList<>());

        // Add the new question to the list
        questionList.add(newQuestion);

        // Update the HashMap with the modified list
        questions.put(difficulty, questionList);

        // Save the updated questions to the JSON file
        saveQuestions();

        // Return true to indicate that the question was successfully added
        return true;
    }

    public boolean editQuestion(Question editedQuestion) {
        // Get the old difficulty of the edited question
        Difficulty oldDifficulty = editedQuestion.getLevel();

        // Check if the question exists in the old difficulty list
        ArrayList<Question> questionList = questions.get(oldDifficulty);
        if (questionList != null && questionList.contains(editedQuestion)) {
            // Update the question in the old difficulty list
            int index = questionList.indexOf(editedQuestion);
            questionList.set(index, editedQuestion);

            // Save the updated questions to the JSON file
            saveQuestions();

            return true;
        }

        // If the question is not found in the old difficulty list, return false
        return false;
    }

    public Question popQuestion(Difficulty level) {
        ArrayList<Question> array = questions.get(level);
        Question q = array.get(new Random().nextInt(array.size()));
        return q;
    }


    public static ArrayList<Game> loadGamesHistoryFromJson(String filename) {
        ArrayList<Game> gamesHistory = new ArrayList<>();

        try (FileReader fileReader = new FileReader(filename)) {
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(fileReader);

            if (obj instanceof JSONArray) {
                JSONArray gamesArray = (JSONArray) obj;

                // Iterate over JSON array and convert each object to Game
                for (Object gameObj : gamesArray) {
                    JSONObject gameJson = (JSONObject) gameObj;
                    String winnerName = (String) gameJson.get("winnerName");
                    String duration = (String) gameJson.get("duration");
                    String level = (String) gameJson.get("level");

                    // Create a new Game object
                    Game game = new Game(winnerName, duration, level);

                    // Add the game to the list
                    gamesHistory.add(game);
                }
            } else if (obj instanceof JSONObject) {
                JSONObject gameJson = (JSONObject) obj;
                String winnerName = (String) gameJson.get("winnerName");
                String duration = (String) gameJson.get("duration");
                String level = (String) gameJson.get("level");

                // Create a new Game object
                Game game = new Game(winnerName, duration, level);

                // Add the single game to the list
                gamesHistory.add(game);
            }

            System.out.println("Games history loaded from " + filename);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return gamesHistory;
    }



    public ArrayList<Game> getGamesHistory() {

        return GamesHistory;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
