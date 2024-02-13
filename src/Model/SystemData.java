package Model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class SystemData {

    private static SystemData instance;
    private HashMap<Difficulty, ArrayList<Question>> questions;
    private ArrayList<GameBoard> games;
    private ArrayList<Player> players;

    private String questionJSONPath = "src/JSON/questions_scheme.txt";
    private final String originalPath = questionJSONPath;

    private SystemData() {
        questions = new HashMap<>();
        games = new ArrayList<>();
        players = new ArrayList<>();
    }

    public static SystemData getInstance() {
        if (instance == null)
            instance = new SystemData();
        return instance;
    }

    public HashMap<Difficulty, ArrayList<Question>> getQuestions() {
        return questions;
    }

    public void setQuestions(HashMap<Difficulty, ArrayList<Question>> questions) {
        this.questions = questions;
    }

    public ArrayList<GameBoard> getGameBoards() {
        return games;
    }

    public void setGameBoards(ArrayList<GameBoard> games) {
        this.games = games;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public boolean loadQuestions(String externalPath) {
        String pathToUse = externalPath != null ? externalPath : originalPath;

        JSONParser parser = new JSONParser();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(pathToUse)))) {
            Object obj = parser.parse(reader);
            JSONObject jo = (JSONObject) obj;
            JSONArray questionsArray = (JSONArray) jo.get("questions");

            for (Object questionObj : questionsArray) {
                JSONObject q = (JSONObject) questionObj;
                String text = (String) q.get("question");
                int correctAnswer = Integer.parseInt(q.get("correct_ans").toString());
                Difficulty level = getQuestionLevel(Integer.parseInt(q.get("level").toString()));

                Question questionToAdd = new Question(text, correctAnswer, level);
                JSONArray answersArray = (JSONArray) q.get("answers");

                for (Object answerObj : answersArray) {
                    String answer = (String) answerObj;
                    questionToAdd.addAnswer(answer);
                }

                questions.computeIfAbsent(questionToAdd.getDifficultLevel(), k -> new ArrayList<>()).add(questionToAdd);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resetPathToDefault();
            return false;
        }
        resetPathToDefault();
        return true;
    }

    private void resetPathToDefault() {
        questionJSONPath = originalPath;
    }

    private static Difficulty getQuestionLevel(int level) {
        switch (level) {
            case 1:
                return Difficulty.EASY;
            case 2:
                return Difficulty.MEDIUM;
            case 3:
                return Difficulty.HARD;
            default:
                return Difficulty.MEDIUM;
        }
    }
}
