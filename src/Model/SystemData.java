// SystemData.java
package Model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SystemData {

    private static SystemData instance;
    private HashMap<Difficulty, ArrayList<Question>> questions;
    public ArrayList<GameBoard> games;
    public ArrayList<Player> players;

    // Singleton pattern: get instance of SystemData
    public static SystemData getInstance() {
        if (instance == null)
            instance = new SystemData();
        return instance;
    }

    // Private constructor to prevent instantiation
    private SystemData() {
        questions = new HashMap<Difficulty, ArrayList<Question>>();
        games = new ArrayList<GameBoard>();
        players = new ArrayList<Player>();
    }

    // Getter for questions
    public HashMap<Difficulty, ArrayList<Question>> getQuestions() {
        return questions;
    }

    // Setter for questions
    public void setQuestions(HashMap<Difficulty, ArrayList<Question>> questions) {
        this.questions = questions;
    }

    // Getter for games
    public ArrayList<GameBoard> getGames() {
        return games;
    }

    // Setter for games
    public void setGames(ArrayList<GameBoard> games) {
        this.games = games;
    }

    // Getter for players
    public ArrayList<Player> getPlayers() {
        return players;
    }

    // Setter for players
    public void setPlayers(ArrayList<Player> players) { this.players = players; }

    private int lastQuestionId = 0;

    // Load questions from JSON file
    public boolean loadQuestions() {
        JSONParser parser = new JSONParser();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("src/Model/questions_scheme.json")))) {
            Object obj = parser.parse(reader);
            JSONObject jo = (JSONObject) obj;
            JSONArray questionsArray = (JSONArray) jo.get("questions");

            // Iterate through JSON array of questions
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

                // Convert difficulty int to Difficulty enum
                Difficulty enumDifficulty = getQuestionDifficulty(difficulty);

                // Generate unique question ID
                int questionId = Question.generateUniqueId();

                Question questionToAdd = new Question(text, answer1, answer2, answer3, answer4, correctAnswer, enumDifficulty);
                questionToAdd.setQuestionId(questionId);
                questions.computeIfAbsent(enumDifficulty, k -> new ArrayList<>()).add(questionToAdd);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Map integer difficulty value to Difficulty enum
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

    // Save questions to JSON file
    public void saveQuestions() {
        try {
            JSONArray JSONQuestions = new JSONArray();
            JSONObject toWrite = new JSONObject();

            // Iterate through questions map and convert to JSON format
            for (ArrayList<Question> list : questions.values()) {
                if (list == null)
                    continue;

                for (Question q : list) {
                    JSONObject jo1 = new JSONObject();
                    jo1.put("questionId", q.getQuestionId());
                    jo1.put("question", q.getText());
                    jo1.put("answer1", q.getAnswer1());
                    jo1.put("answer2", q.getAnswer2());
                    jo1.put("answer3", q.getAnswer3());
                    jo1.put("answer4", q.getAnswer4());
                    jo1.put("correct_ans", q.getCorrectAnswer());
                    jo1.put("difficulty", q.getLevel());
                    JSONQuestions.add(jo1);
                }
            }
            toWrite.put("questions", JSONQuestions);

            FileWriter file = new FileWriter("Model/questions_scheme.json");
            file.write(toWrite.toJSONString());
            file.flush();
            System.out.println("JSON Question was saved successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
